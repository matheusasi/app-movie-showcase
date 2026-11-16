package com.example.projetomobile.data

data class ListaPersonalizada(
    val id: Long,
    val nome: String,
    val descricao: String,
    val filmes: List<Long>
)
