package com.example.questapp

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class partidaactivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_partidaactivity)

        // 1. Referencias a los botones
        val btnVolver = findViewById<Button>(R.id.btn_volver_main)
        val btnVerFichas = findViewById<Button>(R.id.btn_ver_fichas_pdf)

        // 2. Acción para volver al menú principal
        btnVolver.setOnClickListener {
            finish() // Cierra esta pantalla y vuelve a la anterior (MainActivity)
        }

        // 3. Acción para abrir el PDF
        btnVerFichas.setOnClickListener {
            // URL de prueba (puedes cambiarla por tu link de Firebase Storage más tarde)
            val urlPdf = "https://www.wizards.com/dnd/files/QuickStartRules.pdf"

            val intent = Intent(Intent.ACTION_VIEW)
            intent.setDataAndType(Uri.parse(urlPdf), "application/pdf")

            // Esto ayuda a que el PDF se abra correctamente en apps externas
            intent.flags = Intent.FLAG_ACTIVITY_NO_HISTORY
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)

            try {
                startActivity(intent)
            } catch (e: Exception) {
                // Si el emulador o el móvil no tienen lector de PDF instalado
                Toast.makeText(this, "Instala un lector de PDF para ver las fichas", Toast.LENGTH_LONG).show()

                // Opción B: Abrirlo en el navegador si falla lo anterior
                val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(urlPdf))
                startActivity(browserIntent)
            }
        }
    }
}