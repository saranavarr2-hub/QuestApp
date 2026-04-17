package com.example.questapp

import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class RecursosActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recursos)

        val btnReglas = findViewById<Button>(R.id.btn_reglas_rapidas)
        val btnVolver = findViewById<Button>(R.id.btn_volver_recursos)

        btnReglas.setOnClickListener {
            // Aquí podrías abrir otra pantalla con texto o un PDF
            Toast.makeText(this, "Abriendo manual de reglas...", Toast.LENGTH_SHORT).show()
        }

        btnVolver.setOnClickListener {
            finish()
        }
    }
}