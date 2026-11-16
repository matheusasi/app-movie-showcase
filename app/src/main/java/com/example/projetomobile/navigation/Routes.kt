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

@Serializable
data class ItemListaEditar(val id: Long)

@Serializable
data object Colecoes

@Serializable
data object CadastrarColecao

@Serializable
data class ColecaoEditar(val id: Long)

@Serializable
data object Momentos

@Serializable
data object CadastrarMomento

@Serializable
data class MomentoEditar(val id: Long)
