package com.example.appaprendizajemh220744

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.appaprendizajemh220744.adapter.RecursoAdapter
import com.example.appaprendizajemh220744.controller.RecursoController
import com.example.appaprendizajemh220744.model.Recurso
import com.example.appaprendizajemh220744.storage.SessionManager

class MainActivity : AppCompatActivity() {

    private val recursoController = RecursoController()
    private lateinit var sessionManager: SessionManager
    private lateinit var adapter: RecursoAdapter

    private var listaRecursosOriginal = listOf<Recurso>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sessionManager = SessionManager(this)

        val recyclerRecursos = findViewById<RecyclerView>(R.id.recyclerRecursos)
        val edtBuscar = findViewById<EditText>(R.id.edtBuscar)
        val btnBuscar = findViewById<Button>(R.id.btnBuscar)
        val btnAgregar = findViewById<Button>(R.id.btnAgregar)
        val btnLogout = findViewById<Button>(R.id.btnLogout)
        val progressBar = findViewById<ProgressBar>(R.id.progressBar)

        val rol = sessionManager.obtenerRol()

        btnAgregar.visibility = if (rol == "Docente") {
            View.VISIBLE
        } else {
            View.GONE
        }

        adapter = RecursoAdapter(
            recursos = mutableListOf(),
            rol = rol,
            onEditar = { recurso ->
                Toast.makeText(this, "Editar: ${recurso.titulo}", Toast.LENGTH_SHORT).show()
            },
            onEliminar = { recurso ->
                confirmarEliminacion(recurso)
            }
        )

        recyclerRecursos.layoutManager = LinearLayoutManager(this)
        recyclerRecursos.adapter = adapter

        btnBuscar.setOnClickListener {
            val texto = edtBuscar.text.toString().trim().lowercase()

            val filtrados = listaRecursosOriginal.filter { recurso ->
                recurso.id?.lowercase()?.contains(texto) == true ||
                        recurso.titulo.lowercase().contains(texto) ||
                        recurso.tipo.lowercase().contains(texto)
            }

            adapter.actualizarLista(filtrados)
        }

        btnAgregar.setOnClickListener {
            Toast.makeText(this, "Aquí abriremos formulario para agregar", Toast.LENGTH_SHORT).show()
        }

        btnLogout.setOnClickListener {
            sessionManager.cerrarSesion()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        cargarRecursos(progressBar)
    }

    override fun onResume() {
        super.onResume()
        val progressBar = findViewById<ProgressBar>(R.id.progressBar)
        cargarRecursos(progressBar)
    }

    private fun cargarRecursos(progressBar: ProgressBar) {
        progressBar.visibility = View.VISIBLE

        recursoController.obtenerRecursos(
            onSuccess = { recursos ->
                progressBar.visibility = View.GONE
                listaRecursosOriginal = recursos
                adapter.actualizarLista(recursos)
            },
            onError = { mensaje ->
                progressBar.visibility = View.GONE
                Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show()
            }
        )
    }

    private fun confirmarEliminacion(recurso: Recurso) {
        AlertDialog.Builder(this)
            .setTitle("Eliminar recurso")
            .setMessage("¿Seguro que deseas eliminar ${recurso.titulo}?")
            .setPositiveButton("Eliminar") { _, _ ->
                recursoController.eliminarRecurso(
                    id = recurso.id ?: "",
                    onSuccess = {
                        Toast.makeText(this, "Recurso eliminado.", Toast.LENGTH_SHORT).show()
                        cargarRecursos(findViewById(R.id.progressBar))
                    },
                    onError = { mensaje ->
                        Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show()
                    }
                )
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }
}