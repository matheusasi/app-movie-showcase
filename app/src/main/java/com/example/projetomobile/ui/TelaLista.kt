// Tela 4 — Minha Lista
// Responsável: integrante 1
package com.example.projetomobile.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.projetomobile.data.Filme

@Composable
fun TelaLista(
    queroVer: List<Filme>,
    assistidos: List<Filme>,
    aoAbrirFilme: (Filme) -> Unit,
    aoRemover: (Filme) -> Unit
) {
    var abaQueroVer by remember { mutableStateOf(true) }
    val visiveis = if (abaQueroVer) queroVer else assistidos
    val total = queroVer.size + assistidos.size

    Column(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .background(Color(0xFFFFFFFF))
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 18.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = "MINHA LISTA",
                color = Color(0xFF1B1B1B),
                fontSize = 34.sp,
                fontWeight = FontWeight.Black
            )
            Text(
                text = if (total == 1) "1 filme salvo neste aparelho"
                else "$total filmes salvos neste aparelho",
                color = Color(0xFF8C8C8C),
                fontSize = 15.sp
            )
        }

        Row(modifier = Modifier.fillMaxWidth()) {
            Aba(
                texto = "Quero ver (${queroVer.size})",
                ativa = abaQueroVer,
                modifier = Modifier.weight(1f),
                aoClicar = { abaQueroVer = true }
            )
            Aba(
                texto = "Assistidos (${assistidos.size})",
                ativa = !abaQueroVer,
                modifier = Modifier.weight(1f),
                aoClicar = { abaQueroVer = false }
            )
        }
        Separador(cor = Color(0xFF1B1B1B))

        if (visiveis.isEmpty()) {
            Box(
                modifier = Modifier
                    .background(Color(0xFFFFFFFF))
                    .fillMaxSize(),
                contentAlignment = Alignment.TopCenter
            ) {
                Text(
                    text = if (abaQueroVer) "Nenhum filme na fila. Adicione pela tela de detalhe."
                    else "Você ainda não marcou nenhum filme como assistido.",
                    color = Color(0xFF8C8C8C),
                    fontSize = 15.sp,
                    modifier = Modifier.padding(32.dp)
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .background(Color(0xFFFFFFFF))
                    .fillMaxSize()
                    .padding(horizontal = 20.dp)
            ) {
                items(visiveis, key = { it.id }) { filme ->
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
            .background(if (ativa) Color(0xFF1B1B1B) else Color(0xFFFFFFFF))
            .clickable { aoClicar() }
            .height(56.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = texto.uppercase(),
            color = if (ativa) Color(0xFFFFFFFF) else Color(0xFF1B1B1B),
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 0.5.sp
        )
    }
}
