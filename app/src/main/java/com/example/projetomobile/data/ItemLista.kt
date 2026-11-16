package com.example.projetomobile.data

data class ItemLista(
    val id: Long,
    val filmeId: Long,
    val status: String,
    val observacao: String
)

const val QUERO_VER = "Quero ver"
const val ASSISTIDO = "Assistido"
