package com.example.appaprendizajemh220744.model

data class Recurso(
    val id: String? = null,
    val titulo: String,
    val descripcion: String,
    val tipo: String,
    val enlace: String,
    val imagen: String,
    val ratingPromedio: Double = 0.0,
    val totalRatings: Int = 0
)