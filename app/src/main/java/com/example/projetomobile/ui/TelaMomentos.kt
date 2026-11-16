package com.example.projetomobile.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.example.projetomobile.ui.viewmodel.FilmeViewModel
import com.example.projetomobile.ui.viewmodel.MomentoViewModel

@Composable
fun TelaMomentos(
    viewModel: MomentoViewModel,
    filmeViewModel: FilmeViewModel,
    irParaCadastro: () -> Unit,
    irParaEdicao: (Long) -> Unit
) {
    LaunchedEffect(Unit) { filmeViewModel.listar() }

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
                    text = "MOMENTOS",
                    color = Color(0xFF1B1B1B),
                    fontSize = 34.sp,
                    fontWeight = FontWeight.Black
                )
                Text(
                    text = "Registre com uma foto o momento em que você está assistindo a um filme.",
                    color = Color(0xFF8C8C8C),
                    fontSize = 15.sp
                )
                BotaoPrimario(texto = "Novo momento") {
                    if (!viewModel.carregando) irParaCadastro()
                }
                viewModel.mensagem?.let {
                    Text(
                        text = it,
                        color = Color(0xFFE8341C),
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                if (viewModel.carregando) CircularProgressIndicator(color = Color(0xFFE8341C))
                if (!viewModel.carregando && viewModel.momentos.isEmpty()) {
                    Text(
                        text = "Nenhum momento registrado.",
                        color = Color(0xFF8C8C8C),
                        fontSize = 15.sp
                    )
                }
            }
            Separador(cor = Color(0xFF1B1B1B))
        }
        items(viewModel.momentos, key = { it.id }) { momento ->
            val filme = filmeViewModel.filmes.firstOrNull { it.id == momento.filmeId }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 12.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                AsyncImage(
                    model = momento.foto,
                    contentDescription = "Foto do momento",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(220.dp),
                    contentScale = ContentScale.Crop
                )
                Text(
                    text = (filme?.titulo ?: "Filme removido").uppercase(),
                    color = Color(0xFF1B1B1B),
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Black
                )
                Text(
                    text = momento.data,
                    color = Color(0xFFE8341C),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold
                )
                if (momento.legenda.isNotBlank()) {
                    Text(
                        text = momento.legenda,
                        color = Color(0xFF1B1B1B),
                        fontSize = 15.sp
                    )
                }
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    TextButton(
                        onClick = { irParaEdicao(momento.id) },
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
                        onClick = { excluirId = momento.id },
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

    val selecionado = viewModel.momentos.firstOrNull { it.id == excluirId }
    if (selecionado != null) {
        AlertDialog(
            onDismissRequest = { excluirId = null },
            title = { Text("Excluir momento?", fontWeight = FontWeight.Bold) },
            text = { Text("A foto será apagada. Esta ação não pode ser desfeita.") },
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
