package com.example.questapp

import android.os.Bundle
import android.widget.Button // Importamos el botón
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

        // 1. Configurar el botón de Volver
        val btnVolver = findViewById<Button>(R.id.btnVolverReglas)
        btnVolver.setOnClickListener {
            finish() // Cierra esta Activity y regresa a RecursosActivity
        }

        // 2. Configurar el RecyclerView
        val rvReglas = findViewById<RecyclerView>(R.id.rvReglas)
        rvReglas.layoutManager = LinearLayoutManager(this)

        // Inicializamos el adaptador
        adapter = ReglaAdapter(listaReglas) { regla ->
            Toast.makeText(this, "Regla de: ${regla.autor}", Toast.LENGTH_SHORT).show()
        }
        rvReglas.adapter = adapter

        // 3. Configurar el botón flotante para abrir el diálogo
        findViewById<FloatingActionButton>(R.id.fabAddRegla).setOnClickListener {
            mostrarDialogoRegla()
        }

        // 4. Escuchar cambios en Firestore en tiempo real
        escucharFirestore()
    }

    // Función para mostrar el cuadro de diálogo
    private fun mostrarDialogoRegla() {
        val builder = AlertDialog.Builder(this)
        val v = layoutInflater.inflate(com.example.questapp.R.layout.dialogo_regla, null)

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


    // Función para subir los datos a Firebase
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
                    val nuevasReglas = snapshot.toObjects(Regla::class.java)
                    adapter.actualizarLista(nuevasReglas)
                }
            }
    }
}