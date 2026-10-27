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
import com.example.projetomobile.ui.viewmodel.UsuarioViewModel

@Composable
fun UsuarioScreen(
    viewModel: UsuarioViewModel,
    irParaCadastro: () -> Unit,
    irParaEdicao: (Long) -> Unit,
    voltar: () -> Unit
) {
    val atual = viewModel.usuarioAtualId
    if (atual == null) {
        Text("Faça login para continuar", color = Color(0xFF8C8C8C), fontSize = 15.sp)
        return
    }

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
                    text = "USUÁRIOS",
                    color = Color(0xFF1B1B1B),
                    fontSize = 34.sp,
                    fontWeight = FontWeight.Black
                )
                Text(
                    text = if (viewModel.usuarios.size == 1) "1 usuário cadastrado neste aparelho"
                    else "${viewModel.usuarios.size} usuários cadastrados neste aparelho",
                    color = Color(0xFF8C8C8C),
                    fontSize = 15.sp
                )
                BotaoPrimario(texto = "Novo usuário") {
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
                if (!viewModel.carregando && viewModel.usuarios.isEmpty()) {
                    Text(
                        text = "Nenhum usuário encontrado.",
                        color = Color(0xFF8C8C8C),
                        fontSize = 15.sp
                    )
                }
            }
            Separador(cor = Color(0xFF1B1B1B))
        }
        items(viewModel.usuarios, key = { it.id }) { usuario ->
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 12.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = usuario.nome.uppercase(),
                    color = Color(0xFF1B1B1B),
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Black
                )
                Text(
                    text = usuario.email,
                    color = Color(0xFF8C8C8C),
                    fontSize = 14.sp
                )
                if (usuario.id == atual) {
                    Text(
                        text = "VOCÊ ESTÁ CONECTADO",
                        color = Color(0xFFE8341C),
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    TextButton(
                        onClick = { irParaEdicao(usuario.id) },
                        enabled = !viewModel.carregando
                    ) {
                        Text(
                            text = "ALTERAR",
                            color = Color(0xFFE8341C),
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    if (usuario.id != atual) {
                        TextButton(
                            onClick = { excluirId = usuario.id },
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
            }
            Separador()
        }
        item {
            Column(modifier = Modifier.padding(20.dp)) {
                BotaoSecundario(texto = "Voltar", aoClicar = voltar)
            }
        }
    }

    val selecionado = viewModel.usuarios.firstOrNull { it.id == excluirId }
    if (selecionado != null && selecionado.id != atual) {
        AlertDialog(
            onDismissRequest = { excluirId = null },
            title = { Text("Excluir usuário?", fontWeight = FontWeight.Bold) },
            text = { Text("Excluir ${selecionado.nome}? Esta ação não pode ser desfeita.") },
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
