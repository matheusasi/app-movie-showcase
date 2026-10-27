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
import com.example.projetomobile.ui.viewmodel.FilmeViewModel

@Composable
fun TelaFilmeFormulario(
    filmeId: Long?,
    viewModel: FilmeViewModel,
    voltar: () -> Unit
) {
    LaunchedEffect(filmeId) {
        if (filmeId != null) viewModel.buscarPorId(filmeId)
    }
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
                text = if (filmeId == null) "NOVO FILME" else "ALTERAR FILME",
                color = Color(0xFF1B1B1B),
                fontSize = 34.sp,
                fontWeight = FontWeight.Black
            )
            Text(
                text = "O filme aparece no início, na busca e pode ser avaliado.",
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

            if (filmeId == null || viewModel.filme != null) {
                CampoFilme(rotulo = "Título", valor = viewModel.titulo, aoMudar = viewModel::mudarTitulo)
                CampoFilme(rotulo = "Ano", valor = viewModel.ano, aoMudar = viewModel::mudarAno)
                CampoFilme(rotulo = "Gênero", valor = viewModel.genero, aoMudar = viewModel::mudarGenero)
                CampoFilme(rotulo = "Duração (min)", valor = viewModel.duracao, aoMudar = viewModel::mudarDuracao)
                CampoFilme(rotulo = "Diretor", valor = viewModel.diretor, aoMudar = viewModel::mudarDiretor)

                OutlinedTextField(
                    value = viewModel.sinopse,
                    onValueChange = viewModel::mudarSinopse,
                    label = { Text("Sinopse") },
                    enabled = !viewModel.carregando,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF1B1B1B),
                        unfocusedBorderColor = Color(0xFF1B1B1B),
                        focusedLabelColor = Color(0xFFE8341C),
                        cursorColor = Color(0xFFE8341C)
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(140.dp)
                )

                BotaoPrimario(texto = "Salvar filme") { viewModel.salvar() }
            } else if (!viewModel.carregando) {
                BotaoSecundario(texto = "Tentar carregar novamente") {
                    viewModel.buscarPorId(filmeId)
                }
            }

            BotaoSecundario(texto = "Voltar para filmes") {
                if (!viewModel.carregando) voltar()
            }
        }
    }
}

@Composable
private fun CampoFilme(
    rotulo: String,
    valor: String,
    aoMudar: (String) -> Unit
) {
    OutlinedTextField(
        value = valor,
        onValueChange = aoMudar,
        label = { Text(rotulo) },
        singleLine = true,
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Color(0xFF1B1B1B),
            unfocusedBorderColor = Color(0xFF1B1B1B),
            focusedLabelColor = Color(0xFFE8341C),
            cursorColor = Color(0xFFE8341C)
        ),
        modifier = Modifier.fillMaxWidth()
    )
}
