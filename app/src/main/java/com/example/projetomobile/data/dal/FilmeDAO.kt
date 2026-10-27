package com.example.projetomobile.data.dal

import android.content.ContentValues
import android.content.Context
import com.example.projetomobile.data.Filme
import com.example.projetomobile.data.local.AppDatabase

internal class FilmeDAO(context: Context) {
    private val appContext = context.applicationContext

    fun cadastrar(titulo: String, ano: String, genero: String, duracaoMin: String,
                  diretor: String, sinopse: String) {
        AppDatabase(appContext).use { db ->
            val dados = ContentValues().apply {
                put("titulo", titulo)
                put("ano", ano)
                put("genero", genero)
                put("duracao_min", duracaoMin)
                put("diretor", diretor)
                put("nota", "0.0")
                put("total_avaliacoes", 0)
                put("sinopse", sinopse)
            }
            db.writableDatabase.insertOrThrow(
                "filmes",
                null,
                dados
            )
        }
    }

    fun listar(): List<Filme> = AppDatabase(appContext).use { db ->
        db.readableDatabase.query(
            "filmes", arrayOf("id", "titulo", "ano", "genero", "duracao_min",
                "diretor", "nota", "total_avaliacoes", "sinopse"),
            null, null, null, null,
            "id"
        ).use { filme ->
            buildList {
                while (filme.moveToNext()){
                    add(Filme(filme.getLong(0),
                        filme.getString(1),
                        filme.getLong(2),
                        filme.getString(3),
                        filme.getLong(4),
                        filme.getString(5),
                        filme.getString(6),
                        filme.getLong(7),
                        filme.getString(8)))
                }
            }
        }
    }

    fun buscarPorId(id: Long): Filme? = AppDatabase(appContext).use { db ->
        db.readableDatabase.query(
            "filmes", arrayOf("id", "titulo", "ano", "genero", "duracao_min",
                "diretor", "nota", "total_avaliacoes", "sinopse"),
            "id = ?", arrayOf(id.toString()),
            null, null, null
        ).use { filme ->
            if(filme.moveToFirst()) Filme(
                filme.getLong(0),
                filme.getString(1),
                filme.getLong(2),
                filme.getString(3),
                filme.getLong(4),
                filme.getString(5),
                filme.getString(6),
                filme.getLong(7),
                filme.getString(8)
            )
            else null
        }
    }

    fun alterar(id: Long, titulo: String, ano: String, genero: String, duracaoMin: String,
                diretor: String, sinopse: String){
        AppDatabase(appContext).use { db ->
            val valores = ContentValues().apply {
                put("titulo", titulo)
                put("ano", ano)
                put("genero", genero)
                put("duracao_min", duracaoMin)
                put("diretor", diretor)
                put("sinopse", sinopse)
            }
            val alterado = db.writableDatabase.update(
                "filmes",
                valores, "id = ?", arrayOf(id.toString())
            )
            require(alterado == 1) {"Filme não encontrado, atualize a lista"}
        }
    }

    fun excluir(id: Long){
        AppDatabase(appContext).use { db ->
            val excluido = db.writableDatabase.delete(
                "filmes", "id = ?",
                arrayOf(id.toString()))

            require(excluido == 1) {"Filme não encontrado, atualize a lista"}

            db.writableDatabase.delete(
                "avaliacoes", "filme_id = ?",
                arrayOf(id.toString()))
        }
    }
}
