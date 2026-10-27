package com.example.projetomobile.data.dal

import android.content.ContentValues
import android.content.Context
import com.example.projetomobile.data.Usuario
import com.example.projetomobile.data.local.AppDatabase

internal class SenhaSalva(val hash: ByteArray, val salt: ByteArray, val usuarioId: Long )

internal class UsuarioDAO(context: Context) {
    private val appContext = context.applicationContext

    fun cadastrar(nome: String, email: String, senha: ByteArray, salt: ByteArray) {
        AppDatabase(appContext).use { db ->
            val dados = ContentValues().apply {
                put("nome", nome)
                put("email", email)
                put("senha", senha)
                put("salt", salt)
            }
            db.writableDatabase.insertOrThrow(
                "usuarios",
                null,
                dados
            )
        }
    }

    fun buscarSenha(email: String): SenhaSalva?{
        return AppDatabase(appContext).use { db ->
            db.readableDatabase.query(
                "usuarios",
                arrayOf("senha", "salt", "id"),
                "email = ?",
                arrayOf(email),
                null, null, null
            ).use { usuario ->
                if (usuario.moveToFirst()) SenhaSalva(
                    usuario.getBlob(0),
                    usuario.getBlob(1),
                    usuario.getLong(2))
                else null
            }
        }
    }

    fun listar(): List<Usuario> = AppDatabase(appContext).use { db ->
        db.readableDatabase.query(
            "usuarios", arrayOf("id", "nome", "email"),
            null, null, null, null,
            "nome COLLATE NOCASE, id"
        ).use { usuario ->
            buildList {
                while (usuario.moveToNext()){
                    add(Usuario(usuario.getLong(0),
                        usuario.getString(1),
                        usuario.getString(2)))
                }
            }
        }
    }

    fun buscarPorId(id: Long): Usuario? = AppDatabase(appContext).use { db ->
        db.readableDatabase.query(
            "usuarios", arrayOf("id", "nome", "email"),
            "id = ?", arrayOf(id.toString()),
            null, null, null
        ).use { usuario ->
            if(usuario.moveToFirst()) Usuario(
                usuario.getLong(0),
                usuario.getString(1),
                usuario.getString(2)
            )
            else null
        }
    }

    fun alterar(id:Long, nome: String, hash: ByteArray? = null,
                salt: ByteArray? = null){
        AppDatabase(appContext).use { db ->
            val valores = ContentValues().apply {
                put("nome", nome)
                if(hash != null) put("senha", hash)
                if(salt != null) put("salt", salt)
            }
            val alterado = db.writableDatabase.update(
                "usuarios",
                valores, "id = ?", arrayOf(id.toString())
            )
            require(alterado == 1 ) {"Usuário não encontrado, atualize a lista"}
        }
    }

    fun excluir(id: Long, usuarioAtualId: Long){
        require(usuarioAtualId > 0) {"Faça o login para continuar"}
        require(id != usuarioAtualId) {"Você não pode excluir o seu usuário"}
        AppDatabase(appContext).use { db ->
            val excluido = db.writableDatabase.delete(
                "usuarios", "id = ? AND id <> ?",
                arrayOf(id.toString(), usuarioAtualId.toString()))

            require(excluido == 1) {"Usuário não encontrado, atualize a lista"}

            db.writableDatabase.delete(
                "avaliacoes", "usuario_id = ?",
                arrayOf(id.toString()))
        }
    }
}
