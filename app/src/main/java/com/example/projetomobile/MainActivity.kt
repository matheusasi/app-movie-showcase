package com.example.projetomobile

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.projetomobile.auth.SessaoUsuario
import com.example.projetomobile.navigation.AppNavigation
import com.example.projetomobile.navigation.Filmes
import com.example.projetomobile.navigation.Login
import com.example.projetomobile.navigation.Menu
import com.example.projetomobile.navigation.Usuarios
import com.example.projetomobile.ui.BarraTopo

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MaterialTheme {
                val navController = rememberNavController()

                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val rotaAtual = navBackStackEntry?.destination?.route

                // Login e Cadastro ficam sem as barras de navegacao.
                val mostrarMenu = rotaAtual != null &&
                        !rotaAtual.contains("Login") &&
                        !rotaAtual.contains("Cadastro")

                val titulo = when {
                    rotaAtual == null -> "Cineteca"
                    rotaAtual.contains("Lista") -> "Minha Lista"
                    rotaAtual.contains("Detalhe") -> "Detalhe"
                    rotaAtual.contains("Avaliar") -> "Avaliar"
                    rotaAtual.contains("Avaliac") -> "Avaliações"
                    rotaAtual.contains("Usuario") -> "Usuários"
                    rotaAtual.contains("Filme") -> "Filmes"
                    rotaAtual.contains("Buscar") -> "Buscar"
                    else -> "Cineteca"
                }

                Scaffold(
                    containerColor = Color(0xFFF3F1EF),
                    topBar = {
                        if (mostrarMenu) {
                            BarraTopo(
                                titulo = titulo,
                                irFilmes = { navController.navigate(Filmes) },
                                irUsuarios = { navController.navigate(Usuarios) },
                                sair = {
                                    SessaoUsuario.encerrar()
                                    navController.navigate(Login){
                                        popUpTo(0){inclusive = true}
                                    }
                                }
                            )
                        }
                    },
                    bottomBar = {
                        if (mostrarMenu) {
                            Menu(navController = navController, rotaAtual = rotaAtual)
                        }
                    }
                ) { innerPadding ->
                    AppNavigation(
                        navController = navController,
                        modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}
