package com.example.projetomobile.data

// data class -> gera toString(), equals(), copy() e desestruturação (aula de POO)
data class Filme(
    val id: Int,
    val titulo: String,
    val ano: Int,
    val genero: String,
    val duracaoMin: Int,
    val diretor: String,
    val nota: Double,
    val totalAvaliacoes: Int,
    val sinopse: String
)

data class Avaliacao(
    val filmeId: Int,
    val data: String,
    val onde: String,
    val nota: Float,
    val comentario: String
)

// Listas fixas (aula de coleções).
val LOCAIS = listOf("Cinema", "Streaming", "TV aberta")

val CATALOGO = listOf(
    Filme(
        1, "Sete Samurais", 1954, "Drama", 207, "Akira Kurosawa", 9.2, 2841,
        "Uma aldeia de camponeses contrata sete guerreiros sem mestre para defendê-la " +
            "de bandidos que voltam a cada colheita."
    ),
    Filme(
        2, "A Chegada", 2016, "Ficção científica", 116, "Denis Villeneuve", 8.4, 1932,
        "Doze naves pousam pelo mundo e uma linguista é chamada para descobrir o que " +
            "os visitantes querem antes que o medo vença."
    ),
    Filme(
        3, "Cidade de Deus", 2002, "Drama", 130, "Fernando Meirelles", 9.0, 3120,
        "Dois meninos crescem na mesma favela do Rio: um pega uma câmera, o outro pega " +
            "uma arma. A cidade decide o resto."
    ),
    Filme(
        4, "Parasita", 2019, "Suspense", 132, "Bong Joon-ho", 8.9, 1204,
        "Uma família pobre se infiltra, um a um, na casa de uma família rica de Seul. " +
            "O plano funciona até o porão da casa revelar o que estava escondido ali."
    ),
    Filme(
        5, "Memórias de um Assassino", 2003, "Suspense", 132, "Bong Joon-ho", 8.1, 874,
        "Dois detetives de província investigam os primeiros assassinatos em série " +
            "registrados na Coreia do Sul, sem método e sem pistas."
    ),
    Filme(
        6, "O Hospedeiro", 2006, "Suspense", 120, "Bong Joon-ho", 7.4, 651,
        "Uma criatura sai do rio Han e leva uma menina. A família dela decide ir atrás " +
            "sozinha, contra o governo e contra o bom senso."
    ),
    Filme(
        7, "O Fabuloso Destino de Amélie Poulain", 2001, "Comédia", 122, "Jean-Pierre Jeunet", 8.3, 1580,
        "Uma garçonete tímida de Montmartre decide arrumar em segredo a vida das pessoas " +
            "à sua volta e esquece de arrumar a própria."
    ),
    Filme(
        8, "Tempos Modernos", 1936, "Comédia", 87, "Charles Chaplin", 8.5, 1420,
        "O Vagabundo enfrenta a linha de montagem, a fome e a polícia numa cidade que " +
            "trata gente como engrenagem."
    ),
    Filme(
        9, "Roma", 2018, "Drama", 135, "Alfonso Cuarón", 7.7, 903,
        "O retrato de um ano na vida de Cleo, empregada doméstica de uma família de " +
            "classe média na Cidade do México dos anos 1970."
    ),
    Filme(
        10, "A Viagem de Chihiro", 2001, "Animação", 125, "Hayao Miyazaki", 8.6, 2210,
        "Uma menina de dez anos fica presa num balneário de espíritos e precisa " +
            "trabalhar para recuperar o nome e salvar os pais."
    ),
    Filme(
        11, "O Menino e a Garça", 2023, "Animação", 124, "Hayao Miyazaki", 7.4, 512,
        "Depois de perder a mãe, um garoto segue uma garça falante até uma torre que " +
            "liga o mundo dos vivos ao dos mortos."
    ),
    Filme(
        12, "Blade Runner 2049", 2017, "Ficção científica", 164, "Denis Villeneuve", 8.0, 1766,
        "Um replicante policial descobre um segredo enterrado há trinta anos e sai " +
            "atrás de um homem que ninguém acha há tempo demais."
    ),
    Filme(
        13, "O Grande Hotel Budapeste", 2014, "Comédia", 99, "Wes Anderson", 8.1, 1345,
        "Um concierge lendário e seu mensageiro são acusados de roubar um quadro " +
            "renascentista às vésperas de uma guerra."
    ),
    Filme(
        14, "Interestelar", 2014, "Ficção científica", 169, "Christopher Nolan", 8.7, 3402,
        "Com a Terra morrendo, um grupo de astronautas atravessa um buraco de minhoca " +
            "atrás de um novo planeta e o tempo cobra caro por isso."
    ),
    Filme(
        15, "Bacurau", 2019, "Suspense", 132, "Kleber Mendonça Filho", 7.6, 788,
        "Um povoado do sertão some do mapa e começa a receber visitas estranhas. " +
            "O lugar decide se defender do seu próprio jeito."
    )
)

// Formata a nota com uma casa decimal usando só aritmética (9.2 -> "9,2").
fun formatarNota(nota: Double): String {
    val d = (nota * 10 + 0.5).toInt()
    val inteira = d / 10
    val decimal = d - inteira * 10
    return "$inteira,$decimal"
}
