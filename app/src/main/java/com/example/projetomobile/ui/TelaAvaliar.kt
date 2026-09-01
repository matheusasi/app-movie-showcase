// Tela 5 — Avaliar filme
// Responsável: integrante 2
package com.example.projetomobile.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.projetomobile.data.Avaliacao
import com.example.projetomobile.data.CATALOGO
import com.example.projetomobile.data.Filme
import com.example.projetomobile.data.LOCAIS
import com.example.projetomobile.data.formatarNota
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TelaAvaliar(
    filmeInicial: Filme?,
    aoSalvar: (Avaliacao) -> Unit
) {
    var filme by remember { mutableStateOf(filmeInicial ?: CATALOGO[0]) }
    var data by remember { mutableStateOf("") }
    var onde by remember { mutableStateOf(LOCAIS[0]) }
    var nota by remember { mutableFloatStateOf(8.5f) }
    var comentario by remember { mutableStateOf("") }

    var listaAberta by remember { mutableStateOf(false) }
    var calendarioAberto by remember { mutableStateOf(false) }
    var mensagem by remember { mutableStateOf("") }

    val estadoData = rememberDatePickerState()

    Column(
        modifier = Modifier
            .fillMaxSize()
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
                text = "AVALIAR FILME",
                color = Color(0xFF1B1B1B),
                fontSize = 34.sp,
                fontWeight = FontWeight.Black
            )
            Text(
                text = "Sua nota entra na média da biblioteca.",
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
            CampoClicavel(
                rotulo = "Filme",
                valor = "${filme.titulo} (${filme.ano})",
                aoClicar = { listaAberta = true }
            )

            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                CampoClicavel(
                    rotulo = "Assisti em",
                    valor = data,
                    espaco = "Toque para escolher a data",
                    aoClicar = { calendarioAberto = true }
                )
            }

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(text = "Onde você assistiu", color = Color(0xFF1B1B1B), fontSize = 15.sp)
                LOCAIS.forEach { opcao ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .selectable(
                                selected = onde == opcao,
                                onClick = { onde = opcao }
                            ),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = onde == opcao,
                            onClick = { onde = opcao },
                            colors = RadioButtonDefaults.colors(
                                selectedColor = Color(0xFFE8341C),
                                unselectedColor = Color(0xFF1B1B1B)
                            )
                        )
                        Text(text = opcao, color = Color(0xFF1B1B1B), fontSize = 17.sp)
                    }
                }
            }

            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Bottom
                ) {
                    Text(text = "Nota", color = Color(0xFF1B1B1B), fontSize = 15.sp)
                    Text(
                        text = formatarNota(nota),
                        color = Color(0xFFE8341C),
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Black
                    )
                }
                Slider(
                    value = nota,
                    onValueChange = { nota = it },
                    valueRange = 0f..10f,
                    steps = 19,
                    colors = SliderDefaults.colors(
                        thumbColor = Color(0xFFE8341C),
                        activeTrackColor = Color(0xFFE8341C),
                        inactiveTrackColor = Color(0xFFDDD9D6),
                        activeTickColor = Color(0xFFE8341C),
                        inactiveTickColor = Color(0xFFDDD9D6)
                    )
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "0", color = Color(0xFF8C8C8C), fontSize = 13.sp)
                    Text(text = "10", color = Color(0xFF8C8C8C), fontSize = 13.sp)
                }
            }

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(text = "Comentário", color = Color(0xFF1B1B1B), fontSize = 15.sp)
                OutlinedTextField(
                    value = comentario,
                    onValueChange = { comentario = it },
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

            if (mensagem.isNotEmpty()) {
                Text(
                    text = mensagem,
                    color = Color(0xFFE8341C),
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            BotaoPrimario(texto = "Salvar avaliação") {
                if (data.isBlank()) {
                    mensagem = "Escolha a data em que você assistiu."
                } else {
                    aoSalvar(Avaliacao(filme.id, data, onde, nota, comentario))
                    mensagem = "Avaliação de ${filme.titulo} salva com nota ${formatarNota(nota)}."
                    comentario = ""
                }
            }
        }
    }

    if (listaAberta) {
        AlertDialog(
            onDismissRequest = { listaAberta = false },
            title = { Text("Escolha o filme", fontWeight = FontWeight.Bold) },
            text = {
                LazyColumn(modifier = Modifier.heightIn(max = 360.dp)) {
                    items(CATALOGO, key = { it.id }) { opcao ->
                        Text(
                            text = "${opcao.titulo} (${opcao.ano})",
                            color = if (opcao.id == filme.id) Color(0xFFE8341C) else Color(0xFF1B1B1B),
                            fontSize = 16.sp,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    filme = opcao
                                    listaAberta = false
                                }
                                .padding(vertical = 12.dp)
                        )
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { listaAberta = false }) {
                    Text("Fechar", color = Color(0xFFE8341C))
                }
            }
        )
    }

    if (calendarioAberto) {
        DatePickerDialog(
            onDismissRequest = { calendarioAberto = false },
            confirmButton = {
                TextButton(onClick = {
                    val millis = estadoData.selectedDateMillis
                    if (millis != null) {
                        data = formatarData(millis)
                        mensagem = ""
                    }
                    calendarioAberto = false
                }) {
                    Text("OK", color = Color(0xFFE8341C))
                }
            },
            dismissButton = {
                TextButton(onClick = { calendarioAberto = false }) {
                    Text("Cancelar", color = Color(0xFF1B1B1B))
                }
            }
        ) {
            DatePicker(state = estadoData)
        }
    }
}

@Composable
private fun CampoClicavel(
    rotulo: String,
    valor: String,
    espaco: String = "",
    aoClicar: () -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(text = rotulo, color = Color(0xFF1B1B1B), fontSize = 15.sp)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(if (valor.isBlank()) Color(0xFFF3F1EF) else Color(0xFFFFFFFF))
                .clickable { aoClicar() }
        ) {
            OutlinedTextField(
                value = valor,
                onValueChange = {},
                readOnly = true,
                enabled = false,
                placeholder = { Text(espaco, color = Color(0xFF8C8C8C)) },
                trailingIcon = {
                    Text(
                        text = "▾",
                        color = Color(0xFFE8341C),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                },
                colors = OutlinedTextFieldDefaults.colors(
                    disabledBorderColor = Color(0xFF1B1B1B),
                    disabledTextColor = Color(0xFF1B1B1B),
                    disabledPlaceholderColor = Color(0xFF8C8C8C),
                    disabledTrailingIconColor = Color(0xFFE8341C)
                ),
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

private fun formatarData(millis: Long): String {
    val formato = SimpleDateFormat("dd/MM/yyyy", Locale.forLanguageTag("pt-BR"))
    formato.timeZone = TimeZone.getTimeZone("UTC")
    return formato.format(java.util.Date(millis))
}
