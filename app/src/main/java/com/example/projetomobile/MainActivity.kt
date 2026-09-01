package com.example.projetomobile

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.example.projetomobile.data.CATALOGO
import com.example.projetomobile.data.Filme
import com.example.projetomobile.data.filmePorId
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
import com.example.projetomobile.ui.theme.ProjetoMobileTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ProjetoMobileTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    AppCineteca(Modifier.padding(innerPadding))
                }
            }
        }
    }
}

// Converte uma lista de ids numa lista de filmes (aula de coleções: forEach + .find).
fun filmesPorId(ids: List<Int>): List<Filme> {
    val filmes = mutableListOf<Filme>()
    ids.forEach { id ->
        val filme = filmePorId(id)
        if (filme != null) {
            filmes.add(filme)
        }
    }
    return filmes
}

// Devolve uma nova lista com o id incluído (sem duplicar).
fun comItem(lista: List<Int>, id: Int): List<Int> {
    val nova = mutableListOf<Int>()
    lista.forEach { nova.add(it) }
    if (!nova.contains(id)) {
        nova.add(id)
    }
    return nova
}

// Devolve uma nova lista sem o id.
fun semItem(lista: List<Int>, id: Int): List<Int> {
    val nova = mutableListOf<Int>()
    lista.forEach { atual ->
        if (atual != id) {
            nova.add(atual)
        }
    }
    return nova
}

@Composable
fun AppCineteca(modifier: Modifier = Modifier) {
    // Navegação simples: o nome da tela atual fica guardado num estado.
    var telaAtual by remember { mutableStateOf(TELA_INICIO) }
    var telaAnterior by remember { mutableStateOf(TELA_INICIO) }
    var filmeAberto by remember { mutableStateOf(CATALOGO[0]) }

    // Estado do app: ids dos filmes em cada aba da Minha Lista.
    var queroVer by remember { mutableStateOf(listOf(2, 12, 14)) }
    var assistidos by remember { mutableStateOf(listOf(4, 3)) }

    // Funções guardadas em variáveis (aula de funções: lambda em val).
    val abrirFilme: (Filme) -> Unit = { filme ->
        filmeAberto = filme
        telaAnterior = telaAtual
        telaAtual = TELA_DETALHE
    }
    val irPara: (String) -> Unit = { tela ->
        telaAnterior = telaAtual
        telaAtual = tela
    }

    val titulo = when (telaAtual) {
        TELA_BUSCAR -> "Buscar"
        TELA_LISTA -> "Minha Lista"
        TELA_AVALIAR -> "Avaliar"
        TELA_DETALHE -> "Detalhe"
        else -> "Cineteca"
    }

    Column(modifier = modifier.fillMaxSize()) {
        BarraTopo(titulo)
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxSize()
                .background(Color.LightGray)
        ) {
            when (telaAtual) {
                TELA_BUSCAR -> TelaBuscar(aoAbrirFilme = abrirFilme)

                TELA_LISTA -> TelaLista(
                    queroVer = filmesPorId(queroVer),
                    assistidos = filmesPorId(assistidos),
                    aoAbrirFilme = abrirFilme,
                    aoRemover = { filme ->
                        queroVer = semItem(queroVer, filme.id)
                        assistidos = semItem(assistidos, filme.id)
                    }
                )

                TELA_AVALIAR -> TelaAvaliar(
                    filmeInicial = filmeAberto,
                    aoSalvar = { avaliacao ->
                        queroVer = semItem(queroVer, avaliacao.filmeId)
                        assistidos = comItem(assistidos, avaliacao.filmeId)
                    }
                )

                TELA_DETALHE -> TelaDetalhe(
                    filme = filmeAberto,
                    naLista = queroVer.contains(filmeAberto.id) || assistidos.contains(filmeAberto.id),
                    aoVoltar = { telaAtual = telaAnterior },
                    aoAlternarLista = {
                        if (queroVer.contains(filmeAberto.id) || assistidos.contains(filmeAberto.id)) {
                            queroVer = semItem(queroVer, filmeAberto.id)
                            assistidos = semItem(assistidos, filmeAberto.id)
                        } else {
                            queroVer = comItem(queroVer, filmeAberto.id)
                        }
                    },
                    aoAvaliar = { irPara(TELA_AVALIAR) }
                )

                else -> TelaInicio(
                    tamanhoLista = queroVer.size + assistidos.size,
                    aoAbrirFilme = abrirFilme,
                    aoVerTodos = { irPara(TELA_BUSCAR) }
                )
            }
        }
        BarraInferior(telaAtual = telaAtual, aoTrocar = irPara)
    }
}

@Preview(showBackground = true)
@Composable
fun AppCinetecaPreview() {
    ProjetoMobileTheme {
        AppCineteca()
    }
}
