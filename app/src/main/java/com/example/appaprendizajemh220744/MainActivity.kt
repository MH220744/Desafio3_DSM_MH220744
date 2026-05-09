package com.example.appaprendizajemh220744

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
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
import com.example.appaprendizajemh220744.storage.FavoritosManager
import com.example.appaprendizajemh220744.storage.SessionManager

class MainActivity : AppCompatActivity() {

    private val recursoController = RecursoController()
    private lateinit var sessionManager: SessionManager
    private lateinit var favoritosManager: FavoritosManager
    private lateinit var adapter: RecursoAdapter

    private var listaRecursosOriginal = listOf<Recurso>()
    private var mostrandoFavoritos = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sessionManager = SessionManager(this)
        favoritosManager = FavoritosManager(this)

        val recyclerRecursos = findViewById<RecyclerView>(R.id.recyclerRecursos)
        val edtBuscar = findViewById<EditText>(R.id.edtBuscar)
        val btnBuscar = findViewById<Button>(R.id.btnBuscar)
        val btnAgregar = findViewById<Button>(R.id.btnAgregar)
        val btnVerFavoritos = findViewById<Button>(R.id.btnVerFavoritos)
        val btnLogout = findViewById<Button>(R.id.btnLogout)
        val progressBar = findViewById<ProgressBar>(R.id.progressBar)

        val rol = sessionManager.obtenerRol()

        btnAgregar.visibility = if (rol == getString(R.string.rol_docente)) View.VISIBLE else View.GONE
        btnVerFavoritos.visibility = if (rol == getString(R.string.rol_estudiante)) View.VISIBLE else View.GONE

        adapter = RecursoAdapter(
            recursos = mutableListOf(),
            rol = rol,
            favoritosIds = favoritosManager.obtenerFavoritos(sessionManager.obtenerIdUsuario()),
            onFavorito = { recurso ->
                alternarFavorito(recurso)
            },
            onCalificar = { recurso ->
                mostrarDialogoCalificacion(recurso)
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

        edtBuscar.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                s: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {
                // No se necesita.
            }

            override fun onTextChanged(
                s: CharSequence?,
                start: Int,
                before: Int,
                count: Int
            ) {
                if (mostrandoFavoritos) {
                    mostrarSoloFavoritos(s.toString())
                } else {
                    filtrarRecursos(s.toString())
                }
            }

            override fun afterTextChanged(s: Editable?) {
                // No se necesita.
            }
        })

        btnBuscar.setOnClickListener {
            if (mostrandoFavoritos) {
                mostrarSoloFavoritos(edtBuscar.text.toString())
            } else {
                filtrarRecursos(edtBuscar.text.toString())
            }
        }

        btnVerFavoritos.setOnClickListener {
            mostrandoFavoritos = !mostrandoFavoritos

            if (mostrandoFavoritos) {
                btnVerFavoritos.text = getString(R.string.ver_todos)
                mostrarSoloFavoritos(edtBuscar.text.toString())
            } else {
                btnVerFavoritos.text = getString(R.string.ver_favoritos)
                filtrarRecursos(edtBuscar.text.toString())
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
            cargarRecursos(findViewById(R.id.progressBar))
        }
    }

    private fun cargarRecursos(progressBar: ProgressBar) {
        progressBar.visibility = View.VISIBLE

        recursoController.obtenerRecursos(
            onSuccess = { recursos ->
                progressBar.visibility = View.GONE
                listaRecursosOriginal = recursos

                adapter.actualizarFavoritos(
                    favoritosManager.obtenerFavoritos(sessionManager.obtenerIdUsuario())
                )

                val textoBusqueda = findViewById<EditText>(R.id.edtBuscar).text.toString()

                if (mostrandoFavoritos) {
                    mostrarSoloFavoritos(textoBusqueda)
                } else {
                    filtrarRecursos(textoBusqueda)
                }
            },
            onError = { mensaje ->
                progressBar.visibility = View.GONE
                Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show()
            }
        )
    }

    private fun filtrarRecursos(textoBusqueda: String) {
        val texto = textoBusqueda.trim().lowercase()

        if (texto.isEmpty()) {
            adapter.actualizarLista(listaRecursosOriginal)
            return
        }

        val filtrados = listaRecursosOriginal.filter { recurso ->
            recurso.id?.lowercase()?.contains(texto) == true ||
                    recurso.titulo.lowercase().contains(texto) ||
                    recurso.tipo.lowercase().contains(texto)
        }

        adapter.actualizarLista(filtrados)
    }

