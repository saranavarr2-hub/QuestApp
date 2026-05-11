package com.example.questapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ReglaAdapter(
    private var listaReglas: List<Regla>,
    private val onClick: (Regla) -> Unit,
    private val onLongClick: (Regla) -> Unit // <--- Añadimos esto para borrar
) : RecyclerView.Adapter<ReglaAdapter.ReglaViewHolder>() {

    class ReglaViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val titulo: TextView = view.findViewById(R.id.tvTitulo)
        val descripcion: TextView = view.findViewById(R.id.tvDescripcion)
        val autor: TextView = view.findViewById(R.id.tvAutor)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReglaViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ReglaViewHolder(layoutInflater.inflate(R.layout.item_regla, parent, false))
    }

    override fun onBindViewHolder(holder: ReglaViewHolder, position: Int) {
        val item = listaReglas[position]
        holder.titulo.text = item.titulo
        holder.descripcion.text = item.descripcion
        holder.autor.text = "Editado por: ${item.autor}"


        holder.itemView.setOnClickListener { onClick(item) }


        holder.itemView.setOnLongClickListener {
            onLongClick(item)
            true
        }
    }

    override fun getItemCount(): Int = listaReglas.size

    fun actualizarLista(nuevaLista: List<Regla>) {
        this.listaReglas = nuevaLista
        notifyDataSetChanged()
    }
}