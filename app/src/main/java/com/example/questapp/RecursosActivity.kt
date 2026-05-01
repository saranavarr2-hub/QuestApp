package com.example.questapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class RecursosActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recursos)

        // 1. Referencias a los botones mediante sus IDs del XML
        val btnReglas = findViewById<Button>(R.id.btn_reglas_rapidas)
        val btnVolver = findViewById<Button>(R.id.btn_volver_recursos)
        val btnPersonajes = findViewById<Button>(R.id.btn_ir_personajes)

        // 2. Acción para el botón de Personajes
        btnPersonajes.setOnClickListener {
            val intent = Intent(this, PersonajesActivity::class.java)
            startActivity(intent)
        }

        // 3. Acción para el botón de Reglas Rápidas (CAMBIO REALIZADO AQUÍ)
        btnReglas.setOnClickListener {
            Toast.makeText(this, "Abriendo manual de reglas...", Toast.LENGTH_SHORT).show()

            // Creamos el Intent para navegar a la nueva Activity que creamos
            val intent = Intent(this, ReglasActivity::class.java)
            startActivity(intent)
        }

        // 4. Botón para regresar a la pantalla anterior
        btnVolver.setOnClickListener {
            finish()
        }
    }
}