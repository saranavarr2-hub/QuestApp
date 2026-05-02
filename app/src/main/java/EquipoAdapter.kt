package com.example.questapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class EquipoAdapter(
    private val listaEquipo: MutableList<ItemEquipo>,
    private val onBorrarClick: (ItemEquipo) -> Unit // Función para borrar
) : RecyclerView.Adapter<EquipoAdapter.EquipoViewHolder>() {

    class EquipoViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvNombre: TextView = view.findViewById(R.id.tvNombreItem)
        val tvEfecto: TextView = view.findViewById(R.id.tvEfectoItem)
        val btnBorrar: ImageButton = view.findViewById(R.id.btnBorrarItem)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EquipoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_tabla_equipo, parent, false)
        return EquipoViewHolder(view)
    }

    override fun onBindViewHolder(holder: EquipoViewHolder, position: Int) {
        val item = listaEquipo[position]
        holder.tvNombre.text = item.nombre
        holder.tvEfecto.text = item.efecto

        // Acción de borrar
        holder.btnBorrar.setOnClickListener { onBorrarClick(item) }
    }

    override fun getItemCount(): Int = listaEquipo.size
}