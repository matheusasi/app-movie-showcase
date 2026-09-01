// Tela 2 — Buscar
// Responsável: integrante 2
package com.example.projetomobile.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import com.example.projetomobile.data.CATALOGO
import com.example.projetomobile.data.Filme
import com.example.projetomobile.data.GENEROS

@Composable
fun TelaBuscar(aoAbrirFilme: (Filme) -> Unit) {
    var texto by remember { mutableStateOf("") }
    var generoAtivo by remember { mutableStateOf(GENEROS[0]) }

    val resultados = CATALOGO.filter { filme ->
        val casaTexto = texto.isBlank() ||
            filme.titulo.contains(texto, ignoreCase = true) ||
            filme.diretor.contains(texto, ignoreCase = true)
        val casaGenero = generoAtivo == "Todos" ||
            filme.genero.startsWith(generoAtivo, ignoreCase = true)
        casaTexto && casaGenero
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .background(Color(0xFFFFFFFF))
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 18.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "BUSCAR",
                color = Color(0xFF1B1B1B),
                fontSize = 34.sp,
                fontWeight = FontWeight.Black
            )
            OutlinedTextField(
                value = texto,
                onValueChange = { texto = it },
                label = { Text("Buscar filme") },
                singleLine = true,
                trailingIcon = {
                    if (texto.isNotEmpty()) {
                        Text(
                            text = "✕",
                            color = Color(0xFFE8341C),
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier
                                .clickable { texto = "" }
                                .padding(12.dp)
                        )
                    }
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF1B1B1B),
                    unfocusedBorderColor = Color(0xFF1B1B1B),
                    focusedLabelColor = Color(0xFFE8341C),
                    cursorColor = Color(0xFFE8341C)
                ),
                modifier = Modifier.fillMaxWidth()
            )
        }
        Separador(cor = Color(0xFF1B1B1B))

        Column(
            modifier = Modifier
                .background(Color(0xFFFFFFFF))
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 18.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            TituloSecao("Gênero")
            GENEROS.chunked(3).forEach { linha ->
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    linha.forEach { genero ->
                        ChipGenero(
                            texto = genero,
                            ativo = genero == generoAtivo,
                            modifier = Modifier.weight(1f),
                            aoClicar = { generoAtivo = genero }
                        )
                    }
                    // Completa a última linha para os chips não esticarem.
                    repeat(3 - linha.size) { Box(modifier = Modifier.weight(1f)) }
                }
            }
        }
        Separador(cor = Color(0xFF1B1B1B))

        Row(
            modifier = Modifier
                .background(Color(0xFFFFFFFF))
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "RESULTADOS",
                color = Color(0xFF1B1B1B),
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
            )
            Text(
                text = if (resultados.size == 1) "1 filme" else "${resultados.size} filmes",
                color = Color(0xFF8C8C8C),
                fontSize = 14.sp
            )
        }

        if (resultados.isEmpty()) {
            Box(
                modifier = Modifier
                    .background(Color(0xFFFFFFFF))
                    .fillMaxSize(),
                contentAlignment = Alignment.TopCenter
            ) {
                Text(
                    text = "Nenhum filme encontrado.",
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
                items(resultados, key = { it.id }) { filme ->
                    ItemFilme(
                        filme = filme,
                        subtitulo = "${filme.ano} · ${filme.diretor}",
                        aoClicar = { aoAbrirFilme(filme) }
                    )
                    Separador()
                }
            }
        }
    }
}

@Composable
private fun ChipGenero(
    texto: String,
    ativo: Boolean,
    modifier: Modifier = Modifier,
    aoClicar: () -> Unit
) {
    Box(
        modifier = modifier
            .background(if (ativo) Color(0xFFE8341C) else Color(0xFFFFFFFF))
            .border(2.dp, if (ativo) Color(0xFFE8341C) else Color(0xFF1B1B1B))
            .clickable { aoClicar() }
            .padding(vertical = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = texto.uppercase(),
            color = if (ativo) Color(0xFFFFFFFF) else Color(0xFF1B1B1B),
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 0.5.sp
        )
    }
}
