package com.example.appaprendizajemh220744.controller

import com.example.appaprendizajemh220744.model.Recurso
import com.example.appaprendizajemh220744.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RecursoController {

    fun obtenerRecursos(
        onSuccess: (List<Recurso>) -> Unit,
        onError: (String) -> Unit
    ) {
        RetrofitClient.apiService.obtenerRecursos().enqueue(object : Callback<List<Recurso>> {

            override fun onResponse(
                call: Call<List<Recurso>>,
                response: Response<List<Recurso>>
            ) {
                if (response.isSuccessful) {
                    onSuccess(response.body() ?: emptyList())
                } else {
                    onError("No se pudieron cargar los recursos.")
                }
            }

            override fun onFailure(call: Call<List<Recurso>>, t: Throwable) {
                onError("Error de conexión: ${t.message}")
            }
        })
    }

    fun crearRecurso(
        recurso: Recurso,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        RetrofitClient.apiService.crearRecurso(recurso).enqueue(object : Callback<Recurso> {

            override fun onResponse(call: Call<Recurso>, response: Response<Recurso>) {
                if (response.isSuccessful) {
                    onSuccess()
                } else {
                    onError("No se pudo crear el recurso.")
                }
            }

            override fun onFailure(call: Call<Recurso>, t: Throwable) {
                onError("Error de conexión: ${t.message}")
            }
        })
    }

    fun actualizarRecurso(
        id: String,
        recurso: Recurso,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        RetrofitClient.apiService.actualizarRecurso(id, recurso).enqueue(object : Callback<Recurso> {

            override fun onResponse(call: Call<Recurso>, response: Response<Recurso>) {
                if (response.isSuccessful) {
                    onSuccess()
                } else {
                    onError("No se pudo actualizar el recurso.")
                }
            }

            override fun onFailure(call: Call<Recurso>, t: Throwable) {
                onError("Error de conexión: ${t.message}")
            }
        })
    }

    fun eliminarRecurso(
        id: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        RetrofitClient.apiService.eliminarRecurso(id).enqueue(object : Callback<Void> {

            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    onSuccess()
                } else {
                    onError("No se pudo eliminar el recurso.")
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                onError("Error de conexión: ${t.message}")
            }
        })
    }
}