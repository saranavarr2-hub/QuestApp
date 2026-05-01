package com.example.questapp

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class PersonajesActivity : AppCompatActivity() {

    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_personajes)

        // 1. Referencias a la interfaz
        val etNombre = findViewById<EditText>(R.id.et_nombre_pj)
        val etLink = findViewById<EditText>(R.id.et_link_pdf)
        val btnGuardar = findViewById<Button>(R.id.btn_guardar_pj)
        val btnVolver = findViewById<Button>(R.id.btn_volver_personajes)
        val rvPersonajes = findViewById<RecyclerView>(R.id.rv_personajes)

        val userId = auth.currentUser?.uid ?: ""

        // 2. Configurar el RecyclerView (Cómo se verá la lista)
        rvPersonajes.layoutManager = LinearLayoutManager(this)

        // 3. LÓGICA DE CARGA EN TIEMPO REAL (SnapshotListener)
        // Este bloque hace que la lista se actualice sola al guardar
        db.collection("personajes")
            .whereEqualTo("userId", userId)
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    Toast.makeText(this, "Error al cargar personajes", Toast.LENGTH_SHORT).show()
                    return@addSnapshotListener
                }

                if (snapshot != null) {
                    val listaPersonajes = snapshot.documents.mapNotNull { it.data }

                    // Conectamos el Adapter con los datos de Firebase
                    rvPersonajes.adapter = PersonajeAdapter(listaPersonajes) { url ->
                        abrirDocumentoExterno(url)
                    }
                }
            }

        // 4. LÓGICA PARA GUARDAR UN NUEVO PERSONAJE
        btnGuardar.setOnClickListener {
            val nombre = etNombre.text.toString().trim()
            val link = etLink.text.toString().trim()

            if (nombre.isNotEmpty() && link.isNotEmpty()) {
                val personaje = mapOf(
                    "nombre" to nombre,
                    "linkPdf" to link,
                    "userId" to userId
                )

                db.collection("personajes").add(personaje)
                    .addOnSuccessListener {
                        Toast.makeText(this, "¡Personaje añadido!", Toast.LENGTH_SHORT).show()
                        etNombre.text.clear()
                        etLink.text.clear()
                    }
                    .addOnFailureListener {
                        Toast.makeText(this, "Error al guardar en la nube", Toast.LENGTH_SHORT).show()
                    }
            } else {
                Toast.makeText(this, "Por favor, rellena ambos campos", Toast.LENGTH_SHORT).show()
            }
        }

        // 5. LÓGICA PARA VOLVER A LA PANTALLA DE RECURSOS
        btnVolver.setOnClickListener {
            finish()
        }
    }

    // Función auxiliar para abrir el navegador con el PDF
    fun abrirDocumentoExterno(url: String) {
        try {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(this, "No se puede abrir el enlace. Verifica el formato.", Toast.LENGTH_SHORT).show()
        }
    }
}