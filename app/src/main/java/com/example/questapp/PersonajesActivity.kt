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


    private fun abrirDocumentoExterno(url: String?) {
        if (url.isNullOrBlank()) {
            Toast.makeText(this, "El enlace está vacío", Toast.LENGTH_SHORT).show()
            return
        }

        var urlFinal = url.trim()


        if (!urlFinal.startsWith("http")) {
            urlFinal = "https://$urlFinal"
        }


        if (urlFinal.contains("drive.google.com")) {
            urlFinal = when {
                urlFinal.contains("/view") -> urlFinal.replace(Regex("/view.*"), "/preview")
                urlFinal.contains("/edit") -> urlFinal.replace(Regex("/edit.*"), "/preview")
                !urlFinal.endsWith("/preview") -> {
                    if (urlFinal.contains("?")) {
                        urlFinal.substring(0, urlFinal.indexOf("?")).removeSuffix("/") + "/preview"
                    } else {
                        urlFinal.removeSuffix("/") + "/preview"
                    }
                }
                else -> urlFinal
            }
        }

        try {

            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(urlFinal))
            intent.setPackage("com.android.chrome")
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

            try {
                startActivity(intent)
            } catch (e: Exception) {

                val fallbackIntent = Intent(Intent.ACTION_VIEW, Uri.parse(urlFinal))
                startActivity(Intent.createChooser(fallbackIntent, "Abrir PDF con:"))
            }
        } catch (e: Exception) {
            Toast.makeText(this, "Error al intentar abrir el enlace", Toast.LENGTH_SHORT).show()
        }
    }
}