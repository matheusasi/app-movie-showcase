package com.example.projetomobile.data.dal

import android.content.ContentValues
import android.content.Context
import com.example.projetomobile.data.ListaPersonalizada
import com.example.projetomobile.data.local.AppDatabase

internal class ListaDAO(context: Context) {
    private val appContext = context.applicationContext

    fun cadastrar(usuarioId: Long, nome: String, descricao: String, filmes: List<Long>) {
        AppDatabase(appContext).use { db ->
            val dados = ContentValues().apply {
                put("usuario_id", usuarioId)
                put("nome", nome)
                put("descricao", descricao)
            }
            val listaId = db.writableDatabase.insertOrThrow(
                "listas",
                null,
                dados
            )
            salvarFilmes(db, listaId, filmes)
        }
    }

    fun listar(usuarioId: Long): List<ListaPersonalizada> = AppDatabase(appContext).use { db ->
        db.readableDatabase.query(
            "listas", arrayOf("id", "nome", "descricao"),
            "usuario_id = ?", arrayOf(usuarioId.toString()),
            null, null,
            "nome COLLATE NOCASE, id"
        ).use { lista ->
            buildList {
                while (lista.moveToNext()){
                    add(ListaPersonalizada(lista.getLong(0),
                        lista.getString(1),
                        lista.getString(2),
                        buscarFilmes(db, lista.getLong(0))))
                }
            }
        }
    }

    fun buscarPorId(id: Long, usuarioId: Long): ListaPersonalizada? = AppDatabase(appContext).use { db ->
        db.readableDatabase.query(
            "listas", arrayOf("id", "nome", "descricao"),
            "id = ? AND usuario_id = ?", arrayOf(id.toString(), usuarioId.toString()),
            null, null, null
        ).use { lista ->
            if(lista.moveToFirst()) ListaPersonalizada(
                lista.getLong(0),
                lista.getString(1),
                lista.getString(2),
                buscarFilmes(db, lista.getLong(0))
            )
            else null
        }
    }

    fun alterar(id: Long, usuarioId: Long, nome: String, descricao: String, filmes: List<Long>){
        AppDatabase(appContext).use { db ->
            val valores = ContentValues().apply {
                put("nome", nome)
                put("descricao", descricao)
            }
            val alterado = db.writableDatabase.update(
                "listas",
                valores, "id = ? AND usuario_id = ?",
                arrayOf(id.toString(), usuarioId.toString())
            )
            require(alterado == 1) {"Lista não encontrada, atualize a lista"}

            db.writableDatabase.delete(
                "lista_filmes", "lista_id = ?",
                arrayOf(id.toString()))
            salvarFilmes(db, id, filmes)
        }
    }

    fun excluir(id: Long, usuarioId: Long){
        require(usuarioId > 0) {"Faça o login para continuar"}
        AppDatabase(appContext).use { db ->
            val excluido = db.writableDatabase.delete(
                "listas", "id = ? AND usuario_id = ?",
                arrayOf(id.toString(), usuarioId.toString()))

            require(excluido == 1) {"Lista não encontrada, atualize a lista"}

            db.writableDatabase.delete(
                "lista_filmes", "lista_id = ?",
                arrayOf(id.toString()))
        }
    }

    private fun salvarFilmes(db: AppDatabase, listaId: Long, filmes: List<Long>){
        filmes.forEach { filmeId ->
            val dados = ContentValues().apply {
                put("lista_id", listaId)
                put("filme_id", filmeId)
            }
            db.writableDatabase.insertOrThrow("lista_filmes", null, dados)
        }
    }

    private fun buscarFilmes(db: AppDatabase, listaId: Long): List<Long> =
        db.readableDatabase.query(
            "lista_filmes", arrayOf("filme_id"),
            "lista_id = ?", arrayOf(listaId.toString()),
            null, null, null
        ).use { filme ->
            buildList {
                while (filme.moveToNext()){
                    add(filme.getLong(0))
                }
            }
        }
}
