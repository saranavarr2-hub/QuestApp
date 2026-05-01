package com.example.questapp

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.firestore.FirebaseFirestore
import com.bumptech.glide.Glide

class DetallePartidaActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_detalle_partida)

        // 1. Configuración de márgenes (Sistema)
        val rootLayout = findViewById<android.view.View>(R.id.main)
        ViewCompat.setOnApplyWindowInsetsListener(rootLayout) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // 2. Inicializar base de datos y referencias
        val db = FirebaseFirestore.getInstance()
        val etNombre = findViewById<EditText>(R.id.et_nombre_detalle)
        val etMaster = findViewById<EditText>(R.id.et_master_detalle)
        val etJugadores = findViewById<EditText>(R.id.et_jugadores)
        val etVida = findViewById<EditText>(R.id.et_puntos_vida)
        val etNotas = findViewById<EditText>(R.id.et_notas_partida)
        val etUrlMapa = findViewById<EditText>(R.id.et_url_mapa) // Nuevo
        val ivMapa = findViewById<ImageView>(R.id.iv_mapa_detalle) // Nuevo
        val btnGuardar = findViewById<Button>(R.id.btn_guardar_detalle)

        // 3. Recuperar los datos enviados desde MainActivity (Intent)
        val partidaId = intent.getStringExtra("PARTIDA_ID") ?: ""
        val nombreRecibido = intent.getStringExtra("NOMBRE_PARTIDA")
        val masterRecibido = intent.getStringExtra("MASTER_PARTIDA")

        // 4. Mostrar los datos iniciales
        etNombre.setText(nombreRecibido)
        etMaster.setText(masterRecibido)

        // 5. CARGAR datos desde Firebase (Nube -> App)
        if (partidaId.isNotEmpty()) {
            db.collection("partidas").document(partidaId).get()
                .addOnSuccessListener { doc -> // Usamos 'doc' para evitar confusiones
                    if (doc.exists()) {
                        etNombre.setText(doc.getString("nombre"))
                        etMaster.setText(doc.getString("master"))
                        etJugadores.setText(doc.getString("jugadores"))
                        etVida.setText(doc.getString("vida"))
                        etNotas.setText(doc.getString("notas"))

                        // Cargar URL y mostrar imagen con Glide
                        val urlRecuperada = doc.getString("urlMapa") ?: ""
                        etUrlMapa.setText(urlRecuperada)
                        if (urlRecuperada.isNotEmpty()) {
                            Glide.with(this).load(urlRecuperada).into(ivMapa)
                        }
                    }
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Error al cargar datos", Toast.LENGTH_SHORT).show()
                }
        }

        // 6. Botón para GUARDAR / EDITAR (App -> Nube)
        btnGuardar.setOnClickListener {
            val datosActualizados = mapOf(
                "nombre" to etNombre.text.toString(),
                "master" to etMaster.text.toString(),
                "jugadores" to etJugadores.text.toString(),
                "vida" to etVida.text.toString(),
                "notas" to etNotas.text.toString(),
                "urlMapa" to etUrlMapa.text.toString() // Guardamos la URL
            )

            if (partidaId.isNotEmpty()) {
                db.collection("partidas").document(partidaId)
                    .update(datosActualizados)
                    .addOnSuccessListener {
                        Toast.makeText(this, "¡Ficha actualizada!", Toast.LENGTH_SHORT).show()
                        finish() // Volver a la lista
                    }
                    .addOnFailureListener {
                        Toast.makeText(this, "Error al actualizar", Toast.LENGTH_SHORT).show()
                    }
            } else {
                Toast.makeText(this, "Error: ID no encontrado", Toast.LENGTH_LONG).show()
            }
        }
    }
}