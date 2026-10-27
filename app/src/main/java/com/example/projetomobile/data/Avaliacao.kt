package com.example.projetomobile.data

data class Avaliacao(
    val id: Long,
    val filmeId: Long,
    val data: String,
    val onde: String,
    val nota: String,
    val comentario: String
)
