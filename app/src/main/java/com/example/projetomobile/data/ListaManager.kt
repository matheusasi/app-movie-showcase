package com.example.projetomobile.data

import androidx.compose.runtime.mutableStateListOf

object ListaManager {
    val queroVer = mutableStateListOf(1, 2, 7, 8, 9)
    val assistidos = mutableStateListOf(4, 3, 6)

    private val avaliacoes = mutableListOf<Avaliacao>()

    fun naLista(id: Int): Boolean{
        return queroVer.contains(id) || assistidos.contains(id)
    }

    fun adicionar(id: Int): Boolean{
        if (naLista(id)) return false

        queroVer.add(id)
        return true
    }

    fun remover(id: Int): Boolean{
        if (!naLista(id)) return false

        queroVer.remove(id)
        assistidos.remove(id)
        return true
    }

    fun avaliar(avaliacao: Avaliacao): Boolean{
        if (avaliacoes.any { it.filmeId == avaliacao.filmeId }) return false

        avaliacoes.add(avaliacao)
        queroVer.remove(avaliacao.filmeId)
        if (!assistidos.contains(avaliacao.filmeId)) assistidos.add(avaliacao.filmeId)
        return true
    }
}
