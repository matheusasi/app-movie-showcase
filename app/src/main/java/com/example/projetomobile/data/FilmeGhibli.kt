package com.example.projetomobile.data

import com.google.gson.annotations.SerializedName

data class FilmeGhibli(
    @SerializedName("id") val id: String? = null,
    @SerializedName("title") val titulo: String? = null,
    @SerializedName("release_date") val ano: String? = null,
    @SerializedName("director") val diretor: String? = null,
    @SerializedName("running_time") val duracao: String? = null,
    @SerializedName("description") val sinopse: String? = null,
)
