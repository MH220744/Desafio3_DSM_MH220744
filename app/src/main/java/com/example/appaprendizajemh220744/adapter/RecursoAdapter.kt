package com.example.appaprendizajemh220744.adapter

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.appaprendizajemh220744.R
import com.example.appaprendizajemh220744.model.Recurso

class RecursoAdapter(
    private val recursos: MutableList<Recurso>,
    private val rol: String,
    private var favoritosIds: Set<String>,
    private val onFavorito: (Recurso) -> Unit,
    private val onCalificar: (Recurso) -> Unit,
    private val onEditar: (Recurso) -> Unit,
    private val onEliminar: (Recurso) -> Unit
) : RecyclerView.Adapter<RecursoAdapter.RecursoViewHolder>() {

    class RecursoViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imgRecurso: ImageView = view.findViewById(R.id.imgRecurso)
        val txtTitulo: TextView = view.findViewById(R.id.txtTitulo)
        val txtTipo: TextView = view.findViewById(R.id.txtTipo)
        val txtDescripcion: TextView = view.findViewById(R.id.txtDescripcion)
        val txtEnlace: TextView = view.findViewById(R.id.txtEnlace)
        val txtRating: TextView = view.findViewById(R.id.txtRating)
        val btnFavorito: Button = view.findViewById(R.id.btnFavorito)
        val btnCalificar: Button = view.findViewById(R.id.btnCalificar)
        val layoutBotonesDocente: LinearLayout = view.findViewById(R.id.layoutBotonesDocente)
        val btnEditar: Button = view.findViewById(R.id.btnEditar)
        val btnEliminar: Button = view.findViewById(R.id.btnEliminar)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecursoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_recurso, parent, false)

        return RecursoViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecursoViewHolder, position: Int) {
        val recurso = recursos[position]
        val idRecurso = recurso.id ?: ""
        val context = holder.itemView.context

        holder.txtTitulo.text = recurso.titulo
        holder.txtTipo.text = recurso.tipo
        holder.txtDescripcion.text = recurso.descripcion
        holder.txtEnlace.text = recurso.enlace
        holder.txtRating.text = "⭐ ${String.format("%.1f", recurso.ratingPromedio)} (${recurso.totalRatings})"

        Glide.with(context)
            .load(recurso.imagen)
            .placeholder(R.mipmap.ic_launcher)
            .into(holder.imgRecurso)

        holder.txtEnlace.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(recurso.enlace))
            context.startActivity(intent)
        }

        if (rol == context.getString(R.string.rol_estudiante)) {
            holder.btnFavorito.visibility = View.VISIBLE
            holder.btnCalificar.visibility = View.VISIBLE

            holder.btnFavorito.text = if (favoritosIds.contains(idRecurso)) {
                context.getString(R.string.quitar_favoritos)
            } else {
                context.getString(R.string.agregar_favoritos)
            }
        } else {
            holder.btnFavorito.visibility = View.GONE
            holder.btnCalificar.visibility = View.GONE
        }

        holder.layoutBotonesDocente.visibility = if (rol == context.getString(R.string.rol_docente)) {
            View.VISIBLE
        } else {
            View.GONE
        }

        holder.btnFavorito.setOnClickListener {
            onFavorito(recurso)
        }

        holder.btnCalificar.setOnClickListener {
            onCalificar(recurso)
        }

        holder.btnEditar.setOnClickListener {
            onEditar(recurso)
        }

        holder.btnEliminar.setOnClickListener {
            onEliminar(recurso)
        }
    }

    override fun getItemCount(): Int {
        return recursos.size
    }

    fun actualizarLista(nuevaLista: List<Recurso>) {
        recursos.clear()
        recursos.addAll(nuevaLista)
        notifyDataSetChanged()
    }

    fun actualizarFavoritos(nuevosFavoritos: Set<String>) {
        favoritosIds = nuevosFavoritos
        notifyDataSetChanged()
    }
}