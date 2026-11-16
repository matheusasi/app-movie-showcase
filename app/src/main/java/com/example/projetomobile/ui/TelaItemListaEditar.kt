package com.example.projetomobile.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.projetomobile.data.ASSISTIDO
import com.example.projetomobile.data.QUERO_VER
import com.example.projetomobile.ui.viewmodel.FilmeViewModel
import com.example.projetomobile.ui.viewmodel.MinhaListaViewModel

@Composable
fun TelaItemListaEditar(
    itemId: Long,
    viewModel: MinhaListaViewModel,
    filmeViewModel: FilmeViewModel,
    voltar: () -> Unit
) {
    LaunchedEffect(itemId) { viewModel.buscarPorId(itemId) }
    LaunchedEffect(Unit) { filmeViewModel.listar() }
    LaunchedEffect(viewModel.salvo) {
        if (viewModel.salvo) {
            viewModel.consumirSalvo()
            voltar()
        }
    }

    val item = viewModel.item
    val filme = filmeViewModel.filmes.firstOrNull { it.id == item?.filmeId }

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
                text = "ALTERAR ITEM",
                color = Color(0xFF1B1B1B),
                fontSize = 34.sp,
                fontWeight = FontWeight.Black
            )
            Text(
                text = filme?.titulo ?: "",
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

            if (item != null) {
                Text(text = "Situação", color = Color(0xFF1B1B1B), fontSize = 15.sp)
                if (viewModel.status == QUERO_VER) {
                    BotaoPrimario(texto = QUERO_VER) { viewModel.mudarStatus(QUERO_VER) }
                    BotaoSecundario(texto = ASSISTIDO) { viewModel.mudarStatus(ASSISTIDO) }
                } else {
                    BotaoSecundario(texto = QUERO_VER) { viewModel.mudarStatus(QUERO_VER) }
                    BotaoPrimario(texto = ASSISTIDO) { viewModel.mudarStatus(ASSISTIDO) }
                }

                OutlinedTextField(
                    value = viewModel.observacao,
                    onValueChange = viewModel::mudarObservacao,
                    label = { Text("Observação") },
                    enabled = !viewModel.carregando,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF1B1B1B),
                        unfocusedBorderColor = Color(0xFF1B1B1B),
                        focusedLabelColor = Color(0xFFE8341C),
                        cursorColor = Color(0xFFE8341C)
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp)
                )

                BotaoPrimario(texto = "Salvar") { viewModel.salvar() }
            } else if (!viewModel.carregando) {
                BotaoSecundario(texto = "Tentar carregar novamente") {
                    viewModel.buscarPorId(itemId)
                }
            }

            BotaoSecundario(texto = "Voltar para minha lista") {
                if (!viewModel.carregando) voltar()
            }
        }
    }
}
