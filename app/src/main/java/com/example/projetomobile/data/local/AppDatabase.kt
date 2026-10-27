package com.example.projetomobile.data.local

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import java.io.File

internal class AppDatabase(context: Context): SQLiteOpenHelper(
    context.applicationContext,
    File(context.noBackupFilesDir,
        "cineteca.db").absolutePath,
    null,
    2
) {
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("""
            CREATE TABLE usuarios(
            id INTEGER PRIMARY KEY,
            nome TEXT NOT NULL CHECK(length(nome) BETWEEN 3 AND 100),
            email TEXT NOT NULL UNIQUE CHECK(length(email) BETWEEN 3 AND 254),
            senha BLOB NOT NULL,
            salt BLOB NOT NULL
            )
        """.trimIndent())

        db.execSQL("""
            CREATE TABLE avaliacoes(
            id INTEGER PRIMARY KEY,
            usuario_id INTEGER NOT NULL,
            filme_id INTEGER NOT NULL,
            data TEXT NOT NULL,
            onde TEXT NOT NULL,
            nota TEXT NOT NULL,
            comentario TEXT NOT NULL CHECK(length(comentario) <= 500),
            UNIQUE(usuario_id, filme_id)
            )
        """.trimIndent())

        criarFilmes(db)
    }

    override fun onUpgrade(
        db: SQLiteDatabase,
        versaoAntiga: Int,
        versaoNova: Int
    ) {
        if (versaoAntiga < 2) criarFilmes(db)
    }

    private fun criarFilmes(db: SQLiteDatabase) {
        db.execSQL("""
            CREATE TABLE filmes(
            id INTEGER PRIMARY KEY,
            titulo TEXT NOT NULL CHECK(length(titulo) BETWEEN 1 AND 100),
            ano INTEGER NOT NULL CHECK(ano BETWEEN 1888 AND 2100),
            genero TEXT NOT NULL CHECK(length(genero) BETWEEN 2 AND 40),
            duracao_min INTEGER NOT NULL CHECK(duracao_min BETWEEN 1 AND 600),
            diretor TEXT NOT NULL CHECK(length(diretor) BETWEEN 2 AND 100),
            nota TEXT NOT NULL,
            total_avaliacoes INTEGER NOT NULL,
            sinopse TEXT NOT NULL CHECK(length(sinopse) BETWEEN 1 AND 1000)
            )
        """.trimIndent())

        db.execSQL("""
            INSERT INTO filmes(id, titulo, ano, genero, duracao_min, diretor, nota, total_avaliacoes, sinopse) VALUES
            (1, 'Sete Samurais', 1954, 'Drama', 207, 'Akira Kurosawa', '9.2', 2841,
            'Uma aldeia de camponeses contrata sete guerreiros sem mestre para defendê-la de bandidos que voltam a cada colheita.'),
            (2, 'A Chegada', 2016, 'Ficção científica', 116, 'Denis Villeneuve', '8.4', 1932,
            'Doze naves pousam pelo mundo e uma linguista é chamada para descobrir o que os visitantes querem antes que o medo vença.'),
            (3, 'Cidade de Deus', 2002, 'Drama', 130, 'Fernando Meirelles', '9.0', 3120,
            'Dois meninos crescem na mesma favela do Rio: um pega uma câmera, o outro pega uma arma. A cidade decide o resto.'),
            (4, 'Parasita', 2019, 'Suspense', 132, 'Bong Joon-ho', '8.9', 1204,
            'Uma família pobre se infiltra, um a um, na casa de uma família rica de Seul. O plano funciona até que o porão da casa revela o que estava escondido ali embaixo.'),
            (5, 'Memórias de um Assassino', 2003, 'Suspense', 132, 'Bong Joon-ho', '8.1', 874,
            'Dois detetives de província investigam os primeiros assassinatos em série registrados na Coreia do Sul, sem método e sem pistas.'),
            (6, 'O Hospedeiro', 2006, 'Suspense', 120, 'Bong Joon-ho', '7.4', 651,
            'Uma criatura sai do rio Han e leva uma menina. A família dela decide ir atrás sozinha, contra o governo e contra o bom senso.'),
            (7, 'O Fabuloso Destino de Amélie Poulain', 2001, 'Comédia', 122, 'Jean-Pierre Jeunet', '8.3', 1580,
            'Uma garçonete tímida de Montmartre decide arrumar em segredo a vida das pessoas à sua volta — e esquece de arrumar a própria.'),
            (8, 'Tempos Modernos', 1936, 'Comédia', 87, 'Charles Chaplin', '8.5', 1420,
            'O Vagabundo enfrenta a linha de montagem, a fome e a polícia numa cidade que trata gente como engrenagem.'),
            (9, 'Roma', 2018, 'Drama', 135, 'Alfonso Cuarón', '7.7', 903,
            'O retrato de um ano na vida de Cleo, empregada doméstica de uma família de classe média na Cidade do México dos anos 1970.'),
            (10, 'A Viagem de Chihiro', 2001, 'Animação', 125, 'Hayao Miyazaki', '8.6', 2210,
            'Uma menina de dez anos fica presa num balneário de espíritos e precisa trabalhar para recuperar o nome e salvar os pais.'),
            (11, 'O Menino e a Garça', 2023, 'Animação', 124, 'Hayao Miyazaki', '7.4', 512,
            'Depois de perder a mãe, um garoto segue uma garça falante até uma torre que liga o mundo dos vivos ao dos mortos.'),
            (12, 'Blade Runner 2049', 2017, 'Ficção científica', 164, 'Denis Villeneuve', '8.0', 1766,
            'Um replicante policial descobre um segredo enterrado há trinta anos e sai atrás de um homem que ninguém acha há tempo demais.'),
            (13, 'O Grande Hotel Budapeste', 2014, 'Comédia', 99, 'Wes Anderson', '8.1', 1345,
            'Um concierge lendário e seu mensageiro são acusados de roubar um quadro renascentista às vésperas de uma guerra.'),
            (14, 'Interestelar', 2014, 'Ficção científica', 169, 'Christopher Nolan', '8.7', 3402,
            'Com a Terra morrendo, um grupo de astronautas atravessa um buraco de minhoca atrás de um novo planeta — e o tempo cobra caro por isso.'),
            (15, 'Bacurau', 2019, 'Suspense', 132, 'Kleber Mendonça Filho', '7.6', 788,
            'Um povoado do sertão some do mapa e começa a receber visitas estranhas. O lugar decide se defender do seu próprio jeito.')
        """.trimIndent())
    }
}
