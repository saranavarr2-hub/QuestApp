package com.example.questapp

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.firestore.FirebaseFirestore

class EquipoActivity : AppCompatActivity() {

    private lateinit var adapter: EquipoAdapter
    private val listaEquipo = mutableListOf<ItemEquipo>()
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_equipo)


        val rvEquipo = findViewById<RecyclerView>(R.id.rvEquipo)
        rvEquipo.layoutManager = LinearLayoutManager(this)


        adapter = EquipoAdapter(listaEquipo) { item ->
            borrarDeFirebase(item)
        }
        rvEquipo.adapter = adapter


        val fabAdd = findViewById<FloatingActionButton>(R.id.fabAddEquipo)
        fabAdd.setOnClickListener {
            mostrarDialogoAgregar()
        }


        findViewById<Button>(R.id.btnVolverEquipo).setOnClickListener {
            finish()
        }


        escucharFirestore()
    }

    private fun escucharFirestore() {
        db.collection("equipo")
            .addSnapshotListener { snapshot, e ->
                if (e != null) return@addSnapshotListener

                if (snapshot != null) {
                    listaEquipo.clear()
                    for (doc in snapshot) {
                        val item = doc.toObject(ItemEquipo::class.java)
                        listaEquipo.add(item)
                    }
                    adapter.notifyDataSetChanged()
                }
            }
    }

    private fun mostrarDialogoAgregar() {
        val builder = AlertDialog.Builder(this)
        val view = layoutInflater.inflate(R.layout.dialog_add_equipo, null)

        val etNombre = view.findViewById<EditText>(R.id.etNombreItem)
        val etEfecto = view.findViewById<EditText>(R.id.etEfectoItem)
        val rgTipo = view.findViewById<RadioGroup>(R.id.rgTipo)

        builder.setView(view)
        builder.setPositiveButton("Guardar") { _, _ ->
            val nombre = etNombre.text.toString()
            val efecto = etEfecto.text.toString()
            val tipo = if (rgTipo.checkedRadioButtonId == R.id.rbArma) "Arma" else "Hechizo"

            if (nombre.isNotEmpty() && efecto.isNotEmpty()) {
                val id = db.collection("equipo").document().id
                val nuevoItem = ItemEquipo(id, nombre, efecto, tipo)
                db.collection("equipo").document(id).set(nuevoItem)
            }
        }
        builder.setNegativeButton("Cancelar", null)
        builder.show()
    }

    private fun borrarDeFirebase(item: ItemEquipo) {
        db.collection("equipo").document(item.id).delete()
            .addOnSuccessListener {
                Toast.makeText(this, "${item.nombre} eliminado", Toast.LENGTH_SHORT).show()
            }
    }
}