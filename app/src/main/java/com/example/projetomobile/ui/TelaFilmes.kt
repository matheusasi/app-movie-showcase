package com.example.projetomobile.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.projetomobile.ui.viewmodel.FilmeViewModel

@Composable
fun TelaFilmes(
    viewModel: FilmeViewModel,
    irParaCadastro: () -> Unit,
    irParaEdicao: (Long) -> Unit
) {
    var excluirId by rememberSaveable { mutableStateOf<Long?>(null) }

    LazyColumn(
        modifier = Modifier
            .background(Color(0xFFFFFFFF))
            .fillMaxSize()
    ) {
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 18.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "FILMES",
                    color = Color(0xFF1B1B1B),
                    fontSize = 34.sp,
                    fontWeight = FontWeight.Black
                )
                Text(
                    text = if (viewModel.filmes.size == 1) "1 filme no catálogo"
                    else "${viewModel.filmes.size} filmes no catálogo",
                    color = Color(0xFF8C8C8C),
                    fontSize = 15.sp
                )
                BotaoPrimario(texto = "Novo filme") {
                    if (!viewModel.carregando) irParaCadastro()
                }
                BotaoSecundario(texto = "Atualizar lista") { viewModel.listar() }
                viewModel.mensagem?.let {
                    Text(
                        text = it,
                        color = Color(0xFFE8341C),
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                if (viewModel.carregando) CircularProgressIndicator(color = Color(0xFFE8341C))
                if (!viewModel.carregando && viewModel.filmes.isEmpty()) {
                    Text(
                        text = "Nenhum filme cadastrado.",
                        color = Color(0xFF8C8C8C),
                        fontSize = 15.sp
                    )
                }
            }
            Separador(cor = Color(0xFF1B1B1B))
        }
        items(viewModel.filmes, key = { it.id }) { filme ->
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 12.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = filme.titulo.uppercase(),
                    color = Color(0xFF1B1B1B),
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Black,
                    lineHeight = 21.sp
                )
                Text(
                    text = "${filme.ano} · ${filme.genero} · ${filme.duracaoMin} min",
                    color = Color(0xFF8C8C8C),
                    fontSize = 14.sp
                )
                Text(
                    text = filme.diretor,
                    color = Color(0xFF8C8C8C),
                    fontSize = 14.sp
                )
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    TextButton(
                        onClick = { irParaEdicao(filme.id) },
                        enabled = !viewModel.carregando
                    ) {
                        Text(
                            text = "ALTERAR",
                            color = Color(0xFFE8341C),
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    TextButton(
                        onClick = { excluirId = filme.id },
                        enabled = !viewModel.carregando
                    ) {
                        Text(
                            text = "EXCLUIR",
                            color = Color(0xFF1B1B1B),
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
            Separador()
        }
    }

    val selecionado = viewModel.filmes.firstOrNull { it.id == excluirId }
    if (selecionado != null) {
        AlertDialog(
            onDismissRequest = { excluirId = null },
            title = { Text("Excluir filme?", fontWeight = FontWeight.Bold) },
            text = { Text("Excluir ${selecionado.titulo}? As avaliações deste filme também serão apagadas.") },
            confirmButton = {
                TextButton(
                    onClick = {
                        excluirId = null
                        viewModel.excluir(selecionado.id)
                    },
                    enabled = !viewModel.carregando
                ) { Text("Excluir", color = Color(0xFFE8341C)) }
            },
            dismissButton = {
                TextButton(onClick = { excluirId = null }) {
                    Text("Cancelar", color = Color(0xFF1B1B1B))
                }
            }
        )
    }
}
