package com.example.projetomobile.navigation

import kotlinx.serialization.Serializable


@Serializable
data object Inicio

@Serializable
data object Buscar

@Serializable
data object Lista

@Serializable
data class Detalhe(val id: Long)

@Serializable
data class Avaliar(val id: Long)

@Serializable
data object Login

@Serializable
data object Cadastro

@Serializable
data object Avaliacoes

@Serializable
data class AvaliacaoEditar(val id: Long)

@Serializable
data object Usuarios

@Serializable
data class UsuarioEditar(val id: Long)

@Serializable
data object CadastrarUsuario

@Serializable
data object Filmes

@Serializable
data object CadastrarFilme

@Serializable
data class FilmeEditar(val id: Long)
