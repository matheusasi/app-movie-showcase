package com.example.projetomobile.ui.viewmodel

import android.app.Application
import android.database.sqlite.SQLiteConstraintException
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.projetomobile.auth.SessaoUsuario
import com.example.projetomobile.data.Filme
import com.example.projetomobile.data.dal.FilmeDAO
import com.example.projetomobile.validation.FilmeValidator
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FilmeViewModel(application: Application): AndroidViewModel(application) {
    private val dao = FilmeDAO(application)
    var filmes by mutableStateOf<List<Filme>>(emptyList())
        private set
    var filme by mutableStateOf<Filme?>(null)
        private set
    var titulo by mutableStateOf("")
        private set
    var ano by mutableStateOf("")
        private set
    var genero by mutableStateOf("")
        private set
    var duracao by mutableStateOf("")
        private set
    var diretor by mutableStateOf("")
        private set
    var sinopse by mutableStateOf("")
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
                mensagem = "Use só números: ano entre 1888 e 2100 e duração entre 1 e 600 minutos."
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
        val lista = withContext(Dispatchers.IO) { dao.listar() }
        filmes = lista
    }

    fun buscarPorId(id: Long){
        if(filme?.id == id) return
        executar {
            val encontrado = withContext(Dispatchers.IO) { dao.buscarPorId(id) }
            filme = requireNotNull(encontrado) {"Filme não encontrado"}
            titulo = encontrado.titulo
            ano = encontrado.ano.toString()
            genero = encontrado.genero
            duracao = encontrado.duracaoMin.toString()
            diretor = encontrado.diretor
            sinopse = encontrado.sinopse
        }
    }

    fun mudarTitulo(valor: String){
        if (!carregando && valor.length <= 100) titulo = valor
    }

    fun mudarAno(valor: String){
        if (!carregando && valor.length <= 4) ano = valor
    }

    fun mudarGenero(valor: String){
        if (!carregando && valor.length <= 40) genero = valor
    }

    fun mudarDuracao(valor: String){
        if (!carregando && valor.length <= 3) duracao = valor
    }

    fun mudarDiretor(valor: String){
        if (!carregando && valor.length <= 100) diretor = valor
    }

    fun mudarSinopse(valor: String){
        if (!carregando && valor.length <= 1000) sinopse = valor
    }

    fun salvar() = executar {
        val tituloLimpo = titulo.trim()
        val anoLimpo = ano.trim()
        val generoLimpo = genero.trim()
        val duracaoLimpa = duracao.trim()
        val diretorLimpo = diretor.trim()
        val sinopseLimpa = sinopse.trim()

        FilmeValidator.validar(tituloLimpo, anoLimpo, generoLimpo, duracaoLimpa,
            diretorLimpo, sinopseLimpa)

        val selecionado = filme
        withContext(Dispatchers.IO) {
            if (selecionado == null) {
                dao.cadastrar(tituloLimpo, anoLimpo, generoLimpo, duracaoLimpa,
                    diretorLimpo, sinopseLimpa)
            } else {
                dao.alterar(selecionado.id, tituloLimpo, anoLimpo, generoLimpo,
                    duracaoLimpa, diretorLimpo, sinopseLimpa)
            }
        }
        salvo = true
    }

    fun excluir(id: Long) = executar {
        withContext(Dispatchers.IO) { dao.excluir(id) }
        filmes = filmes.filterNot { it.id == id }
        mensagem = "Filme excluído."
    }

    fun consumirSalvo(){ salvo = false }
}
