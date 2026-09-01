// Tela 4 — Minha Lista (abas Quero ver / Assistidos)
// Responsável: Matheus Rodrigues Cassab Asinelli Beyersdorff
package com.example.projetomobile.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.projetomobile.data.CATALOGO
import com.example.projetomobile.data.Filme
import com.example.projetomobile.ui.theme.ProjetoMobileTheme

@Composable
fun TelaLista(
    queroVer: List<Filme>,
    assistidos: List<Filme>,
    aoAbrirFilme: (Filme) -> Unit,
    aoRemover: (Filme) -> Unit,
    modifier: Modifier = Modifier
) {
    var abaQueroVer by remember { mutableStateOf(true) }
    val visiveis = if (abaQueroVer) queroVer else assistidos
    val total = queroVer.size + assistidos.size

    Column(modifier = modifier.fillMaxSize()) {

        Column(
            modifier = Modifier
                .background(Color.White)
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 14.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text("MINHA LISTA", color = Color.Black, fontSize = 30.sp, fontWeight = FontWeight.Bold)
            Text(
                text = if (total == 1) "1 filme salvo neste aparelho"
                else "$total filmes salvos neste aparelho",
                color = Color.Gray,
                fontSize = 14.sp
            )
        }

        Row(modifier = Modifier.fillMaxWidth()) {
            Aba("Quero ver (${queroVer.size})", abaQueroVer, Modifier.weight(1f)) { abaQueroVer = true }
            Aba("Assistidos (${assistidos.size})", !abaQueroVer, Modifier.weight(1f)) { abaQueroVer = false }
        }
        Separador(cor = Color.Black)

        Column(
            modifier = Modifier
                .background(Color.White)
                .fillMaxSize()
                .padding(horizontal = 20.dp)
        ) {
            if (visiveis.size == 0) {
                Text(
                    text = if (abaQueroVer) "Nenhum filme na fila. Adicione pela tela de detalhe."
                    else "Você ainda não marcou nenhum filme como assistido.",
                    color = Color.Gray,
                    fontSize = 15.sp,
                    modifier = Modifier.padding(vertical = 24.dp)
                )
            } else {
                visiveis.forEach { filme ->
                    ItemFilme(
                        filme = filme,
                        subtitulo = "${filme.ano} · ${filme.genero}",
                        mostrarNota = false,
                        acao = "Remover",
                        aoClicarAcao = { aoRemover(filme) },
                        aoClicar = { aoAbrirFilme(filme) }
                    )
                    Separador()
                }
            }
        }
    }
}

@Composable
private fun Aba(
    texto: String,
    ativa: Boolean,
    modifier: Modifier = Modifier,
    aoClicar: () -> Unit
) {
    Box(
        modifier = modifier
            .background(if (ativa) Color.Black else Color.White)
            .clickable { aoClicar() }
            .padding(vertical = 16.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = texto.uppercase(),
            color = if (ativa) Color.White else Color.Black,
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Preview(showBackground = true)
@Composable
fun TelaListaPreview() {
    ProjetoMobileTheme {
        TelaLista(
            queroVer = listOf(CATALOGO[1], CATALOGO[11]),
            assistidos = listOf(CATALOGO[3]),
            aoAbrirFilme = {},
            aoRemover = {}
        )
    }
}
