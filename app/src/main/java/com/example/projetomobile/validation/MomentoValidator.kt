package com.example.projetomobile.validation

object MomentoValidator {
    fun validar(filmeId: Long?, legenda: String, foto: ByteArray?){
        require(filmeId != null){
            "Escolha o filme que você está assistindo."
        }
        require(foto != null){
            "Tire uma foto do momento."
        }
        require(legenda.length <= 200){
            "Use uma legenda de até 200 caracteres."
        }
    }
}
