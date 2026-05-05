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


        val btnReglas = findViewById<Button>(R.id.btn_reglas_rapidas)
        val btnVolver = findViewById<Button>(R.id.btn_volver_recursos)
        val btnPersonajes = findViewById<Button>(R.id.btn_ir_personajes)
        val btnDados = findViewById<Button>(R.id.btn_dados)


        val btnEquipo = findViewById<Button>(R.id.btn_ir_equipo)


        btnPersonajes.setOnClickListener {
            val intent = Intent(this, PersonajesActivity::class.java)
            startActivity(intent)
        }


        btnReglas.setOnClickListener {
            Toast.makeText(this, "Abriendo manual de reglas...", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, ReglasActivity::class.java)
            startActivity(intent)
        }


        btnDados.setOnClickListener {
            Toast.makeText(this, "Preparando los dados...", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, DadosActivity::class.java)
            startActivity(intent)
        }


        btnEquipo.setOnClickListener {
            Toast.makeText(this, "Cargando Hechizos y Armas...", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, EquipoActivity::class.java)
            startActivity(intent)
        }


        btnVolver.setOnClickListener {
            finish()
        }
    }
}