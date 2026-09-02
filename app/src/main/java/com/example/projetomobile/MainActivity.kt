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
import com.example.projetomobile.ui.BarraInferior
import com.example.projetomobile.ui.BarraTopo
import com.example.projetomobile.ui.TELA_AVALIAR
import com.example.projetomobile.ui.TELA_DETALHE
import com.example.projetomobile.ui.TELA_INICIO
import com.example.projetomobile.ui.TelaAvaliar
import com.example.projetomobile.ui.TelaDetalhe
import com.example.projetomobile.ui.TelaInicio
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

@Composable
fun AppCineteca(modifier: Modifier = Modifier) {
    // Navegação simples: o nome da tela atual fica guardado num estado.
    var telaAtual by remember { mutableStateOf(TELA_INICIO) }
    var telaAnterior by remember { mutableStateOf(TELA_INICIO) }
    var filmeAberto by remember { mutableStateOf(CATALOGO[0]) }

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
                TELA_DETALHE -> TelaDetalhe(
                    filme = filmeAberto,
                    aoVoltar = { telaAtual = telaAnterior },
                    aoAvaliar = { irPara(TELA_AVALIAR) }
                )

                TELA_AVALIAR -> TelaAvaliar(filmeInicial = filmeAberto)

                else -> TelaInicio(aoAbrirFilme = abrirFilme)
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
