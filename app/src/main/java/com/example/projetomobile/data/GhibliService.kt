package com.example.projetomobile.data

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

interface GhibliService {
    @GET("films")
    suspend fun listar(): List<FilmeGhibli>
}

object GhibliApi{
    val service: GhibliService by lazy {
        Retrofit.Builder()
            .baseUrl("https://ghibliapi.vercel.app/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(GhibliService::class.java)
    }
}
