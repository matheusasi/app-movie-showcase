package com.example.projetomobile.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.projetomobile.ui.viewmodel.UsuarioViewModel

@Composable
fun UsuarioEditarScreen(
    usuarioId: Long,
    viewModel: UsuarioViewModel,
    voltar: () -> Unit
) {
    LaunchedEffect(usuarioId) { viewModel.buscarPorId(usuarioId) }
    LaunchedEffect(viewModel.salvo) {
        if (viewModel.salvo) {
            viewModel.consumirSalvo()
            voltar()
        }
    }
    var novaSenha by remember { mutableStateOf("") }

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
                text = "ALTERAR USUÁRIO",
                color = Color(0xFF1B1B1B),
                fontSize = 30.sp,
                fontWeight = FontWeight.Black
            )
            Text(
                text = "O e-mail não pode ser alterado.",
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

            val usuario = viewModel.usuario
            if (usuario != null) {
                OutlinedTextField(
                    value = viewModel.nome,
                    onValueChange = viewModel::mudarNome,
                    label = { Text("Nome") },
                    singleLine = true,
                    enabled = !viewModel.carregando,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF1B1B1B),
                        unfocusedBorderColor = Color(0xFF1B1B1B),
                        focusedLabelColor = Color(0xFFE8341C),
                        cursorColor = Color(0xFFE8341C)
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = usuario.email,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("E-mail") },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF1B1B1B),
                        unfocusedBorderColor = Color(0xFF1B1B1B),
                        focusedLabelColor = Color(0xFFE8341C)
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = novaSenha,
                    onValueChange = { if (it.length <= 128) novaSenha = it },
                    label = { Text("Nova senha") },
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password,
                        autoCorrectEnabled = false
                    ),
                    singleLine = true,
                    enabled = !viewModel.carregando,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF1B1B1B),
                        unfocusedBorderColor = Color(0xFF1B1B1B),
                        focusedLabelColor = Color(0xFFE8341C),
                        cursorColor = Color(0xFFE8341C)
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
                Text(
                    text = "Deixe em branco para manter a senha atual ou digite entre 6 e 128 caracteres.",
                    color = Color(0xFF8C8C8C),
                    fontSize = 14.sp
                )
                BotaoPrimario(texto = "Salvar") {
                    if (!viewModel.carregando && !viewModel.salvo) {
                        viewModel.salvar(novaSenha)
                        novaSenha = ""
                    }
                }
            } else if (!viewModel.carregando) {
                BotaoSecundario(texto = "Tentar carregar novamente") {
                    viewModel.buscarPorId(usuarioId)
                }
            }

            BotaoSecundario(texto = "Voltar para usuários") {
                if (!viewModel.carregando) voltar()
            }
        }
    }
}
