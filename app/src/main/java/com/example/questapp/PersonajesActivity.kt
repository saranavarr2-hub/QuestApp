package com.example.questapp

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
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

        val etNombre = findViewById<EditText>(R.id.et_nombre_pj)
        val etLink = findViewById<EditText>(R.id.et_link_pdf)
        val btnGuardar = findViewById<Button>(R.id.btn_guardar_pj)
        val btnVolver = findViewById<Button>(R.id.btn_volver_personajes)
        val rvPersonajes = findViewById<RecyclerView>(R.id.rv_personajes)

        val userId = auth.currentUser?.uid ?: ""

        rvPersonajes.layoutManager = LinearLayoutManager(this)

        // Escuchar cambios en Firestore
        db.collection("personajes")
            .whereEqualTo("userId", userId)
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    Toast.makeText(this, "Error al cargar personajes", Toast.LENGTH_SHORT).show()
                    return@addSnapshotListener
                }

                if (snapshot != null) {
                    val listaPersonajes = snapshot.documents.mapNotNull { doc ->
                        val data = doc.data?.toMutableMap()
                        data?.put("id", doc.id)
                        data
                    }

                    rvPersonajes.adapter = PersonajeAdapter(
                        listaPersonajes,
                        { url -> abrirDocumentoExterno(url) },
                        { id -> mostrarDialogoEliminar(id) }
                    )
                }
            }

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

        btnVolver.setOnClickListener {
            finish()
        }
    }

    private fun mostrarDialogoEliminar(id: String) {
        AlertDialog.Builder(this)
            .setTitle("Eliminar personaje")
            .setMessage("¿Estás seguro de que quieres borrar esta ficha?")
            .setPositiveButton("Eliminar") { _, _ ->
                eliminarPersonaje(id)
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun eliminarPersonaje(id: String) {
        db.collection("personajes").document(id)
            .delete()
            .addOnSuccessListener {
                Toast.makeText(this, "Personaje eliminado", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Error al eliminar", Toast.LENGTH_SHORT).show()
            }
    }

    /**
     * Versión optimizada para Emuladores: Forza el uso del Navegador mediante un Chooser
     */
    private fun abrirDocumentoExterno(url: String?) {
        if (url.isNullOrBlank()) {
            Toast.makeText(this, "El enlace está vacío", Toast.LENGTH_SHORT).show()
            return
        }

        var urlLimpia = url.trim()
        if (!urlLimpia.startsWith("http://") && !urlLimpia.startsWith("https://")) {
            urlLimpia = "https://$urlLimpia"
        }

        // Modo preview para evitar que Drive pida edición
        if (urlLimpia.contains("drive.google.com")) {
            urlLimpia = urlLimpia.replace("/view", "/preview")
                .replace("/edit", "/preview")
        }

        try {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(urlLimpia))

            // 1. Añadimos categoría de navegador
            intent.addCategory(Intent.CATEGORY_BROWSABLE)

            // 2. Creamos un Selector (Chooser)
            // Esto obligará al emulador a preguntarte con qué app abrirlo
            val chooser = Intent.createChooser(intent, "Abrir ficha con:")

            // 3. Importante para evitar crasheos en algunas versiones de Android al usar Chooser
            chooser.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

            startActivity(chooser)
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this, "No se encontró un navegador compatible", Toast.LENGTH_SHORT).show()
        }
    }
}