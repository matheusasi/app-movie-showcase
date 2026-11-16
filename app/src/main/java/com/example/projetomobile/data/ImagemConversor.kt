package com.example.projetomobile.data

import android.graphics.Bitmap
import java.io.ByteArrayOutputStream

object ImagemConversor {

    fun paraBlob(imagem: Bitmap): ByteArray{
        return ByteArrayOutputStream().use { saida ->
            check(imagem.compress(Bitmap.CompressFormat.PNG, 100, saida)){
                "Não foi possível salvar imagem."
            }
            saida.toByteArray()
        }
    }
}
