package com.example.projetomobile

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.example.projetomobile.data.Avaliacao
import com.example.projetomobile.data.CATALOGO
import com.example.projetomobile.data.Filme
import com.example.projetomobile.ui.BarraInferior
import com.example.projetomobile.ui.BarraTopo
import com.example.projetomobile.ui.TELA_AVALIAR
import com.example.projetomobile.ui.TELA_BUSCAR
import com.example.projetomobile.ui.TELA_DETALHE
import com.example.projetomobile.ui.TELA_INICIO
import com.example.projetomobile.ui.TELA_LISTA
import com.example.projetomobile.ui.TelaAvaliar
import com.example.projetomobile.ui.TelaBuscar
import com.example.projetomobile.ui.TelaDetalhe
import com.example.projetomobile.ui.TelaInicio
import com.example.projetomobile.ui.TelaLista

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                AppCineteca()
            }
        }
    }
}

@Composable
fun AppCineteca() {
    // Navegacao simples: guardamos o nome da tela atual em um estado.
    var telaAtual by remember { mutableStateOf(TELA_INICIO) }
    var telaAnterior by remember { mutableStateOf(TELA_INICIO) }
    var filmeAberto by remember { mutableStateOf<Filme?>(null) }

    // Estado do app: ids dos filmes em cada aba da Minha Lista.
    val queroVer = remember { mutableStateListOf(1, 2, 7, 8, 9) }
    val assistidos = remember { mutableStateListOf(4, 3, 6) }
    val avaliacoes = remember { mutableStateListOf<Avaliacao>() }

    fun irPara(tela: String) {
        telaAnterior = telaAtual
        telaAtual = tela
    }

    fun abrirFilme(filme: Filme) {
        filmeAberto = filme
        irPara(TELA_DETALHE)
    }

    val titulo = when (telaAtual) {
        TELA_BUSCAR -> "Buscar"
        TELA_LISTA -> "Minha Lista"
        TELA_AVALIAR -> "Avaliar"
        TELA_DETALHE -> "Detalhe"
        else -> "Cineteca"
    }

    Column(modifier = Modifier.fillMaxSize()) {
        BarraTopo(titulo)
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxSize()
                .background(Color(0xFFF3F1EF))
        ) {
            when (telaAtual) {
                TELA_INICIO -> TelaInicio(
                    tamanhoLista = queroVer.size + assistidos.size,
                    aoAbrirFilme = { abrirFilme(it) },
                    aoVerTodos = { irPara(TELA_BUSCAR) }
                )

                TELA_BUSCAR -> TelaBuscar(
                    aoAbrirFilme = { abrirFilme(it) }
                )

                TELA_LISTA -> TelaLista(
                    queroVer = queroVer.mapNotNull { id -> CATALOGO.find { it.id == id } },
                    assistidos = assistidos.mapNotNull { id -> CATALOGO.find { it.id == id } },
                    aoAbrirFilme = { abrirFilme(it) },
                    aoRemover = { filme ->
                        queroVer.remove(filme.id)
                        assistidos.remove(filme.id)
                    }
                )

                TELA_AVALIAR -> TelaAvaliar(
                    filmeInicial = filmeAberto,
                    aoSalvar = { avaliacao ->
                        avaliacoes.add(avaliacao)
                        queroVer.remove(avaliacao.filmeId)
                        if (!assistidos.contains(avaliacao.filmeId)) {
                            assistidos.add(avaliacao.filmeId)
                        }
                    }
                )

                TELA_DETALHE -> {
                    val filme = filmeAberto
                    if (filme == null) {
                        telaAtual = TELA_INICIO
                    } else {
                        TelaDetalhe(
                            filme = filme,
                            naLista = queroVer.contains(filme.id) || assistidos.contains(filme.id),
                            aoVoltar = { irPara(telaAnterior) },
                            aoAlternarLista = {
                                if (queroVer.contains(filme.id) || assistidos.contains(filme.id)) {
                                    queroVer.remove(filme.id)
                                    assistidos.remove(filme.id)
                                } else {
                                    queroVer.add(filme.id)
                                }
                            },
                            aoAvaliar = { irPara(TELA_AVALIAR) }
                        )
                    }
                }
            }
        }
        BarraInferior(telaAtual = telaAtual, aoTrocar = { irPara(it) })
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun AppCinetecaPreview() {
    MaterialTheme {
        AppCineteca()
    }
}
