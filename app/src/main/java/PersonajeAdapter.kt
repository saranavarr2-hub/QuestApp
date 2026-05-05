package com.example.questapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class PersonajeAdapter(
    private val listaPjs: List<Map<String, Any>>,
    private val onPdfClick: (String) -> Unit,
    private val onDeleteClick: (String) -> Unit // <--- CAMBIO 1: Añadir este callback
) : RecyclerView.Adapter<PersonajeAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nombre: TextView = view.findViewById(R.id.tv_nombre_item)
        val btnPdf: Button = view.findViewById(R.id.btn_ver_pdf)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_personaje, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val pj = listaPjs[position]

        // Mostrar nombre
        holder.nombre.text = pj["nombre"]?.toString() ?: "Sin nombre"

        // Configurar clic en el botón PDF
        holder.btnPdf.setOnClickListener {
            val url = pj["linkPdf"]?.toString() ?: ""
            onPdfClick(url)
        }

        // CAMBIO 2: Configurar clic largo en toda la tarjeta para ELIMINAR
        holder.itemView.setOnLongClickListener {
            val id = pj["id"]?.toString() ?: ""
            if (id.isNotEmpty()) {
                onDeleteClick(id) // Llamamos a la función de borrar del Activity
            }
            true // Esto confirma que el clic largo fue procesado
        }
    }

    override fun getItemCount() = listaPjs.size
}