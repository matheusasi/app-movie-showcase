package com.example.projetomobile.data.dal

import android.content.ContentValues
import android.content.Context
import com.example.projetomobile.data.ASSISTIDO
import com.example.projetomobile.data.ItemLista
import com.example.projetomobile.data.QUERO_VER
import com.example.projetomobile.data.local.AppDatabase

internal class MinhaListaDAO(context: Context) {
    private val appContext = context.applicationContext

    fun adicionar(usuarioId: Long, filmeId: Long) {
        AppDatabase(appContext).use { db ->
            val dados = ContentValues().apply {
                put("usuario_id", usuarioId)
                put("filme_id", filmeId)
                put("status", QUERO_VER)
                put("observacao", "")
            }
            db.writableDatabase.insertOrThrow(
                "minha_lista",
                null,
                dados
            )
        }
    }

    fun listar(usuarioId: Long): List<ItemLista> = AppDatabase(appContext).use { db ->
        db.readableDatabase.query(
            "minha_lista", arrayOf("id", "filme_id", "status", "observacao"),
            "usuario_id = ?", arrayOf(usuarioId.toString()),
            null, null,
            "id DESC"
        ).use { item ->
            buildList {
                while (item.moveToNext()){
                    add(ItemLista(item.getLong(0),
                        item.getLong(1),
                        item.getString(2),
                        item.getString(3)))
                }
            }
        }
    }

    fun buscarPorId(id: Long, usuarioId: Long): ItemLista? = AppDatabase(appContext).use { db ->
        db.readableDatabase.query(
            "minha_lista", arrayOf("id", "filme_id", "status", "observacao"),
            "id = ? AND usuario_id = ?", arrayOf(id.toString(), usuarioId.toString()),
            null, null, null
        ).use { item ->
            if(item.moveToFirst()) ItemLista(
                item.getLong(0),
                item.getLong(1),
                item.getString(2),
                item.getString(3)
            )
            else null
        }
    }

    fun alterar(id: Long, usuarioId: Long, status: String, observacao: String){
        AppDatabase(appContext).use { db ->
            val valores = ContentValues().apply {
                put("status", status)
                put("observacao", observacao)
            }
            val alterado = db.writableDatabase.update(
                "minha_lista",
                valores, "id = ? AND usuario_id = ?",
                arrayOf(id.toString(), usuarioId.toString())
            )
            require(alterado == 1) {"Filme não encontrado na lista, atualize a lista"}
        }
    }

    fun marcarAssistido(usuarioId: Long, filmeId: Long){
        AppDatabase(appContext).use { db ->
            val valores = ContentValues().apply {
                put("status", ASSISTIDO)
            }
            val alterado = db.writableDatabase.update(
                "minha_lista",
                valores, "usuario_id = ? AND filme_id = ?",
                arrayOf(usuarioId.toString(), filmeId.toString())
            )
            if (alterado == 0) {
                val dados = ContentValues().apply {
                    put("usuario_id", usuarioId)
                    put("filme_id", filmeId)
                    put("status", ASSISTIDO)
                    put("observacao", "")
                }
                db.writableDatabase.insertOrThrow("minha_lista", null, dados)
            }
        }
    }

    fun excluir(id: Long, usuarioId: Long){
        require(usuarioId > 0) {"Faça o login para continuar"}
        AppDatabase(appContext).use { db ->
            val excluido = db.writableDatabase.delete(
                "minha_lista", "id = ? AND usuario_id = ?",
                arrayOf(id.toString(), usuarioId.toString()))

            require(excluido == 1) {"Filme não encontrado na lista, atualize a lista"}
        }
    }
}
