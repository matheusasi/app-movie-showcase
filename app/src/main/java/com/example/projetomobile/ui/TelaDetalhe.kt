// Tela 3 — Detalhe do filme
// Responsável: integrante 3
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
import com.example.projetomobile.data.Filme
import com.example.projetomobile.data.formatarNota
import com.example.projetomobile.data.formatarTotal

@Composable
fun TelaDetalhe(
    filme: Filme,
    naLista: Boolean,
    aoVoltar: () -> Unit,
    aoAlternarLista: () -> Unit,
    aoAvaliar: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Box(
            modifier = Modifier
                .background(Color(0xFFFFFFFF))
                .fillMaxWidth()
                .clickable { aoVoltar() }
                .padding(horizontal = 20.dp, vertical = 16.dp)
        ) {
            Text(
                text = "‹ VOLTAR",
                color = Color(0xFFE8341C),
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.5.sp
            )
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(240.dp)
                .background(Color(0xFFBDB9B6))
        )

        Column(
            modifier = Modifier
                .background(Color(0xFFFFFFFF))
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 18.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(
                text = filme.titulo.uppercase(),
                color = Color(0xFF1B1B1B),
                fontSize = 32.sp,
                fontWeight = FontWeight.Black,
                lineHeight = 36.sp
            )
            Text(
                text = "${filme.ano} · ${filme.genero} · ${filme.duracaoMin} min · ${filme.diretor}",
                color = Color(0xFF8C8C8C),
                fontSize = 15.sp
            )
            Row(
                modifier = Modifier.padding(top = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Badge(formatarNota(filme.nota))
                Column(modifier = Modifier.padding(start = 14.dp)) {
                    Text(
                        text = "NOTA MÉDIA",
                        color = Color(0xFF1B1B1B),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.5.sp
                    )
                    Text(
                        text = "de ${formatarTotal(filme.totalAvaliacoes)} avaliações",
                        color = Color(0xFF8C8C8C),
                        fontSize = 14.sp
                    )
                }
            }
        }
        Separador(cor = Color(0xFF1B1B1B))

        Column(
            modifier = Modifier
                .background(Color(0xFFFFFFFF))
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 18.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            TituloSecao("Sinopse")
            Text(
                text = filme.sinopse,
                color = Color(0xFF1B1B1B),
                fontSize = 16.sp,
                lineHeight = 24.sp
            )
        }
        Separador(cor = Color(0xFF1B1B1B))

        Column(
            modifier = Modifier
                .background(Color(0xFFFFFFFF))
                .fillMaxWidth()
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            BotaoPrimario(
                texto = if (naLista) "Remover da minha lista" else "Adicionar à minha lista",
                aoClicar = aoAlternarLista
            )
            BotaoSecundario(texto = "Avaliar este filme", aoClicar = aoAvaliar)
        }
    }
}
