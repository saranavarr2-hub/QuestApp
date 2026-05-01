package com.example.questapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ReglaAdapter(
    private var listaReglas: List<Regla>,
    private val onClick: (Regla) -> Unit
) : RecyclerView.Adapter<ReglaAdapter.ReglaViewHolder>() {

    // Esta clase interna busca los componentes del XML item_regla.xml
    class ReglaViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val titulo: TextView = view.findViewById(R.id.tvTitulo)
        val descripcion: TextView = view.findViewById(R.id.tvDescripcion)
        val autor: TextView = view.findViewById(R.id.tvAutor)
    }

    // Aquí se "infla" (se crea) el diseño de la tarjeta
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReglaViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ReglaViewHolder(layoutInflater.inflate(R.layout.item_regla, parent, false))
    }

    // Aquí se le dice qué datos poner en cada tarjeta
    override fun onBindViewHolder(holder: ReglaViewHolder, position: Int) {
        val item = listaReglas[position]
        holder.titulo.text = item.titulo
        holder.descripcion.text = item.descripcion
        holder.autor.text = "Editado por: ${item.autor}"

        // Al hacer clic en la tarjeta, ejecutamos la función que pasamos por parámetro
        holder.itemView.setOnClickListener { onClick(item) }
    }

    override fun getItemCount(): Int = listaReglas.size

    // Función extra para actualizar la lista cuando cambien los datos en Firebase
    fun actualizarLista(nuevaLista: List<Regla>) {
        this.listaReglas = nuevaLista
        notifyDataSetChanged()
    }
}