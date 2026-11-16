package com.example.projetomobile.ui.viewmodel

import android.app.Application
import android.database.sqlite.SQLiteConstraintException
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.projetomobile.auth.SessaoUsuario
import com.example.projetomobile.data.ItemLista
import com.example.projetomobile.data.dal.MinhaListaDAO
import com.example.projetomobile.validation.ListaValidator
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MinhaListaViewModel(application: Application): AndroidViewModel(application) {
    private val dao = MinhaListaDAO(application)
    var itens by mutableStateOf<List<ItemLista>>(emptyList())
        private set
    var item by mutableStateOf<ItemLista?>(null)
        private set
    var status by mutableStateOf("")
        private set
    var observacao by mutableStateOf("")
        private set
    var mensagem by mutableStateOf<String?>(null)
        private set
    var carregando by mutableStateOf(false)
        private set
    var salvo by mutableStateOf(false)
        private set

    private fun executar(operacao: suspend () -> Unit){
        if (carregando || salvo) return
        carregando = true
        mensagem = null

        viewModelScope.launch {
            try{
                SessaoUsuario.exigirUsuario()
                operacao()
            }catch(erro: CancellationException){
                throw erro
            }catch(erro: SQLiteConstraintException){
                mensagem = "Este filme já está na sua lista."
            }catch(erro: IllegalArgumentException){
                mensagem = erro.message
            }catch(erro: Exception){
                mensagem = "Ocorreu um erro inesperado!"
            }finally {
                carregando = false
            }
        }
    }

    fun naLista(filmeId: Long): Boolean = itens.any { it.filmeId == filmeId }

    fun listar() = executar {
        val atual = SessaoUsuario.exigirUsuario()
        val lista = withContext(Dispatchers.IO) { dao.listar(atual) }
        itens = lista
    }

    fun adicionar(filmeId: Long) = executar {
        val atual = SessaoUsuario.exigirUsuario()
        val lista = withContext(Dispatchers.IO) {
            dao.adicionar(atual, filmeId)
            dao.listar(atual)
        }
        itens = lista
        mensagem = "Filme adicionado à sua lista."
    }

    fun remover(filmeId: Long){
        val encontrado = itens.firstOrNull { it.filmeId == filmeId } ?: return
        excluir(encontrado.id)
    }

    fun buscarPorId(id: Long){
        if(item?.id == id) return
        executar {
            val atual = SessaoUsuario.exigirUsuario()
            val encontrado = withContext(Dispatchers.IO) { dao.buscarPorId(id, atual) }
            item = requireNotNull(encontrado) {"Filme não encontrado na lista"}
            status = encontrado.status
            observacao = encontrado.observacao
        }
    }

    fun mudarStatus(valor: String){
        if (!carregando) status = valor
    }

    fun mudarObservacao(valor: String){
        if (!carregando && valor.length <= 200) observacao = valor
    }

    fun salvar() = executar {
        val atual = SessaoUsuario.exigirUsuario()
        val selecionado = requireNotNull(item) { "Selecione um filme da lista" }
        val observacaoLimpa = observacao.trim()
        ListaValidator.validar(observacaoLimpa)
        withContext(Dispatchers.IO) {
            dao.alterar(selecionado.id, atual, status, observacaoLimpa)
        }
        salvo = true
    }

    fun excluir(id: Long) = executar {
        val atual = SessaoUsuario.exigirUsuario()
        withContext(Dispatchers.IO) { dao.excluir(id, atual) }
        itens = itens.filterNot { it.id == id }
        mensagem = "Filme removido da sua lista."
    }

    fun consumirSalvo(){ salvo = false }
}
