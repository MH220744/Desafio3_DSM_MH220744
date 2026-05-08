package com.example.appaprendizajemh220744

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.appaprendizajemh220744.controller.RecursoController
import com.example.appaprendizajemh220744.model.Recurso

class RecursoFormActivity : AppCompatActivity() {

    private val recursoController = RecursoController()
    private var recursoId: String? = null
    private var ratingPromedioActual: Double = 0.0
    private var totalRatingsActual: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recurso_form)

        val edtTitulo = findViewById<EditText>(R.id.edtTituloRecurso)
        val edtDescripcion = findViewById<EditText>(R.id.edtDescripcionRecurso)
        val edtTipo = findViewById<EditText>(R.id.edtTipoRecurso)
        val edtEnlace = findViewById<EditText>(R.id.edtEnlaceRecurso)
        val edtImagen = findViewById<EditText>(R.id.edtImagenRecurso)
        val btnGuardar = findViewById<Button>(R.id.btnGuardarRecurso)

        recursoId = intent.getStringExtra("id")

        if (recursoId != null) {
            edtTitulo.setText(intent.getStringExtra("titulo"))
            edtDescripcion.setText(intent.getStringExtra("descripcion"))
            edtTipo.setText(intent.getStringExtra("tipo"))
            edtEnlace.setText(intent.getStringExtra("enlace"))
            edtImagen.setText(intent.getStringExtra("imagen"))
            ratingPromedioActual = intent.getDoubleExtra("ratingPromedio", 0.0)
            totalRatingsActual = intent.getIntExtra("totalRatings", 0)
        }

        btnGuardar.setOnClickListener {
            val titulo = edtTitulo.text.toString().trim()
            val descripcion = edtDescripcion.text.toString().trim()
            val tipo = edtTipo.text.toString().trim()
            val enlace = edtEnlace.text.toString().trim()
            val imagen = edtImagen.text.toString().trim()

            if (titulo.isEmpty() || descripcion.isEmpty() || tipo.isEmpty() || enlace.isEmpty() || imagen.isEmpty()) {
                Toast.makeText(this, "Completa todos los campos.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val recurso = Recurso(
                id = recursoId,
                titulo = titulo,
                descripcion = descripcion,
                tipo = tipo,
                enlace = enlace,
                imagen = imagen,
                ratingPromedio = ratingPromedioActual,
                totalRatings = totalRatingsActual
            )

            if (recursoId == null) {
                crearRecurso(recurso)
            } else {
                actualizarRecurso(recurso)
            }
        }
    }

    private fun crearRecurso(recurso: Recurso) {
        recursoController.crearRecurso(
            recurso = recurso,
            onSuccess = {
                Toast.makeText(this, "Recurso creado correctamente.", Toast.LENGTH_SHORT).show()
                finish()
            },
            onError = { mensaje ->
                Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show()
            }
        )
    }

    private fun actualizarRecurso(recurso: Recurso) {
        recursoController.actualizarRecurso(
            id = recursoId ?: "",
            recurso = recurso,
            onSuccess = {
                Toast.makeText(this, "Recurso actualizado correctamente.", Toast.LENGTH_SHORT).show()
                finish()
            },
            onError = { mensaje ->
                Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show()
            }
        )
    }
}