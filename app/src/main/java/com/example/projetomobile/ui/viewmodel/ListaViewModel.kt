package com.example.projetomobile.ui.viewmodel

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.projetomobile.auth.SessaoUsuario
import com.example.projetomobile.data.ListaPersonalizada
import com.example.projetomobile.data.dal.ListaDAO
import com.example.projetomobile.validation.ListaValidator
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ListaViewModel(application: Application): AndroidViewModel(application) {
    private val dao = ListaDAO(application)
    var listas by mutableStateOf<List<ListaPersonalizada>>(emptyList())
        private set
    var lista by mutableStateOf<ListaPersonalizada?>(null)
        private set
    var nome by mutableStateOf("")
        private set
    var descricao by mutableStateOf("")
        private set
    var filmesSelecionados by mutableStateOf<List<Long>>(emptyList())
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
            }catch(erro: IllegalArgumentException){
                mensagem = erro.message
            }catch(erro: Exception){
                mensagem = "Ocorreu um erro inesperado!"
            }finally {
                carregando = false
            }
        }
    }

    fun listar() = executar {
        val atual = SessaoUsuario.exigirUsuario()
        val resultado = withContext(Dispatchers.IO) { dao.listar(atual) }
        listas = resultado
    }

    fun buscarPorId(id: Long){
        if(lista?.id == id) return
        executar {
            val atual = SessaoUsuario.exigirUsuario()
            val encontrada = withContext(Dispatchers.IO) { dao.buscarPorId(id, atual) }
            lista = requireNotNull(encontrada) {"Lista não encontrada"}
            nome = encontrada.nome
            descricao = encontrada.descricao
            filmesSelecionados = encontrada.filmes
        }
    }

    fun mudarNome(valor: String){
        if (!carregando && valor.length <= 60) nome = valor
    }

    fun mudarDescricao(valor: String){
        if (!carregando && valor.length <= 200) descricao = valor
    }

    fun alternarFilme(filmeId: Long){
        if (carregando) return
        filmesSelecionados = if (filmesSelecionados.contains(filmeId))
            filmesSelecionados - filmeId
        else filmesSelecionados + filmeId
    }

    fun salvar() = executar {
        val atual = SessaoUsuario.exigirUsuario()
        val nomeLimpo = nome.trim()
        val descricaoLimpa = descricao.trim()
        ListaValidator.validar(nomeLimpo, descricaoLimpa, filmesSelecionados)

        val selecionada = lista
        withContext(Dispatchers.IO) {
            if (selecionada == null) {
                dao.cadastrar(atual, nomeLimpo, descricaoLimpa, filmesSelecionados)
            } else {
                dao.alterar(selecionada.id, atual, nomeLimpo, descricaoLimpa, filmesSelecionados)
            }
        }
        salvo = true
    }

    fun excluir(id: Long) = executar {
        val atual = SessaoUsuario.exigirUsuario()
        withContext(Dispatchers.IO) { dao.excluir(id, atual) }
        listas = listas.filterNot { it.id == id }
        mensagem = "Lista excluída."
    }

    fun consumirSalvo(){ salvo = false }
}
