package com.example.questapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

// Añadimos "onItemClick" en el constructor para manejar los clics
class PartidaAdapter(
    private val listaPartidas: List<Partida>,
    private val onItemClick: (Partida) -> Unit
) : RecyclerView.Adapter<PartidaAdapter.PartidaViewHolder>() {

    class PartidaViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nombre: TextView = view.findViewById(R.id.tv_nombre_partida)
        val master: TextView = view.findViewById(R.id.tv_master_partida)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PartidaViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_partida, parent, false)
        return PartidaViewHolder(view)
    }

    override fun onBindViewHolder(holder: PartidaViewHolder, position: Int) {
        val partida = listaPartidas[position]
        holder.nombre.text = partida.nombre
        holder.master.text = "Master: ${partida.master}"

        // --- AQUÍ SE DETECTA EL CLIC ---
        holder.itemView.setOnClickListener {
            onItemClick(partida) // Ejecuta la función que le pasaremos desde el Main
        }
    }

    override fun getItemCount() = listaPartidas.size
}