package com.example.appaprendizajemh220744.storage

import android.content.Context

class SessionManager(context: Context) {

    private val prefs = context.getSharedPreferences("sesion_usuario", Context.MODE_PRIVATE)

    fun guardarSesion(id: String, nombre: String, email: String, rol: String) {
        prefs.edit()
            .putString("id", id)
            .putString("nombre", nombre)
            .putString("email", email)
            .putString("rol", rol)
            .putBoolean("logueado", true)
            .apply()
    }

    fun estaLogueado(): Boolean {
        return prefs.getBoolean("logueado", false)
    }

    fun obtenerIdUsuario(): String {
        return prefs.getString("id", "") ?: ""
    }

    fun obtenerNombre(): String {
        return prefs.getString("nombre", "") ?: ""
    }

    fun obtenerEmail(): String {
        return prefs.getString("email", "") ?: ""
    }

    fun obtenerRol(): String {
        return prefs.getString("rol", "Estudiante") ?: "Estudiante"
    }

    fun cerrarSesion() {
        prefs.edit().clear().apply()
    }
}