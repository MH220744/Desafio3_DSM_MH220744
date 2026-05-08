package com.example.appaprendizajemh220744.controller

import com.example.appaprendizajemh220744.model.Usuario
import com.example.appaprendizajemh220744.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AuthController {

    fun login(
        email: String,
        password: String,
        onSuccess: (Usuario) -> Unit,
        onError: (String) -> Unit
    ) {
        RetrofitClient.apiService.obtenerUsuarios().enqueue(object : Callback<List<Usuario>> {

            override fun onResponse(
                call: Call<List<Usuario>>,
                response: Response<List<Usuario>>
            ) {
                if (response.isSuccessful) {
                    val usuarioEncontrado = response.body()?.find {
                        it.email == email && it.password == password
                    }

                    if (usuarioEncontrado != null) {
                        onSuccess(usuarioEncontrado)
                    } else {
                        onError("Correo o contraseña incorrectos.")
                    }
                } else {
                    onError("No se pudo iniciar sesión.")
                }
            }

            override fun onFailure(call: Call<List<Usuario>>, t: Throwable) {
                onError("Error de conexión: ${t.message}")
            }
        })
    }

    fun registrar(
        usuario: Usuario,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        RetrofitClient.apiService.registrarUsuario(usuario).enqueue(object : Callback<Usuario> {

            override fun onResponse(call: Call<Usuario>, response: Response<Usuario>) {
                if (response.isSuccessful) {
                    onSuccess()
                } else {
                    onError("No se pudo registrar el usuario.")
                }
            }

            override fun onFailure(call: Call<Usuario>, t: Throwable) {
                onError("Error de conexión: ${t.message}")
            }
        })
    }
}