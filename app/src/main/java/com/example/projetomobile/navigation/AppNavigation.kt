package com.example.projetomobile.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.example.projetomobile.ui.CadastroScreen
import com.example.projetomobile.ui.LoginScreen
import com.example.projetomobile.ui.TelaAvaliar
import com.example.projetomobile.ui.TelaBuscar
import com.example.projetomobile.ui.TelaDetalhe
import com.example.projetomobile.ui.TelaInicio
import com.example.projetomobile.ui.TelaLista

@Composable
fun AppNavigation(navController: NavHostController, modifier: Modifier){
    NavHost(
        navController = navController,
        startDestination = Login,
        modifier = modifier
    ){
        composable<Inicio>{
            TelaInicio(
                irParaDetalhes = { id ->
                    navController.navigate(Detalhe(id))
                }
            )
        }

        composable<Buscar> {
            TelaBuscar(irParaDetalhes = {
                    id -> navController
                .navigate(Detalhe(id))
            })
        }

        composable<Lista> {
            TelaLista(irParaDetalhes = {
                    id -> navController
                .navigate(Detalhe(id))
            })
        }

        composable<Detalhe>(
            enterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Up,
                    animationSpec = tween(600)
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = tween(900)
                )
            }
        ){ back ->
            val rota = back.toRoute<Detalhe>()
            TelaDetalhe(
                filmeId = rota.id,
                irParaAvaliar = { id ->
                    navController.navigate(Avaliar(id))
                },
                irInicio = {
                    navController.popBackStack(
                        route = Inicio,
                        inclusive = false
                    )
                },
                voltar = {
                    navController.popBackStack()
                }
            )

        }

        composable<Avaliar>{ back ->
            val rota = back.toRoute<Avaliar>()
            TelaAvaliar(
                filmeId = rota.id,
                voltar = {
                    navController.popBackStack()
                }
            )
        }

        composable<Login> {
            LoginScreen(
                irLogin = {
                navController.navigate(Inicio){
                    popUpTo(Login) { inclusive = true}
                }
            }, irCadastro = {
                navController.navigate(Cadastro)
            }
            )
        }

        composable<Cadastro>{
            CadastroScreen(
                {navController.popBackStack()}
            )
        }
    }
}
