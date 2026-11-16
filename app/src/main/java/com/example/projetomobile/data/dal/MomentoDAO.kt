package com.example.projetomobile.data.dal

import android.content.ContentValues
import android.content.Context
import com.example.projetomobile.data.Momento
import com.example.projetomobile.data.local.AppDatabase

internal class MomentoDAO(context: Context) {
    private val appContext = context.applicationContext

    fun cadastrar(usuarioId: Long, filmeId: Long, data: String, legenda: String, foto: ByteArray) {
        AppDatabase(appContext).use { db ->
            val dados = ContentValues().apply {
                put("usuario_id", usuarioId)
                put("filme_id", filmeId)
                put("data", data)
                put("legenda", legenda)
                put("foto", foto)
            }
            db.writableDatabase.insertOrThrow(
                "momentos",
                null,
                dados
            )
        }
    }

    fun listar(usuarioId: Long): List<Momento> = AppDatabase(appContext).use { db ->
        db.readableDatabase.query(
            "momentos", arrayOf("id", "filme_id", "data", "legenda", "foto"),
            "usuario_id = ?", arrayOf(usuarioId.toString()),
            null, null,
            "id DESC"
        ).use { momento ->
            buildList {
                while (momento.moveToNext()){
                    add(Momento(momento.getLong(0),
                        momento.getLong(1),
                        momento.getString(2),
                        momento.getString(3),
                        momento.getBlob(4)))
                }
            }
        }
    }

    fun buscarPorId(id: Long, usuarioId: Long): Momento? = AppDatabase(appContext).use { db ->
        db.readableDatabase.query(
            "momentos", arrayOf("id", "filme_id", "data", "legenda", "foto"),
            "id = ? AND usuario_id = ?", arrayOf(id.toString(), usuarioId.toString()),
            null, null, null
        ).use { momento ->
            if(momento.moveToFirst()) Momento(
                momento.getLong(0),
                momento.getLong(1),
                momento.getString(2),
                momento.getString(3),
                momento.getBlob(4)
            )
            else null
        }
    }

    fun alterar(id: Long, usuarioId: Long, filmeId: Long, legenda: String, foto: ByteArray){
        AppDatabase(appContext).use { db ->
            val valores = ContentValues().apply {
                put("filme_id", filmeId)
                put("legenda", legenda)
                put("foto", foto)
            }
            val alterado = db.writableDatabase.update(
                "momentos",
                valores, "id = ? AND usuario_id = ?",
                arrayOf(id.toString(), usuarioId.toString())
            )
            require(alterado == 1) {"Momento não encontrado, atualize a lista"}
        }
    }

    fun excluir(id: Long, usuarioId: Long){
        require(usuarioId > 0) {"Faça o login para continuar"}
        AppDatabase(appContext).use { db ->
            val excluido = db.writableDatabase.delete(
                "momentos", "id = ? AND usuario_id = ?",
                arrayOf(id.toString(), usuarioId.toString()))

            require(excluido == 1) {"Momento não encontrado, atualize a lista"}
        }
    }
}
