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
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.projetomobile.data.LOCAIS
import com.example.projetomobile.data.formatarNota
import com.example.projetomobile.ui.viewmodel.AvaliacaoViewModel
import com.example.projetomobile.ui.viewmodel.FilmeViewModel

@Composable
fun TelaEditarAvaliacao(
    avaliacaoId: Long,
    viewModel: AvaliacaoViewModel,
    filmeViewModel: FilmeViewModel,
    voltar: () -> Unit
) {
    LaunchedEffect(avaliacaoId) { viewModel.buscarPorId(avaliacaoId) }
    LaunchedEffect(Unit) { filmeViewModel.listar() }
    LaunchedEffect(viewModel.salvo) {
        if (viewModel.salvo) {
            viewModel.consumirSalvo()
            voltar()
        }
    }

    if (viewModel.usuarioAtualId == null) {
        Text("Faça login para continuar.", color = Color(0xFF8C8C8C), fontSize = 15.sp)
        return
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
                text = "ALTERAR AVALIAÇÃO",
                color = Color(0xFF1B1B1B),
                fontSize = 30.sp,
                fontWeight = FontWeight.Black
            )
            Text(
                text = "O filme, a data e a nota não podem ser alterados.",
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
            verticalArrangement = Arrangement.spacedBy(18.dp)
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

            val avaliacao = viewModel.avaliacao
            if (avaliacao != null) {
                val filme = filmeViewModel.filmes.firstOrNull { it.id == avaliacao.filmeId }
                OutlinedTextField(
                    value = if (filme != null) "${filme.titulo} (${filme.ano})" else "",
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Filme") },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF1B1B1B),
                        unfocusedBorderColor = Color(0xFF1B1B1B),
                        focusedLabelColor = Color(0xFFE8341C)
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = avaliacao.data,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Assisti em") },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF1B1B1B),
                        unfocusedBorderColor = Color(0xFF1B1B1B),
                        focusedLabelColor = Color(0xFFE8341C)
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = formatarNota(avaliacao.nota),
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Nota") },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF1B1B1B),
                        unfocusedBorderColor = Color(0xFF1B1B1B),
                        focusedLabelColor = Color(0xFFE8341C)
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(text = "Onde você assistiu", color = Color(0xFF1B1B1B), fontSize = 15.sp)
                    LOCAIS.forEach { opcao ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .selectable(
                                    selected = viewModel.onde == opcao,
                                    onClick = { viewModel.mudarOnde(opcao) }
                                ),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = viewModel.onde == opcao,
                                onClick = { viewModel.mudarOnde(opcao) },
                                colors = RadioButtonDefaults.colors(
                                    selectedColor = Color(0xFFE8341C),
                                    unselectedColor = Color(0xFF1B1B1B)
                                )
                            )
                            Text(text = opcao, color = Color(0xFF1B1B1B), fontSize = 17.sp)
                        }
                    }
                }

                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(text = "Comentário", color = Color(0xFF1B1B1B), fontSize = 15.sp)
                    OutlinedTextField(
                        value = viewModel.comentario,
                        onValueChange = viewModel::mudarComentario,
                        placeholder = { Text("Escreva o que achou...") },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFF1B1B1B),
                            unfocusedBorderColor = Color(0xFF1B1B1B),
                            cursorColor = Color(0xFFE8341C)
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(120.dp)
                    )
                }

                BotaoPrimario(texto = "Salvar alterações") { viewModel.salvar() }
            } else if (!viewModel.carregando) {
                BotaoSecundario(texto = "Tentar carregar novamente") {
                    viewModel.buscarPorId(avaliacaoId)
                }
            }

            BotaoSecundario(texto = "Voltar para avaliações") {
                if (!viewModel.carregando) voltar()
            }
        }
    }
}
