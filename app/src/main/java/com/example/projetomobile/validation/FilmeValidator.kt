package com.example.projetomobile.validation

object FilmeValidator {
    fun validar(titulo: String, ano: String, genero: String, duracaoMin: String,
                diretor: String, sinopse: String){
        require(titulo.length in 1 .. 100){
            "Informe um título de até 100 caracteres."
        }
        require(ano.length == 4){
            "Informe o ano com 4 números."
        }
        require(genero.length in 2 .. 40){
            "Informe um gênero entre 2 e 40 caracteres."
        }
        require(duracaoMin.length in 1 .. 3){
            "Informe a duração em minutos."
        }
        require(diretor.length in 2 .. 100){
            "Informe um diretor entre 2 e 100 caracteres."
        }
        require(sinopse.length in 1 .. 1000){
            "Informe uma sinopse de até 1000 caracteres."
        }
    }
}
