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
    private val onDeleteClick: (String) -> Unit
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


        holder.nombre.text = pj["nombre"]?.toString() ?: "Sin nombre"


        holder.btnPdf.setOnClickListener {
            val url = pj["linkPdf"]?.toString() ?: ""
            onPdfClick(url)
        }


        holder.itemView.setOnLongClickListener {
            val id = pj["id"]?.toString() ?: ""
            if (id.isNotEmpty()) {
                onDeleteClick(id)
            }
            true
        }
    }

    override fun getItemCount() = listaPjs.size
}