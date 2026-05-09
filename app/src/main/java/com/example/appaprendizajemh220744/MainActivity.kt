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
import com.example.appaprendizajemh220744.storage.FavoritosManager

class MainActivity : AppCompatActivity() {

    private lateinit var favoritosManager: FavoritosManager
    private val recursoController = RecursoController()
    private lateinit var sessionManager: SessionManager
    private lateinit var adapter: RecursoAdapter

    private fun alternarFavorito(recurso: Recurso) {
        val idUsuario = sessionManager.obtenerIdUsuario()
        val idRecurso = recurso.id ?: return

        if (favoritosManager.esFavorito(idUsuario, idRecurso)) {
            favoritosManager.quitarFavorito(idUsuario, idRecurso)
            Toast.makeText(this, "Recurso quitado de favoritos.", Toast.LENGTH_SHORT).show()
        } else {
            favoritosManager.agregarFavorito(idUsuario, idRecurso)
            Toast.makeText(this, "Recurso agregado a favoritos.", Toast.LENGTH_SHORT).show()
        }

        cargarRecursos(findViewById(R.id.progressBar))
    }
    private var listaRecursosOriginal = listOf<Recurso>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sessionManager = SessionManager(this)
        favoritosManager = FavoritosManager(this)

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
            favoritosIds = favoritosManager.obtenerFavoritos(sessionManager.obtenerIdUsuario()),
            onFavorito = { recurso ->
                alternarFavorito(recurso)
            },
            onEditar = { recurso ->
                val intent = Intent(this, RecursoFormActivity::class.java)

                intent.putExtra("id", recurso.id)
                intent.putExtra("titulo", recurso.titulo)
                intent.putExtra("descripcion", recurso.descripcion)
                intent.putExtra("tipo", recurso.tipo)
                intent.putExtra("enlace", recurso.enlace)
                intent.putExtra("imagen", recurso.imagen)
                intent.putExtra("ratingPromedio", recurso.ratingPromedio)
                intent.putExtra("totalRatings", recurso.totalRatings)

                startActivity(intent)
            },
            onEliminar = { recurso ->
                confirmarEliminacion(recurso)
            }
        )

        recyclerRecursos.layoutManager = LinearLayoutManager(this)
        recyclerRecursos.adapter = adapter

        btnBuscar.setOnClickListener {
            val texto = edtBuscar.text.toString().trim().lowercase()

            if (texto.isEmpty()) {
                adapter.actualizarLista(listaRecursosOriginal)
                return@setOnClickListener
            }

            val filtrados = listaRecursosOriginal.filter { recurso ->
                recurso.id?.lowercase()?.contains(texto) == true ||
                        recurso.titulo.lowercase().contains(texto) ||
                        recurso.tipo.lowercase().contains(texto)
            }

            adapter.actualizarLista(filtrados)

            if (filtrados.isEmpty()) {
                Toast.makeText(this, "No se encontraron recursos.", Toast.LENGTH_SHORT).show()
            }
        }

        btnAgregar.setOnClickListener {
            startActivity(Intent(this, RecursoFormActivity::class.java))
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

        if (::adapter.isInitialized) {
            val progressBar = findViewById<ProgressBar>(R.id.progressBar)
            cargarRecursos(progressBar)
        }
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