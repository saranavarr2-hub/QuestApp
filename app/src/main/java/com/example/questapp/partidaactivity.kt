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


        val btnVolver = findViewById<Button>(R.id.btn_volver_main)
        val btnVerFichas = findViewById<Button>(R.id.btn_ver_fichas_pdf)


        btnVolver.setOnClickListener {
            finish()
        }


        btnVerFichas.setOnClickListener {

            val urlPdf = "https://www.wizards.com/dnd/files/QuickStartRules.pdf"

            val intent = Intent(Intent.ACTION_VIEW)
            intent.setDataAndType(Uri.parse(urlPdf), "application/pdf")


            intent.flags = Intent.FLAG_ACTIVITY_NO_HISTORY
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)

            try {
                startActivity(intent)
            } catch (e: Exception) {

                Toast.makeText(this, "Instala un lector de PDF para ver las fichas", Toast.LENGTH_LONG).show()


                val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(urlPdf))
                startActivity(browserIntent)
            }
        }
    }
}