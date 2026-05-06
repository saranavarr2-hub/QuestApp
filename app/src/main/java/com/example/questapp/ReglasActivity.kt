package com.example.questapp

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

class ReglasActivity : AppCompatActivity() {

    private lateinit var adapter: ReglaAdapter
    private val db = FirebaseFirestore.getInstance()
    private var listaReglas = mutableListOf<Regla>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reglas)

        val btnVolver = findViewById<Button>(R.id.btnVolverReglas)
        btnVolver.setOnClickListener {
            finish()
        }

        val rvReglas = findViewById<RecyclerView>(R.id.rvReglas)
        rvReglas.layoutManager = LinearLayoutManager(this)

        // ACTUALIZACIÓN: Ahora el adapter recibe dos lambdas (clic y clic largo)
        adapter = ReglaAdapter(
            listaReglas,
            { regla ->
                Toast.makeText(this, "Regla de: ${regla.autor}", Toast.LENGTH_SHORT).show()
            },
            { regla ->
                // Al mantener pulsado, mostramos el diálogo de eliminar
                mostrarDialogoEliminar(regla.id)
            }
        )
        rvReglas.adapter = adapter

        findViewById<FloatingActionButton>(R.id.fabAddRegla).setOnClickListener {
            mostrarDialogoRegla()
        }

        escucharFirestore()
    }

    private fun mostrarDialogoRegla() {
        val builder = AlertDialog.Builder(this)
        val v = layoutInflater.inflate(R.layout.dialogo_regla, null)

        val etTitulo = v.findViewById<EditText>(R.id.etTituloDialogo)
        val etDesc = v.findViewById<EditText>(R.id.etDescDialogo)

        builder.setView(v)
        builder.setTitle("Nueva Regla Rápida")
        builder.setPositiveButton("Guardar") { _, _ ->
            val titulo = etTitulo.text.toString()
            val descripcion = etDesc.text.toString()
            if (titulo.isNotEmpty()) {
                guardarEnFirestore(titulo, descripcion)
            }
        }
        builder.setNegativeButton("Cancelar", null)
        builder.show()
    }

    // NUEVA FUNCIÓN: Diálogo de confirmación para borrar
    private fun mostrarDialogoEliminar(id: String) {
        AlertDialog.Builder(this)
            .setTitle("Eliminar regla")
            .setMessage("¿Quieres borrar esta regla de la lista?")
            .setPositiveButton("Eliminar") { _, _ ->
                eliminarRegla(id)
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    // NUEVA FUNCIÓN: Eliminar de Firestore
    private fun eliminarRegla(id: String) {
        db.collection("reglas_rapidas").document(id)
            .delete()
            .addOnSuccessListener {
                Toast.makeText(this, "Regla eliminada", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error al eliminar: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun guardarEnFirestore(titulo: String, descripcion: String) {
        val usuarioEmail = Firebase.auth.currentUser?.email ?: "Anónimo"

        val nuevaRegla = hashMapOf(
            "titulo" to titulo,
            "descripcion" to descripcion,
            "autor" to usuarioEmail
        )

        db.collection("reglas_rapidas")
            .add(nuevaRegla)
            .addOnSuccessListener {
                Toast.makeText(this, "¡Regla añadida con éxito!", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error al guardar: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun escucharFirestore() {
        db.collection("reglas_rapidas")
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    Toast.makeText(this, "Error al conectar", Toast.LENGTH_SHORT).show()
                    return@addSnapshotListener
                }

                if (snapshot != null) {
                    // ACTUALIZACIÓN: Mapeamos los documentos para incluir el ID de Firestore
                    val nuevasReglas = snapshot.documents.mapNotNull { doc ->
                        val regla = doc.toObject(Regla::class.java)
                        regla?.id = doc.id // Asignamos el ID del documento al objeto Regla
                        regla
                    }
                    adapter.actualizarLista(nuevasReglas)
                }
            }
    }
}