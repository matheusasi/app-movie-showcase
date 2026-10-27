package com.example.projetomobile.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.projetomobile.data.Filme
import com.example.projetomobile.data.formatarNota

@Composable
fun BarraTopo(
    titulo: String,
    irFilmes: () -> Unit,
    irUsuarios: () -> Unit,
    sair: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF1B1B1B))
            .statusBarsPadding()
            .height(52.dp)
            .padding(start = 20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = titulo.uppercase(),
            color = Color(0xFFFFFFFF),
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.sp,
            modifier = Modifier.weight(1f)
        )
        TextButton(onClick = irFilmes) {
            Text(
                text = "FILMES",
                color = Color(0xFFFFFFFF),
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.5.sp
            )
        }
        TextButton(onClick = irUsuarios) {
            Text(
                text = "USUÁRIOS",
                color = Color(0xFFFFFFFF),
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.5.sp
            )
        }
        IconButton(onClick = sair) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                contentDescription = "Sair",
                tint = Color(0xFFE8341C)
            )
        }
    }
}

@Composable
fun Poster(modifier: Modifier = Modifier) {
    Box(modifier = modifier.background(Color(0xFFBDB9B6)))
}

@Composable
fun TituloSecao(
    texto: String,
    modifier: Modifier = Modifier,
    acao: String? = null,
    aoClicarAcao: () -> Unit = {}
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = texto.uppercase(),
            color = Color(0xFF1B1B1B),
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.sp
        )
        if (acao != null) {
            Text(
                text = acao.uppercase(),
                color = Color(0xFFE8341C),
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.5.sp,
                modifier = Modifier.clickable { aoClicarAcao() }
            )
        }
    }
}

@Composable
fun ItemFilme(
    filme: Filme,
    subtitulo: String,
    modifier: Modifier = Modifier,
    mostrarNota: Boolean = true,
    acao: String? = null,
    aoClicarAcao: () -> Unit = {},
    aoClicar: () -> Unit = {}
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable { aoClicar() }
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Poster(
            modifier = Modifier
                .width(56.dp)
                .height(80.dp)
        )
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 14.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = filme.titulo.uppercase(),
                color = Color(0xFF1B1B1B),
                fontSize = 17.sp,
                fontWeight = FontWeight.Black,
                lineHeight = 21.sp
            )
            Text(
                text = subtitulo,
                color = Color(0xFF8C8C8C),
                fontSize = 14.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            if (mostrarNota) {
                Text(
                    text = "NOTA ${formatarNota(filme.nota)}",
                    color = Color(0xFFE8341C),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
        if (acao != null) {
            Text(
                text = acao.uppercase(),
                color = Color(0xFFE8341C),
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.End,
                modifier = Modifier.clickable { aoClicarAcao() }
            )
        }
    }
}

@Composable
fun BotaoPrimario(texto: String, modifier: Modifier = Modifier, aoClicar: () -> Unit) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(Color(0xFFE8341C))
            .clickable { aoClicar() }
            .padding(vertical = 18.dp, horizontal = 20.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Text(
            text = texto.uppercase(),
            color = Color(0xFFFFFFFF),
            fontSize = 16.sp,
            fontWeight = FontWeight.Black,
            letterSpacing = 0.5.sp
        )
    }
}

@Composable
fun BotaoSecundario(texto: String, modifier: Modifier = Modifier, aoClicar: () -> Unit) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(Color(0xFFFFFFFF))
            .border(2.dp, Color(0xFF1B1B1B))
            .clickable { aoClicar() }
            .padding(vertical = 18.dp, horizontal = 20.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Text(
            text = texto.uppercase(),
            color = Color(0xFF1B1B1B),
            fontSize = 16.sp,
            fontWeight = FontWeight.Black,
            letterSpacing = 0.5.sp
        )
    }
}

@Composable
fun Separador(modifier: Modifier = Modifier, cor: Color = Color(0xFFDDD9D6)) {
    HorizontalDivider(modifier = modifier, thickness = 1.dp, color = cor)
}

@Composable
fun Badge(texto: String, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .background(Color(0xFFE8341C))
            .size(width = 72.dp, height = 52.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = texto,
            color = Color(0xFFFFFFFF),
            fontSize = 26.sp,
            fontWeight = FontWeight.Black
        )
    }
}
