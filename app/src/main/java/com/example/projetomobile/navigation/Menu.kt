package com.example.projetomobile.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController

@Composable
fun Menu(
    navController: NavHostController,
    rotaAtual: String?
){
    Column(modifier = Modifier.background(Color(0xFFFFFFFF))) {
        HorizontalDivider(thickness = 2.dp, color = Color(0xFF1B1B1B))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
        ) {
            ItemBarra(
                rotulo = "Início",
                // Detalhe e Avaliar sao abertos a partir do Inicio, entao a aba fica acesa.
                ativa = rotaAtual != null && (rotaAtual.contains("Inicio") ||
                        rotaAtual.contains("Detalhe") || rotaAtual.contains("Avaliar")),
                modifier = Modifier.weight(1f),
                aoTrocar = { navController.navigate(Inicio) }
            )
            ItemBarra(
                rotulo = "Buscar",
                ativa = rotaAtual != null && rotaAtual.contains("Buscar"),
                modifier = Modifier.weight(1f),
                aoTrocar = { navController.navigate(Buscar) }
            )
            ItemBarra(
                rotulo = "Lista",
                ativa = rotaAtual != null && rotaAtual.contains("Lista"),
                modifier = Modifier.weight(1f),
                aoTrocar = { navController.navigate(Lista) }
            )
        }
    }
}

@Composable
private fun ItemBarra(
    rotulo: String,
    ativa: Boolean,
    modifier: Modifier,
    aoTrocar: () -> Unit
) {
    Box(
        modifier = modifier
            .background(if (ativa) Color(0xFFE8341C) else Color(0xFFFFFFFF))
            .clickable { aoTrocar() }
            .height(56.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = rotulo.uppercase(),
            color = if (ativa) Color(0xFFFFFFFF) else Color(0xFF1B1B1B),
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 0.5.sp
        )
    }
}
