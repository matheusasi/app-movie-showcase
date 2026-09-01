// Tela 3 — Detalhe do filme (sinopse, nota média e ações)
// Responsável: João Adolfo Bonato
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
fun TelaDetalhe(
    filme: Filme,
    naLista: Boolean,
    aoVoltar: () -> Unit,
    aoAlternarLista: () -> Unit,
    aoAvaliar: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxSize()) {

        Box(
            modifier = Modifier
                .background(Color.White)
                .fillMaxWidth()
                .clickable { aoVoltar() }
                .padding(horizontal = 20.dp, vertical = 12.dp)
        ) {
            Text("‹ VOLTAR", color = Color.Red, fontSize = 15.sp, fontWeight = FontWeight.Bold)
        }

        Poster(
            modifier = Modifier
                .fillMaxWidth()
                .height(140.dp)
        )

        Column(
            modifier = Modifier
                .background(Color.White)
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(
                text = filme.titulo.uppercase(),
                color = Color.Black,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "${filme.ano} · ${filme.genero} · ${filme.duracaoMin} min · ${filme.diretor}",
                color = Color.Gray,
                fontSize = 14.sp
            )
            Row(
                modifier = Modifier.padding(top = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Badge(formatarNota(filme.nota))
                Column(modifier = Modifier.padding(start = 14.dp)) {
                    Text("NOTA MÉDIA", color = Color.Black, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                    Text(
                        text = "de ${filme.totalAvaliacoes} avaliações",
                        color = Color.Gray,
                        fontSize = 14.sp
                    )
                }
            }
        }
        Separador(cor = Color.Black)

        Column(
            modifier = Modifier
                .background(Color.White)
                .fillMaxWidth()
                .weight(1f)
                .padding(horizontal = 20.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            TituloSecao("Sinopse")
            Text(text = filme.sinopse, color = Color.Black, fontSize = 15.sp)
        }
        Separador(cor = Color.Black)

        Column(
            modifier = Modifier
                .background(Color.White)
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            BotaoPrimario(
                texto = if (naLista) "Remover da minha lista" else "Adicionar à minha lista",
                aoClicar = aoAlternarLista
            )
            BotaoSecundario(texto = "Avaliar este filme", aoClicar = aoAvaliar)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TelaDetalhePreview() {
    ProjetoMobileTheme {
        TelaDetalhe(
            filme = CATALOGO[0],
            naLista = false,
            aoVoltar = {},
            aoAlternarLista = {},
            aoAvaliar = {}
        )
    }
}
