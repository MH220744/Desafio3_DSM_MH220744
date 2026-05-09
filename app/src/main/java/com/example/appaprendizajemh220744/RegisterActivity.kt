package com.example.appaprendizajemh220744

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.appaprendizajemh220744.controller.AuthController
import com.example.appaprendizajemh220744.model.Usuario
import com.example.appaprendizajemh220744.utils.PasswordValidator

class RegisterActivity : AppCompatActivity() {

    private val authController = AuthController()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val edtNombre = findViewById<EditText>(R.id.edtNombre)
        val edtEmail = findViewById<EditText>(R.id.edtEmailRegistro)
        val edtPassword = findViewById<EditText>(R.id.edtPasswordRegistro)
        val spinnerRol = findViewById<Spinner>(R.id.spinnerRol)
        val btnRegistrar = findViewById<Button>(R.id.btnRegistrar)
        val btnRegresarRegistro = findViewById<Button>(R.id.btnRegresarRegistro)

        btnRegresarRegistro.setOnClickListener {
            finish()
        }

        val roles = listOf(
            getString(R.string.rol_estudiante),
            getString(R.string.rol_docente)
        )

        spinnerRol.adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            roles
        )

        btnRegistrar.setOnClickListener {
            val nombre = edtNombre.text.toString().trim()
            val email = edtEmail.text.toString().trim()
            val password = edtPassword.text.toString().trim()
            val rol = spinnerRol.selectedItem.toString()

            if (nombre.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Toast.makeText(
                    this,
                    getString(R.string.completa_campos),
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            if (!PasswordValidator.esValida(password)) {
                Toast.makeText(
                    this,
                    getString(R.string.password_error),
                    Toast.LENGTH_LONG
                ).show()
                return@setOnClickListener
            }

            val usuario = Usuario(
                nombre = nombre,
                email = email,
                password = password,
                rol = rol
            )

            authController.registrar(
                usuario = usuario,
                onSuccess = {
                    Toast.makeText(
                        this,
                        getString(R.string.usuario_registrado),
                        Toast.LENGTH_SHORT
                    ).show()
                    finish()
                },
                onError = { mensaje ->
                    Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show()
                }
            )
        }
    }
}