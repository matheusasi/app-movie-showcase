package com.example.projetomobile.ui.viewmodel

import android.app.Application
import android.database.sqlite.SQLiteConstraintException
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.projetomobile.auth.SessaoUsuario
import com.example.projetomobile.data.Avaliacao
import com.example.projetomobile.data.dal.AvaliacaoDAO
import com.example.projetomobile.data.formatarNota
import com.example.projetomobile.validation.AvaliacaoValidator
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AvaliacaoViewModel(application: Application): AndroidViewModel(application) {
    private val dao = AvaliacaoDAO(application)
    val usuarioAtualId: Long? get() = SessaoUsuario.usuarioId
    var avaliacoes by mutableStateOf<List<Avaliacao>>(emptyList())
        private set
    var avaliacao by mutableStateOf<Avaliacao?>(null)
        private set
    var onde by mutableStateOf("")
        private set
    var comentario by mutableStateOf("")
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
                mensagem = "Você já avaliou este filme."
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
        val lista = withContext(Dispatchers.IO) { dao.listar(atual) }
        avaliacoes = lista
    }

    fun cadastrar(filmeId: Long, data: String, onde: String, nota: Float, comentario: String) = executar {
        val atual = SessaoUsuario.exigirUsuario()
        val comentarioLimpo = comentario.trim()
        AvaliacaoValidator.validar(data, nota, comentarioLimpo)
        withContext(Dispatchers.IO) {
            dao.cadastrar(atual, filmeId, data, onde, formatarNota(nota), comentarioLimpo)
        }
        mensagem = "Avaliação salva com sucesso."
        salvo = true
    }

    fun buscarPorId(id: Long){
        if(avaliacao?.id == id) return
        executar {
            val atual = SessaoUsuario.exigirUsuario()
            val encontrada = withContext(Dispatchers.IO) { dao.buscarPorId(id, atual) }
            avaliacao = requireNotNull(encontrada) {"Avaliação não encontrada"}
            onde = encontrada.onde
            comentario = encontrada.comentario
        }
    }

    fun mudarOnde(valor: String){
        if (!carregando) onde = valor
    }

    fun mudarComentario(valor: String){
        if (!carregando && valor.length <= 500) comentario = valor
    }

    fun salvar() = executar {
        val atual = SessaoUsuario.exigirUsuario()
        val selecionada = requireNotNull(avaliacao) { "Selecione uma avaliação" }
        val comentarioLimpo = comentario.trim()
        AvaliacaoValidator.validar(comentarioLimpo)
        withContext(Dispatchers.IO) {
            dao.alterar(selecionada.id, atual, onde, comentarioLimpo)
        }
        salvo = true
    }

    fun excluir(id: Long) = executar {
        val atual = SessaoUsuario.exigirUsuario()
        withContext(Dispatchers.IO) { dao.excluir(id, atual) }
        avaliacoes = avaliacoes.filterNot { it.id == id }
        mensagem = "Avaliação excluída."
    }

    fun consumirSalvo(){ salvo = false }
}
