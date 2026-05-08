package com.example.appaprendizajemh220744.utils

object PasswordValidator {

    fun esValida(password: String): Boolean {
        val regex = Regex("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#\$%^&*]).{12,}$")
        return regex.matches(password)
    }

    fun mensajeError(): String {
        return "La contraseña debe tener mínimo 12 caracteres, una mayúscula, una minúscula, un número y un carácter especial."
    }
}