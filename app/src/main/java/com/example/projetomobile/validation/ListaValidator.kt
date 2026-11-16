package com.example.projetomobile.validation

object ListaValidator {
    fun validar(nome: String, descricao: String, filmes: List<Long>){
        require(nome.length in 3 .. 60){
            "Informe um nome entre 3 e 60 caracteres."
        }
        require(descricao.length <= 200){
            "Use uma descrição de até 200 caracteres."
        }
        require(filmes.isNotEmpty()){
            "Escolha pelo menos um filme."
        }
    }

    fun validar(observacao: String){
        require(observacao.length <= 200){
            "Use uma observação de até 200 caracteres."
        }
    }
}
