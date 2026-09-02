// Tela 3 — Avaliar filme (data, local, nota e comentário)
// Responsável: Matheus Henrique Farias de Jesus
package com.example.projetomobile.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Slider
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.projetomobile.data.Avaliacao
import com.example.projetomobile.data.CATALOGO
import com.example.projetomobile.data.Filme
import com.example.projetomobile.data.LOCAIS
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import com.example.projetomobile.ui.theme.ProjetoMobileTheme

// @OptIn é obrigatório para usar o DatePicker (o mesmo componente do formulário da aula myapplication).
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TelaAvaliar(
    filmeInicial: Filme,
    modifier: Modifier = Modifier
) {
    var data by remember { mutableStateOf("Clique para selecionar") }
    var mostrarCalendario by remember { mutableStateOf(false) }
    var onde by remember { mutableStateOf(LOCAIS[0]) }
    var nota by remember { mutableFloatStateOf(8f) }
    var comentario by remember { mutableStateOf("") }
    var mensagem by remember { mutableStateOf("") }

    val coresInputs = OutlinedTextFieldDefaults.colors(
        focusedBorderColor = Color.Black,
        unfocusedBorderColor = Color.Black,
        disabledBorderColor = Color.Black
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(horizontal = 20.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text("AVALIAR FILME", color = Color.Black, fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Text(
            text = "Filme: ${filmeInicial.titulo} (${filmeInicial.ano})",
            color = Color.Gray,
            fontSize = 15.sp
        )

        OutlinedTextField(
            value = data,
            onValueChange = { },
            label = { Text("Assisti em") },
            readOnly = true,
            enabled = false,
            colors = coresInputs,
            modifier = Modifier
                .fillMaxWidth()
                .clickable { mostrarCalendario = true }
        )

        if (mostrarCalendario) {
            val datePicker = rememberDatePickerState()
            DatePickerDialog(
                onDismissRequest = { mostrarCalendario = false },
                confirmButton = {
                    TextButton(onClick = {
                        val escolhida = datePicker.selectedDateMillis?.let {
                            SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date(it))
                        } ?: "Nenhuma data"
                        data = escolhida
                        mensagem = ""
                        mostrarCalendario = false
                    }) { Text("Confirmar") }
                },
                dismissButton = {
                    TextButton(onClick = { mostrarCalendario = false }) { Text("Cancelar") }
                }
            ) {
                DatePicker(state = datePicker)
            }
        }

        Column(modifier = Modifier.fillMaxWidth()) {
            Text("Onde você assistiu", fontWeight = FontWeight.Bold, color = Color.Black)
            LOCAIS.forEach { opcao ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onde = opcao }
                ) {
                    RadioButton(
                        selected = (opcao == onde),
                        onClick = { onde = opcao }
                    )
                    Text(text = opcao, modifier = Modifier.padding(start = 8.dp), color = Color.Black)
                }
            }
        }

        Column(modifier = Modifier.fillMaxWidth()) {
            Text("Nota (0 a 10): ${nota.toInt()}", fontWeight = FontWeight.Bold, color = Color.Black)
            Slider(
                value = nota,
                onValueChange = { nota = it },
                valueRange = 0f..10f,
                steps = 9
            )
        }

        OutlinedTextField(
            value = comentario,
            onValueChange = { comentario = it },
            label = { Text("Comentário") },
            colors = coresInputs,
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp)
        )

        if (mensagem != "") {
            Text(text = mensagem, color = Color.Red, fontSize = 15.sp, fontWeight = FontWeight.Bold)
        }

        BotaoPrimario(texto = "Salvar avaliação") {
            if (data == "Clique para selecionar") {
                mensagem = "Escolha a data em que você assistiu."
            } else {
                val av = Avaliacao(filmeInicial.id, data, onde, nota, comentario)
                mensagem = "Avaliação de ${filmeInicial.titulo} salva: nota ${av.nota.toInt()}, " +
                    "assistido em ${av.data} (${av.onde})."
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TelaAvaliarPreview() {
    ProjetoMobileTheme {
        TelaAvaliar(filmeInicial = CATALOGO[0])
    }
}
