package com.example.projetomobile.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.projetomobile.data.Filme
import com.example.projetomobile.data.formatarNota

// Nomes das telas. A navegacao no MainActivity usa when(telaAtual) com estes valores.
val TELA_INICIO = "inicio"
val TELA_DETALHE = "detalhe"
val TELA_AVALIAR = "avaliar"

@Composable
fun BarraTopo(titulo: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.Black)
            .height(44.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = titulo.uppercase(),
            color = Color.White,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun BarraInferior(telaAtual: String, aoTrocar: (String) -> Unit) {
    Column(modifier = Modifier.background(Color.White)) {
        Separador(cor = Color.Black)
        Row(modifier = Modifier.fillMaxWidth()) {
            ItemBarra("Início", TELA_INICIO, telaAtual, Modifier.weight(1f), aoTrocar)
            ItemBarra("Avaliar", TELA_AVALIAR, telaAtual, Modifier.weight(1f), aoTrocar)
        }
    }
}

@Composable
private fun ItemBarra(
    rotulo: String,
    tela: String,
    telaAtual: String,
    modifier: Modifier,
    aoTrocar: (String) -> Unit
) {
    val ativa = telaAtual == tela || (telaAtual == TELA_DETALHE && tela == TELA_INICIO)
    Box(
        modifier = modifier
            .background(if (ativa) Color.Red else Color.White)
            .clickable { aoTrocar(tela) }
            .height(56.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = rotulo.uppercase(),
            color = if (ativa) Color.White else Color.Black,
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

// Retangulo cinza no lugar do poster (a aula nao ensinou a carregar imagem).
@Composable
fun Poster(modifier: Modifier = Modifier) {
    Box(modifier = modifier.background(Color.Gray))
}

@Composable
fun TituloSecao(texto: String, modifier: Modifier = Modifier) {
    Text(
        text = texto.uppercase(),
        color = Color.Black,
        fontSize = 14.sp,
        fontWeight = FontWeight.Bold,
        modifier = modifier
    )
}

@Composable
fun ItemFilme(
    filme: Filme,
    subtitulo: String,
    modifier: Modifier = Modifier,
    aoClicar: () -> Unit = {}
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable { aoClicar() }
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Poster(
            modifier = Modifier
                .width(56.dp)
                .height(80.dp)
        )
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 14.dp)
        ) {
            Text(
                text = filme.titulo.uppercase(),
                color = Color.Black,
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold
            )
            Text(text = subtitulo, color = Color.Gray, fontSize = 14.sp)
            Text(
                text = "NOTA ${formatarNota(filme.nota)}",
                color = Color.Red,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun BotaoPrimario(texto: String, modifier: Modifier = Modifier, aoClicar: () -> Unit) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.Red)
            .clickable { aoClicar() }
            .padding(vertical = 16.dp, horizontal = 20.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = texto.uppercase(),
            color = Color.White,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun Separador(modifier: Modifier = Modifier, cor: Color = Color.LightGray) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(1.dp)
            .background(cor)
    )
}

@Composable
fun Badge(texto: String, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .width(72.dp)
            .height(52.dp)
            .background(Color.Red),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = texto,
            color = Color.White,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )
    }
}
