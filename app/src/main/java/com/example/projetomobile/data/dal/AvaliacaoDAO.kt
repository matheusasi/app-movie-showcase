package com.example.projetomobile.data.dal

import android.content.ContentValues
import android.content.Context
import com.example.projetomobile.data.Avaliacao
import com.example.projetomobile.data.local.AppDatabase

internal class AvaliacaoDAO(context: Context) {
    private val appContext = context.applicationContext

    fun cadastrar(usuarioId: Long, filmeId: Long, data: String, onde: String,
                  nota: String, comentario: String) {
        AppDatabase(appContext).use { db ->
            val dados = ContentValues().apply {
                put("usuario_id", usuarioId)
                put("filme_id", filmeId)
                put("data", data)
                put("onde", onde)
                put("nota", nota)
                put("comentario", comentario)
            }
            db.writableDatabase.insertOrThrow(
                "avaliacoes",
                null,
                dados
            )
        }
    }

    fun listar(usuarioId: Long): List<Avaliacao> = AppDatabase(appContext).use { db ->
        db.readableDatabase.query(
            "avaliacoes", arrayOf("id", "filme_id", "data", "onde", "nota", "comentario"),
            "usuario_id = ?", arrayOf(usuarioId.toString()),
            null, null,
            "id DESC"
        ).use { avaliacao ->
            buildList {
                while (avaliacao.moveToNext()){
                    add(Avaliacao(avaliacao.getLong(0),
                        avaliacao.getLong(1),
                        avaliacao.getString(2),
                        avaliacao.getString(3),
                        avaliacao.getString(4),
                        avaliacao.getString(5)))
                }
            }
        }
    }

    fun buscarPorId(id: Long, usuarioId: Long): Avaliacao? = AppDatabase(appContext).use { db ->
        db.readableDatabase.query(
            "avaliacoes", arrayOf("id", "filme_id", "data", "onde", "nota", "comentario"),
            "id = ? AND usuario_id = ?", arrayOf(id.toString(), usuarioId.toString()),
            null, null, null
        ).use { avaliacao ->
            if(avaliacao.moveToFirst()) Avaliacao(
                avaliacao.getLong(0),
                avaliacao.getLong(1),
                avaliacao.getString(2),
                avaliacao.getString(3),
                avaliacao.getString(4),
                avaliacao.getString(5)
            )
            else null
        }
    }

    fun alterar(id: Long, usuarioId: Long, onde: String, comentario: String){
        AppDatabase(appContext).use { db ->
            val valores = ContentValues().apply {
                put("onde", onde)
                put("comentario", comentario)
            }
            val alterado = db.writableDatabase.update(
                "avaliacoes",
                valores, "id = ? AND usuario_id = ?",
                arrayOf(id.toString(), usuarioId.toString())
            )
            require(alterado == 1) {"Avaliação não encontrada, atualize a lista"}
        }
    }

    fun excluir(id: Long, usuarioId: Long){
        require(usuarioId > 0) {"Faça o login para continuar"}
        AppDatabase(appContext).use { db ->
            val excluido = db.writableDatabase.delete(
                "avaliacoes", "id = ? AND usuario_id = ?",
                arrayOf(id.toString(), usuarioId.toString()))

            require(excluido == 1) {"Avaliação não encontrada, atualize a lista"}
        }
    }
}