    private fun mostrarSoloFavoritos(textoBusqueda: String = "") {
        val idUsuario = sessionManager.obtenerIdUsuario()

        if (idUsuario.isEmpty()) {
            Toast.makeText(this, getString(R.string.no_usuario_actual), Toast.LENGTH_SHORT).show()
            return
        }

        val favoritosIds = favoritosManager.obtenerFavoritos(idUsuario)

        var favoritos = listaRecursosOriginal.filter { recurso ->
            favoritosIds.contains(recurso.id)
        }

        val texto = textoBusqueda.trim().lowercase()

        if (texto.isNotEmpty()) {
            favoritos = favoritos.filter { recurso ->
                recurso.id?.lowercase()?.contains(texto) == true ||
                        recurso.titulo.lowercase().contains(texto) ||
                        recurso.tipo.lowercase().contains(texto)
            }
        }

        adapter.actualizarFavoritos(favoritosIds)
        adapter.actualizarLista(favoritos)

        if (favoritos.isEmpty()) {
            Toast.makeText(this, getString(R.string.sin_favoritos), Toast.LENGTH_SHORT).show()
        }
    }

    private fun alternarFavorito(recurso: Recurso) {
        val idUsuario = sessionManager.obtenerIdUsuario()
        val idRecurso = recurso.id

        if (idUsuario.isEmpty()) {
            Toast.makeText(this, getString(R.string.no_usuario_actual), Toast.LENGTH_SHORT).show()
            return
        }

        if (idRecurso.isNullOrEmpty()) {
            Toast.makeText(this, getString(R.string.recurso_sin_id), Toast.LENGTH_SHORT).show()
            return
        }

        if (favoritosManager.esFavorito(idUsuario, idRecurso)) {
            favoritosManager.quitarFavorito(idUsuario, idRecurso)
            Toast.makeText(this, getString(R.string.favorito_quitado), Toast.LENGTH_SHORT).show()
        } else {
            favoritosManager.agregarFavorito(idUsuario, idRecurso)
            Toast.makeText(this, getString(R.string.favorito_agregado), Toast.LENGTH_SHORT).show()
        }

        val favoritosActualizados = favoritosManager.obtenerFavoritos(idUsuario)
        adapter.actualizarFavoritos(favoritosActualizados)

        if (mostrandoFavoritos) {
            mostrarSoloFavoritos(findViewById<EditText>(R.id.edtBuscar).text.toString())
        } else {
            filtrarRecursos(findViewById<EditText>(R.id.edtBuscar).text.toString())
        }
    }

    private fun mostrarDialogoCalificacion(recurso: Recurso) {
        val opciones = arrayOf(
            getString(R.string.una_estrella),
            getString(R.string.dos_estrellas),
            getString(R.string.tres_estrellas),
            getString(R.string.cuatro_estrellas),
            getString(R.string.cinco_estrellas)
        )

        var calificacionSeleccionada = 5

        AlertDialog.Builder(this)
            .setTitle(getString(R.string.calificar_titulo))
            .setSingleChoiceItems(opciones, 4) { _, which ->
                calificacionSeleccionada = which + 1
            }
            .setPositiveButton(getString(R.string.guardar)) { _, _ ->
                guardarCalificacion(recurso, calificacionSeleccionada)
            }
            .setNegativeButton(getString(R.string.cancelar), null)
            .show()
    }

    private fun guardarCalificacion(recurso: Recurso, nuevaCalificacion: Int) {
        val idRecurso = recurso.id

        if (idRecurso.isNullOrEmpty()) {
            Toast.makeText(this, getString(R.string.recurso_sin_id), Toast.LENGTH_SHORT).show()
            return
        }

        val totalActual = recurso.totalRatings
        val promedioActual = recurso.ratingPromedio

        val nuevoTotal = totalActual + 1
        val nuevoPromedio = ((promedioActual * totalActual) + nuevaCalificacion) / nuevoTotal

        val recursoActualizado = Recurso(
            id = recurso.id,
            titulo = recurso.titulo,
            descripcion = recurso.descripcion,
            tipo = recurso.tipo,
            enlace = recurso.enlace,
            imagen = recurso.imagen,
            ratingPromedio = nuevoPromedio,
            totalRatings = nuevoTotal
        )

        recursoController.actualizarRecurso(
            id = idRecurso,
            recurso = recursoActualizado,
            onSuccess = {
                Toast.makeText(this, getString(R.string.calificacion_guardada), Toast.LENGTH_SHORT).show()
                cargarRecursos(findViewById(R.id.progressBar))
            },
            onError = { mensaje ->
                Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show()
            }
        )
    }

    private fun confirmarEliminacion(recurso: Recurso) {
        AlertDialog.Builder(this)
            .setTitle(getString(R.string.eliminar_recurso))
            .setMessage(getString(R.string.confirmar_eliminar_recurso, recurso.titulo))
            .setPositiveButton(getString(R.string.eliminar)) { _, _ ->
                recursoController.eliminarRecurso(
                    id = recurso.id ?: "",
                    onSuccess = {
                        Toast.makeText(this, getString(R.string.recurso_eliminado), Toast.LENGTH_SHORT).show()
                        cargarRecursos(findViewById(R.id.progressBar))
                    },
                    onError = { mensaje ->
                        Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show()
                    }
                )
            }
            .setNegativeButton(getString(R.string.cancelar), null)
            .show()
    }
}