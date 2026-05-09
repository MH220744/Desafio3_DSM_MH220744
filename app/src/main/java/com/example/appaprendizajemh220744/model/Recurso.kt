package com.example.appaprendizajemh220744.model

import com.google.gson.annotations.SerializedName

data class Recurso(
    @SerializedName("id")
    val id: String? = null,

    @SerializedName("titulo")
    val titulo: String,

    @SerializedName("descripcion")
    val descripcion: String,

    @SerializedName("tipo")
    val tipo: String,

    @SerializedName("enlace")
    val enlace: String,

    @SerializedName("imagen")
    val imagen: String,

    @SerializedName("ratingPromedio")
    val ratingPromedio: Double = 0.0,

    @SerializedName("totalRatings")
    val totalRatings: Int = 0
)