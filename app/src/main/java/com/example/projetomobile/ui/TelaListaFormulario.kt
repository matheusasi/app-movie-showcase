package com.example.projetomobile.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.projetomobile.ui.viewmodel.FilmeViewModel
import com.example.projetomobile.ui.viewmodel.ListaViewModel

@Composable
fun TelaListaFormulario(
    listaId: Long?,
    viewModel: ListaViewModel,
    filmeViewModel: FilmeViewModel,
    voltar: () -> Unit
) {
    LaunchedEffect(listaId) {
        if (listaId != null) viewModel.buscarPorId(listaId)
    }
    LaunchedEffect(Unit) { filmeViewModel.listar() }
    LaunchedEffect(viewModel.salvo) {
        if (viewModel.salvo) {
            viewModel.consumirSalvo()
            voltar()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .imePadding()
            .verticalScroll(rememberScrollState())
    ) {
        Column(
            modifier = Modifier
                .background(Color(0xFFFFFFFF))
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 18.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = if (listaId == null) "NOVA LISTA" else "ALTERAR LISTA",
                color = Color(0xFF1B1B1B),
                fontSize = 34.sp,
                fontWeight = FontWeight.Black
            )
            Text(
                text = "${viewModel.filmesSelecionados.size} filmes escolhidos",
                color = Color(0xFF8C8C8C),
                fontSize = 15.sp
            )
        }
        Separador(cor = Color(0xFF1B1B1B))

        Column(
            modifier = Modifier
                .background(Color(0xFFFFFFFF))
                .fillMaxWidth()
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            viewModel.mensagem?.let {
                Text(
                    text = it,
                    color = Color(0xFFE8341C),
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            if (viewModel.carregando) CircularProgressIndicator(color = Color(0xFFE8341C))

            if (listaId == null || viewModel.lista != null) {
                OutlinedTextField(
                    value = viewModel.nome,
                    onValueChange = viewModel::mudarNome,
                    label = { Text("Nome da lista") },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF1B1B1B),
                        unfocusedBorderColor = Color(0xFF1B1B1B),
                        focusedLabelColor = Color(0xFFE8341C),
                        cursorColor = Color(0xFFE8341C)
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = viewModel.descricao,
                    onValueChange = viewModel::mudarDescricao,
                    label = { Text("Descrição") },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF1B1B1B),
                        unfocusedBorderColor = Color(0xFF1B1B1B),
                        focusedLabelColor = Color(0xFFE8341C),
                        cursorColor = Color(0xFFE8341C)
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(110.dp)
                )

                TituloSecao("Filmes da lista")
                filmeViewModel.filmes.forEach { filme ->
                    val escolhido = viewModel.filmesSelecionados.contains(filme.id)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "${filme.titulo} (${filme.ano})",
                            color = if (escolhido) Color(0xFFE8341C) else Color(0xFF1B1B1B),
                            fontSize = 16.sp,
                            fontWeight = if (escolhido) FontWeight.Bold else FontWeight.Normal,
                            modifier = Modifier.weight(1f)
                        )
                        TextButton(onClick = { viewModel.alternarFilme(filme.id) }) {
                            Text(
                                text = if (escolhido) "REMOVER" else "ADICIONAR",
                                color = if (escolhido) Color(0xFF1B1B1B) else Color(0xFFE8341C),
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                    Separador()
                }

                BotaoPrimario(texto = "Salvar lista") { viewModel.salvar() }
            } else if (!viewModel.carregando) {
                BotaoSecundario(texto = "Tentar carregar novamente") {
                    viewModel.buscarPorId(listaId)
                }
            }

            BotaoSecundario(texto = "Voltar para listas") {
                if (!viewModel.carregando) voltar()
            }
        }
    }
}
