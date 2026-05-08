package com.example.appaprendizajemh220744.storage

import android.content.Context

class FavoritosManager(context: Context) {

    private val prefs = context.getSharedPreferences("favoritos_usuario", Context.MODE_PRIVATE)

    fun esFavorito(idUsuario: String, idRecurso: String): Boolean {
        val favoritos = obtenerFavoritos(idUsuario)
        return favoritos.contains(idRecurso)
    }

    fun agregarFavorito(idUsuario: String, idRecurso: String) {
        val favoritos = obtenerFavoritos(idUsuario).toMutableSet()
        favoritos.add(idRecurso)
        guardarFavoritos(idUsuario, favoritos)
    }

    fun quitarFavorito(idUsuario: String, idRecurso: String) {
        val favoritos = obtenerFavoritos(idUsuario).toMutableSet()
        favoritos.remove(idRecurso)
        guardarFavoritos(idUsuario, favoritos)
    }

    fun obtenerFavoritos(idUsuario: String): Set<String> {
        return prefs.getStringSet("favoritos_$idUsuario", emptySet()) ?: emptySet()
    }

    private fun guardarFavoritos(idUsuario: String, favoritos: Set<String>) {
        prefs.edit()
            .putStringSet("favoritos_$idUsuario", favoritos)
            .apply()
    }
}