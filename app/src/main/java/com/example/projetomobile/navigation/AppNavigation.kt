package com.example.projetomobile.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.toRoute
import com.example.projetomobile.auth.SessaoUsuario
import com.example.projetomobile.ui.CadastroScreen
import com.example.projetomobile.ui.LoginScreen
import com.example.projetomobile.ui.TelaAvaliacoes
import com.example.projetomobile.ui.TelaAvaliar
import com.example.projetomobile.ui.TelaBuscar
import com.example.projetomobile.ui.TelaDetalhe
import com.example.projetomobile.ui.TelaEditarAvaliacao
import com.example.projetomobile.ui.TelaFilmeFormulario
import com.example.projetomobile.ui.TelaFilmes
import com.example.projetomobile.ui.TelaInicio
import com.example.projetomobile.ui.TelaLista
import com.example.projetomobile.ui.UsuarioEditarScreen
import com.example.projetomobile.ui.UsuarioScreen
import com.example.projetomobile.ui.viewmodel.AvaliacaoViewModel
import com.example.projetomobile.ui.viewmodel.FilmeViewModel
import com.example.projetomobile.ui.viewmodel.UsuarioViewModel

@Composable
fun AppNavigation(navController: NavHostController, modifier: Modifier){
    val entradaAtual by navController.currentBackStackEntryAsState()
    val destino = entradaAtual?.destination
    val usuarioAtual = SessaoUsuario.usuarioId
    LaunchedEffect(destino, usuarioAtual){
        val publica = destino?.hasRoute<Login>() == true ||
                destino?.hasRoute<Cadastro>() == true
        if (destino != null && !publica && usuarioAtual == null){
            navController.navigate(Login){
                popUpTo(0) { inclusive = true }
                launchSingleTop = true
            }
        }
    }

    NavHost(
        navController = navController,
        startDestination = Login,
        modifier = modifier
    ){
        composable<Inicio>{
            TelaInicio(
                viewModel = viewModel(),
                irParaDetalhes = { id ->
                    navController.navigate(Detalhe(id))
                }
            )
        }

        composable<Buscar> {
            TelaBuscar(
                viewModel = viewModel(),
                irParaDetalhes = {
                    id -> navController
                .navigate(Detalhe(id))
            })
        }

        composable<Lista> {
            TelaLista(
                viewModel = viewModel(),
                irParaDetalhes = {
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
                viewModel = viewModel(),
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
                viewModel = viewModel(),
                filmeViewModel = viewModel(),
                voltar = {
                    navController.popBackStack()
                }
            )
        }

        composable<Avaliacoes> { entrada ->
            val avaliacaoViewModel: AvaliacaoViewModel = viewModel()
            DisposableEffect(entrada) {
                val observador = LifecycleEventObserver{ _, evento ->
                    if(evento == Lifecycle.Event.ON_RESUME) avaliacaoViewModel.listar()
                }
                entrada.lifecycle.addObserver(observador)
                onDispose { entrada.lifecycle.removeObserver(observador) }
            }
            TelaAvaliacoes(
                viewModel = avaliacaoViewModel,
                filmeViewModel = viewModel(),
                irParaCadastro = { navController.navigate(Avaliar(0)) },
                irParaEdicao = { navController.navigate(AvaliacaoEditar(it)) }
            )
        }

        composable<AvaliacaoEditar> { entrada ->
            val rota = entrada.toRoute<AvaliacaoEditar>()
            TelaEditarAvaliacao(
                avaliacaoId = rota.id,
                viewModel = viewModel(),
                filmeViewModel = viewModel(),
                voltar = { navController.popBackStack() }
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
            },
                authViewModel = viewModel()
            )
        }

        composable<Cadastro>{
            CadastroScreen(
                {navController.popBackStack()},
                viewModel()
            )
        }

        composable<Usuarios> { entrada ->
            val usuarioViewModel: UsuarioViewModel = viewModel()
            DisposableEffect(entrada) {
                val observador = LifecycleEventObserver{ _, evento ->
                    if(evento == Lifecycle.Event.ON_RESUME) usuarioViewModel.listar()
                }
                entrada.lifecycle.addObserver(observador)
                onDispose { entrada.lifecycle.removeObserver(observador) }
            }
            UsuarioScreen(
                viewModel = usuarioViewModel,
                irParaCadastro = { navController.navigate(CadastrarUsuario) },
                irParaEdicao = { navController.navigate(UsuarioEditar(it)) },
                voltar = { navController.popBackStack() }
            )
        }

        composable<CadastrarUsuario>{
            CadastroScreen(
                irLogin = { navController.popBackStack() },
                authViewModel = viewModel()
            )
        }

        composable<Filmes> { entrada ->
            val filmeViewModel: FilmeViewModel = viewModel()
            DisposableEffect(entrada) {
                val observador = LifecycleEventObserver{ _, evento ->
                    if(evento == Lifecycle.Event.ON_RESUME) filmeViewModel.listar()
                }
                entrada.lifecycle.addObserver(observador)
                onDispose { entrada.lifecycle.removeObserver(observador) }
            }
            TelaFilmes(
                viewModel = filmeViewModel,
                irParaCadastro = { navController.navigate(CadastrarFilme) },
                irParaEdicao = { navController.navigate(FilmeEditar(it)) }
            )
        }

        composable<CadastrarFilme> {
            TelaFilmeFormulario(
                filmeId = null,
                viewModel = viewModel(),
                voltar = { navController.popBackStack() }
            )
        }

        composable<FilmeEditar> { entrada ->
            val rota = entrada.toRoute<FilmeEditar>()
            TelaFilmeFormulario(
                filmeId = rota.id,
                viewModel = viewModel(),
                voltar = { navController.popBackStack() }
            )
        }

        composable<UsuarioEditar> { entrada ->
            val rota = entrada.toRoute<UsuarioEditar>()
            UsuarioEditarScreen(
                usuarioId = rota.id,
                viewModel = viewModel(),
                voltar = { navController.popBackStack() }
            )
        }
    }
}
