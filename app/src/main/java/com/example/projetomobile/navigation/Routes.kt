package com.example.projetomobile.navigation

import kotlinx.serialization.Serializable


@Serializable
data object Inicio

@Serializable
data object Buscar

@Serializable
data object Lista

@Serializable
data class Detalhe(val id: Int)

@Serializable
data class Avaliar(val id: Int)

@Serializable
data object Login

@Serializable
data object Cadastro
