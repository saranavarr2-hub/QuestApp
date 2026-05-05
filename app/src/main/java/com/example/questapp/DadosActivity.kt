package com.example.questapp

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class DadosActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dados)

        val tvResultado = findViewById<TextView>(R.id.tvResultadoDado)
        val tvInfo = findViewById<TextView>(R.id.tvTipoDado)
        val btnVolver = findViewById<Button>(R.id.btnVolverDados)

        fun lanzar(caras: Int) {
            val resultado = (1..caras).random()
            tvResultado.text = resultado.toString()
            tvInfo.text = "Lanzaste un D$caras"
        }

        //botones de texto
        findViewById<Button>(R.id.btnD4).setOnClickListener { lanzar(4) }
        findViewById<Button>(R.id.btnD6).setOnClickListener { lanzar(6) }
        findViewById<Button>(R.id.btnD8).setOnClickListener { lanzar(8) }
        findViewById<Button>(R.id.btnD10).setOnClickListener { lanzar(10) }
        findViewById<Button>(R.id.btnD12).setOnClickListener { lanzar(12) }
        findViewById<Button>(R.id.btnD20).setOnClickListener { lanzar(20) }

        btnVolver.setOnClickListener { finish() }
    }
}