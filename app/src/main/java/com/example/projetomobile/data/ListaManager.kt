package com.example.projetomobile.data

import androidx.compose.runtime.mutableStateListOf

object ListaManager {
    val queroVer = mutableStateListOf<Long>(1, 2, 7, 8, 9)
    val assistidos = mutableStateListOf<Long>(4, 3, 6)

    fun naLista(id: Long): Boolean{
        return queroVer.contains(id) || assistidos.contains(id)
    }

    fun adicionar(id: Long): Boolean{
        if (naLista(id)) return false

        queroVer.add(id)
        return true
    }

    fun remover(id: Long): Boolean{
        if (!naLista(id)) return false

        queroVer.remove(id)
        assistidos.remove(id)
        return true
    }

    fun avaliar(id: Long): Boolean{
        queroVer.remove(id)
        if (assistidos.contains(id)) return false

        assistidos.add(id)
        return true
    }
}
