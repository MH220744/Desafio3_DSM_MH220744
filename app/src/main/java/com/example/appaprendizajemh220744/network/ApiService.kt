package com.example.appaprendizajemh220744.network

import com.example.appaprendizajemh220744.model.Recurso
import com.example.appaprendizajemh220744.model.Usuario
import retrofit2.Call
import retrofit2.http.*

interface ApiService {

    @GET("resources")
    fun obtenerRecursos(): Call<List<Recurso>>

    @GET("resources/{id}")
    fun obtenerRecursoPorId(
        @Path("id") id: String
    ): Call<Recurso>

    @POST("resources")
    fun crearRecurso(
        @Body recurso: Recurso
    ): Call<Recurso>

    @PUT("resources/{id}")
    fun actualizarRecurso(
        @Path("id") id: String,
        @Body recurso: Recurso
    ): Call<Recurso>

    @DELETE("resources/{id}")
    fun eliminarRecurso(
        @Path("id") id: String
    ): Call<Void>

    @GET("users")
    fun obtenerUsuarios(): Call<List<Usuario>>

    @POST("users")
    fun registrarUsuario(
        @Body usuario: Usuario
    ): Call<Usuario>
}