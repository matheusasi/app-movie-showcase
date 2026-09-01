// Tela 2 — Buscar (campo de texto + filtro por gênero)
// Responsável: Matheus Henrique Farias de Jesus
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.projetomobile.data.CATALOGO
import com.example.projetomobile.data.Filme
import com.example.projetomobile.data.GENEROS
import com.example.projetomobile.ui.theme.ProjetoMobileTheme

@Composable
fun TelaBuscar(
    aoAbrirFilme: (Filme) -> Unit,
    modifier: Modifier = Modifier
) {
    var texto by remember { mutableStateOf("") }
    var generoAtivo by remember { mutableStateOf(GENEROS[0]) }

    val coresInputs = OutlinedTextFieldDefaults.colors(
        focusedBorderColor = Color.Black,
        unfocusedBorderColor = Color.Black
    )

    // Filtro feito com forEach + if (aula de coleções), sem usar .filter.
    // "x in texto" usa o mesmo operador 'in' da aula (aqui: "está contido em").
    val resultados = mutableListOf<Filme>()
    CATALOGO.forEach { filme ->
        val busca = texto.lowercase()
        val casaTexto = texto == "" ||
            busca in filme.titulo.lowercase() ||
            busca in filme.diretor.lowercase()
        val casaGenero = generoAtivo == "Todos" || filme.genero == generoAtivo
        if (casaTexto && casaGenero) {
            resultados.add(filme)
        }
    }

    Column(modifier = modifier.fillMaxSize()) {

        Column(
            modifier = Modifier
                .background(Color.White)
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 14.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text("BUSCAR", color = Color.Black, fontSize = 30.sp, fontWeight = FontWeight.Bold)
            OutlinedTextField(
                value = texto,
                onValueChange = { texto = it },
                label = { Text("Buscar por título ou diretor") },
                colors = coresInputs,
                modifier = Modifier.fillMaxWidth()
            )

            TituloSecao("Gênero")
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                ChipGenero(GENEROS[0], generoAtivo, Modifier.weight(1f)) { generoAtivo = GENEROS[0] }
                ChipGenero(GENEROS[1], generoAtivo, Modifier.weight(1f)) { generoAtivo = GENEROS[1] }
                ChipGenero(GENEROS[2], generoAtivo, Modifier.weight(1f)) { generoAtivo = GENEROS[2] }
            }
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                ChipGenero(GENEROS[3], generoAtivo, Modifier.weight(1f)) { generoAtivo = GENEROS[3] }
                ChipGenero(GENEROS[4], generoAtivo, Modifier.weight(1f)) { generoAtivo = GENEROS[4] }
                ChipGenero(GENEROS[5], generoAtivo, Modifier.weight(1f)) { generoAtivo = GENEROS[5] }
            }
        }
        Separador(cor = Color.Black)

        Row(
            modifier = Modifier
                .background(Color.White)
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("RESULTADOS", color = Color.Black, fontSize = 13.sp, fontWeight = FontWeight.Bold)
            Text(
                text = if (resultados.size == 1) "1 filme" else "${resultados.size} filmes",
                color = Color.Gray,
                fontSize = 14.sp
            )
        }

        Column(
            modifier = Modifier
                .background(Color.White)
                .fillMaxSize()
                .padding(horizontal = 20.dp)
        ) {
            if (resultados.size == 0) {
                Text(
                    text = "Nenhum filme encontrado.",
                    color = Color.Gray,
                    fontSize = 15.sp,
                    modifier = Modifier.padding(vertical = 24.dp)
                )
            } else {
                resultados.forEach { filme ->
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
    generoAtivo: String,
    modifier: Modifier = Modifier,
    aoClicar: () -> Unit
) {
    val ativo = texto == generoAtivo
    Box(
        modifier = modifier
            .background(if (ativo) Color.Red else Color.LightGray)
            .clickable { aoClicar() }
            .padding(vertical = 10.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = texto,
            color = if (ativo) Color.White else Color.Black,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Preview(showBackground = true)
@Composable
fun TelaBuscarPreview() {
    ProjetoMobileTheme {
        TelaBuscar(aoAbrirFilme = {})
    }
}
