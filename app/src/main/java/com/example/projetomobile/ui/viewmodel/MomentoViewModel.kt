package com.example.projetomobile.ui.viewmodel

import android.app.Application
import android.graphics.Bitmap
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.projetomobile.auth.SessaoUsuario
import com.example.projetomobile.data.ImagemConversor
import com.example.projetomobile.data.Momento
import com.example.projetomobile.data.dal.MomentoDAO
import com.example.projetomobile.validation.MomentoValidator
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MomentoViewModel(application: Application): AndroidViewModel(application) {
    private val dao = MomentoDAO(application)
    var momentos by mutableStateOf<List<Momento>>(emptyList())
        private set
    var momento by mutableStateOf<Momento?>(null)
        private set
    var filmeId by mutableStateOf<Long?>(null)
        private set
    var legenda by mutableStateOf("")
        private set
    var foto by mutableStateOf<ByteArray?>(null)
        private set
    var abrindo by mutableStateOf(false)
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
        val lista = withContext(Dispatchers.IO) { dao.listar(atual) }
        momentos = lista
    }

    fun buscarPorId(id: Long){
        if(momento?.id == id) return
        executar {
            val atual = SessaoUsuario.exigirUsuario()
            val encontrado = withContext(Dispatchers.IO) { dao.buscarPorId(id, atual) }
            momento = requireNotNull(encontrado) {"Momento não encontrado"}
            filmeId = encontrado.filmeId
            legenda = encontrado.legenda
            foto = encontrado.foto
        }
    }

    fun mudarFilme(id: Long){
        if (!carregando) filmeId = id
    }

    fun mudarLegenda(valor: String){
        if (!carregando && valor.length <= 200) legenda = valor
    }

    fun iniciarCamera(){
        mensagem = null
        abrindo = true
    }

    fun receberFoto(imagem: Bitmap?){
        abrindo = false
        if (imagem == null) {
            mensagem = "Captura cancelada"
            return
        }
        foto = ImagemConversor.paraBlob(imagem)
    }

    fun informarFalha(){
        abrindo = false
        mensagem = "Não foi possível abrir a câmera"
    }

    fun salvar() = executar {
        val atual = SessaoUsuario.exigirUsuario()
        val legendaLimpa = legenda.trim()
        MomentoValidator.validar(filmeId, legendaLimpa, foto)
        val filmeEscolhido = requireNotNull(filmeId)
        val fotoTirada = requireNotNull(foto)

        val selecionado = momento
        withContext(Dispatchers.IO) {
            if (selecionado == null) {
                val data = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.forLanguageTag("pt-BR"))
                    .format(Date())
                dao.cadastrar(atual, filmeEscolhido, data, legendaLimpa, fotoTirada)
            } else {
                dao.alterar(selecionado.id, atual, filmeEscolhido, legendaLimpa, fotoTirada)
            }
        }
        salvo = true
    }

    fun excluir(id: Long) = executar {
        val atual = SessaoUsuario.exigirUsuario()
        withContext(Dispatchers.IO) { dao.excluir(id, atual) }
        momentos = momentos.filterNot { it.id == id }
        mensagem = "Momento excluído."
    }

    fun consumirSalvo(){ salvo = false }
}
