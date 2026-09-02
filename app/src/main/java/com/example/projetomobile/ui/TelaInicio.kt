// Tela 1 — Início (destaque da semana + recomendados)
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
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.projetomobile.data.CATALOGO
import com.example.projetomobile.data.Filme
import com.example.projetomobile.data.formatarNota
import com.example.projetomobile.ui.theme.ProjetoMobileTheme

@Composable
fun TelaInicio(
    aoAbrirFilme: (Filme) -> Unit,
    modifier: Modifier = Modifier
) {
    val destaque = CATALOGO[0]
    val recomendados = listOf(CATALOGO[1], CATALOGO[2], CATALOGO[3])

    Column(modifier = modifier.fillMaxSize()) {

        // Cabeçalho
        Column(
            modifier = Modifier
                .background(Color.White)
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text("SUA BIBLIOTECA", color = Color.Red, fontSize = 13.sp, fontWeight = FontWeight.Bold)
            Text("CINETECA", color = Color.Black, fontSize = 34.sp, fontWeight = FontWeight.Bold)
            Text(
                text = "${CATALOGO.size} filmes catalogados",
                color = Color.Gray,
                fontSize = 14.sp
            )
        }
        Separador(cor = Color.Black)

        // Destaque da semana
        Column(
            modifier = Modifier
                .background(Color.White)
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 14.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            TituloSecao("Destaque da semana")
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp)
                    .background(Color.Gray)
                    .clickable { aoAbrirFilme(destaque) },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = destaque.titulo.uppercase(),
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "${destaque.ano} · ${destaque.genero} · ${destaque.duracaoMin} min",
                    color = Color.Gray,
                    fontSize = 14.sp
                )
                Text(
                    text = "NOTA ${formatarNota(destaque.nota)}",
                    color = Color.Red,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
        Separador(cor = Color.Black)

        // Recomendados para você
        Column(
            modifier = Modifier
                .background(Color.White)
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 14.dp)
        ) {
            TituloSecao("Recomendados para você")
            recomendados.forEach { filme ->
                ItemFilme(
                    filme = filme,
                    subtitulo = "${filme.ano} · ${filme.genero}",
                    aoClicar = { aoAbrirFilme(filme) }
                )
                Separador()
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TelaInicioPreview() {
    ProjetoMobileTheme {
        TelaInicio(aoAbrirFilme = {})
    }
}
