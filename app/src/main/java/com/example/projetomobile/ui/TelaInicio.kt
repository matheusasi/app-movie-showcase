// Tela 1 — Início (Destaque da semana + recomendados)
// Responsável: integrante 1
package com.example.projetomobile.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.projetomobile.data.CATALOGO
import com.example.projetomobile.data.Filme
import com.example.projetomobile.data.formatarNota

@Composable
fun TelaInicio(
    tamanhoLista: Int,
    aoAbrirFilme: (Filme) -> Unit,
    aoVerTodos: () -> Unit
) {
    val destaque = CATALOGO[0]
    val recomendados = CATALOGO.subList(1, 4)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Column(
            modifier = Modifier
                .background(Color(0xFFFFFFFF))
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 22.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = "SUA BIBLIOTECA",
                color = Color(0xFFE8341C),
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
            )
            Text(
                text = "CINETECA",
                color = Color(0xFF1B1B1B),
                fontSize = 40.sp,
                fontWeight = FontWeight.Black
            )
            Text(
                text = "${CATALOGO.size} filmes catalogados · $tamanhoLista na sua lista",
                color = Color(0xFF8C8C8C),
                fontSize = 15.sp
            )
        }
        Separador(cor = Color(0xFF1B1B1B))

        Column(
            modifier = Modifier
                .background(Color(0xFFFFFFFF))
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 18.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            TituloSecao("Destaque da semana")
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(16f / 9f)
                    .background(Color(0xFFBDB9B6))
                    .clickable { aoAbrirFilme(destaque) },
                contentAlignment = Alignment.BottomStart
            ) {
                Text(
                    text = destaque.titulo.uppercase(),
                    color = Color(0xFFFFFFFF),
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Black,
                    modifier = Modifier
                        .padding(16.dp)
                        .background(Color(0xFFE8341C))
                        .padding(horizontal = 16.dp, vertical = 10.dp)
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "${destaque.ano} · ${destaque.genero} · ${destaque.duracaoMin} min",
                    color = Color(0xFF8C8C8C),
                    fontSize = 15.sp
                )
                Text(
                    text = "NOTA ${formatarNota(destaque.nota)}",
                    color = Color(0xFFE8341C),
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
        Separador(cor = Color(0xFF1B1B1B))

        Column(
            modifier = Modifier
                .background(Color(0xFFFFFFFF))
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 18.dp)
        ) {
            TituloSecao(
                texto = "Recomendados para você",
                acao = "Ver todos",
                aoClicarAcao = aoVerTodos
            )
            recomendados.forEachIndexed { indice, filme ->
                ItemFilme(
                    filme = filme,
                    subtitulo = "${filme.ano} · ${filme.genero}",
                    aoClicar = { aoAbrirFilme(filme) }
                )
                if (indice < recomendados.lastIndex) Separador()
            }
        }
    }
}
