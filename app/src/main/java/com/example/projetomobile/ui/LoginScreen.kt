package com.example.projetomobile.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.projetomobile.ui.viewmodel.AuthViewModel

@Composable
fun LoginScreen(
    irLogin: () -> Unit,
    irCadastro: () -> Unit,
    authViewModel: AuthViewModel
){
    var email by remember { mutableStateOf("") }
    var senha by remember { mutableStateOf("") }
    var senhaVisivel by remember { mutableStateOf(false) }

    LaunchedEffect(authViewModel.concluido) {
        if (authViewModel.concluido){
            authViewModel.consumirResultado()
            irLogin()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFFFFF))
            .padding(20.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = "SUA BIBLIOTECA",
            color = Color(0xFFE8341C),
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.sp
        )
        Text(
            text = "CINETECA",
            color = Color(0xFF1B1B1B),
            fontSize = 40.sp,
            fontWeight = FontWeight.Black
        )
        Text(
            text = "Entre para ver a sua lista de filmes.",
            color = Color(0xFF8C8C8C),
            fontSize = 15.sp
        )

        Spacer(Modifier.height(32.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { if(it.length <= 254) email = it },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            label = {Text("E-mail")},
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFF1B1B1B),
                unfocusedBorderColor = Color(0xFF1B1B1B),
                focusedLabelColor = Color(0xFFE8341C),
                cursorColor = Color(0xFFE8341C)
            ),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(16.dp))

        OutlinedTextField(
            value = senha,
            onValueChange = { if(it.length <= 128) senha = it },
            label = {Text("Senha")},
            singleLine = true,
            visualTransformation = if(senhaVisivel)
                VisualTransformation.None
                else PasswordVisualTransformation(),
            trailingIcon = {
                val image = if(senhaVisivel)
                    Icons.Default.Visibility
                    else Icons.Default.VisibilityOff
                val descricao = if(senhaVisivel) "Esconder Senha"
                    else "Mostrar Senha"
                IconButton(onClick = { senhaVisivel = !senhaVisivel}) {
                    Icon(
                        imageVector = image,
                        contentDescription = descricao,
                        tint = Color(0xFFE8341C)
                    )
                }
            },
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFF1B1B1B),
                unfocusedBorderColor = Color(0xFF1B1B1B),
                focusedLabelColor = Color(0xFFE8341C),
                cursorColor = Color(0xFFE8341C)
            ),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(16.dp))

        authViewModel.mensagem?.let {
            Text(
                text = it,
                color = Color(0xFFE8341C),
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold
            )
        }
        if (authViewModel.carregando) Text("Aguarde...", color = Color(0xFF8C8C8C), fontSize = 15.sp)

        Spacer(Modifier.height(16.dp))

        BotaoPrimario(texto = "Entrar") {
            authViewModel.login(email, senha)
            senha = ""
        }

        TextButton(onClick = irCadastro, enabled = !authViewModel.carregando) {
            Text(
                text = "NÃO TEM UMA CONTA? CADASTRE-SE",
                color = Color(0xFFE8341C),
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.5.sp
            )
        }
    }
}
