package com.example.appaprendizajemh220744

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.appaprendizajemh220744.controller.AuthController
import com.example.appaprendizajemh220744.storage.SessionManager

class LoginActivity : AppCompatActivity() {

    private val authController = AuthController()
    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        sessionManager = SessionManager(this)

        if (sessionManager.estaLogueado()) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

        val edtEmail = findViewById<EditText>(R.id.edtEmail)
        val edtPassword = findViewById<EditText>(R.id.edtPassword)
        val btnLogin = findViewById<Button>(R.id.btnLogin)
        val txtIrRegistro = findViewById<TextView>(R.id.txtIrRegistro)

        btnLogin.setOnClickListener {
            val email = edtEmail.text.toString().trim()
            val password = edtPassword.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Completa todos los campos.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            authController.login(
                email = email,
                password = password,
                onSuccess = { usuario ->
                    sessionManager.guardarSesion(
                        id = usuario.id ?: "",
                        nombre = usuario.nombre,
                        email = usuario.email,
                        rol = usuario.rol
                    )

                    Toast.makeText(
                        this,
                        "Bienvenido, ${usuario.nombre}",
                        Toast.LENGTH_SHORT
                    ).show()

                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                },
                onError = { mensaje ->
                    Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show()
                }
            )
        }

        txtIrRegistro.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }
}