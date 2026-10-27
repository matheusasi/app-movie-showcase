package com.example.projetomobile.data

import java.util.Locale
data class Filme(
    val id: Long,
    val titulo: String,
    val ano: Long,
    val genero: String,
    val duracaoMin: Long,
    val diretor: String,
    val nota: String,
    val totalAvaliacoes: Long,
    val sinopse: String
)

val GENEROS = listOf("Todos", "Drama", "Suspense", "Ficção", "Comédia", "Animação")
val LOCAIS = listOf("Cinema", "Streaming", "TV aberta")
fun formatarNota(nota: String): String = nota.replace('.', ',')
fun formatarNota(nota: Float): String =
    String.format(Locale.US, "%.1f", nota).replace('.', ',')

fun formatarTotal(total: Long): String =
    String.format(Locale.forLanguageTag("pt-BR"), "%,d", total)
