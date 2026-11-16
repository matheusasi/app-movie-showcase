package com.example.projetomobile.ui

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
fun TelaMomentoFormulario(
    momentoId: Long?,
    viewModel: MomentoViewModel,
    filmeViewModel: FilmeViewModel,
    voltar: () -> Unit
) {
    val camera = rememberLauncherForActivityResult(
        ActivityResultContracts.TakePicturePreview()
    ) { imagem -> viewModel.receberFoto(imagem) }

    var escolhendoFilme by remember { mutableStateOf(false) }

    LaunchedEffect(momentoId) {
        if (momentoId != null) viewModel.buscarPorId(momentoId)
    }
    LaunchedEffect(Unit) { filmeViewModel.listar() }
    LaunchedEffect(viewModel.salvo) {
        if (viewModel.salvo) {
            viewModel.consumirSalvo()
            voltar()
        }
    }

    val filme = filmeViewModel.filmes.firstOrNull { it.id == viewModel.filmeId }

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
                text = if (momentoId == null) "NOVO MOMENTO" else "ALTERAR MOMENTO",
                color = Color(0xFF1B1B1B),
                fontSize = 34.sp,
                fontWeight = FontWeight.Black
            )
            Text(
                text = viewModel.momento?.data ?: "Tire uma foto enquanto assiste ao filme.",
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

            if (momentoId == null || viewModel.momento != null) {
                viewModel.foto?.let { foto ->
                    AsyncImage(
                        model = foto,
                        contentDescription = "Foto do momento",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(300.dp),
                        contentScale = ContentScale.Fit
                    )
                }

                BotaoSecundario(
                    texto = if (viewModel.foto == null) "Tirar foto" else "Tirar outra foto"
                ) {
                    if (!viewModel.abrindo) {
                        viewModel.iniciarCamera()
                        try {
                            camera.launch(null)
                        } catch (_: Exception) {
                            viewModel.informarFalha()
                        }
                    }
                }

                TituloSecao(
                    texto = "Filme: ${filme?.titulo ?: "nenhum"}",
                    acao = if (escolhendoFilme) "Fechar" else "Escolher",
                    aoClicarAcao = { escolhendoFilme = !escolhendoFilme }
                )
                if (escolhendoFilme) {
                    filmeViewModel.filmes.forEach { opcao ->
                        TextButton(
                            onClick = {
                                viewModel.mudarFilme(opcao.id)
                                escolhendoFilme = false
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "${opcao.titulo} (${opcao.ano})",
                                color = if (opcao.id == viewModel.filmeId) Color(0xFFE8341C)
                                else Color(0xFF1B1B1B),
                                fontSize = 16.sp,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                }

                OutlinedTextField(
                    value = viewModel.legenda,
                    onValueChange = viewModel::mudarLegenda,
                    label = { Text("Legenda") },
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

                BotaoPrimario(texto = "Salvar momento") { viewModel.salvar() }
            } else if (!viewModel.carregando) {
                BotaoSecundario(texto = "Tentar carregar novamente") {
                    viewModel.buscarPorId(momentoId)
                }
            }

            BotaoSecundario(texto = "Voltar para momentos") {
                if (!viewModel.carregando) voltar()
            }
        }
    }
}
