package com.example.projetomobile.validation

object AvaliacaoValidator {
    fun validar(data: String, nota: Float, comentario: String){
        require(data.isNotBlank()){
            "Escolha a data em que você assistiu."
        }
        require(nota in 0f .. 10f){
            "A nota deve ser entre 0 e 10."
        }
        validarComentario(comentario)
    }

    fun validar(comentario: String){
        validarComentario(comentario)
    }

    private fun validarComentario(comentario: String){
        require(comentario.length <= 500){
            "Use um comentário de até 500 caracteres."
        }
    }
}
