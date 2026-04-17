package com.example.questapp

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class DetallePartidaActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_detalle_partida)

        // 1. Configuración de márgenes para que no choque con la barra de estado/notch
        // Asegúrate de que en tu XML el primer Layout tenga el id: android:id="@+id/main"
        val rootLayout = findViewById<android.view.View>(R.id.main)
        ViewCompat.setOnApplyWindowInsetsListener(rootLayout) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // 2. Referencias a los componentes del XML que creamos antes
        val tvTitulo = findViewById<TextView>(R.id.tv_titulo_detalle)
        val tvMaster = findViewById<TextView>(R.id.tv_master_detalle)
        val btnGuardar = findViewById<Button>(R.id.btn_guardar_detalle)

        // 3. Recuperar los datos enviados desde MainActivity
        val nombreRecibido = intent.getStringExtra("NOMBRE_PARTIDA")
        val masterRecibido = intent.getStringExtra("MASTER_PARTIDA")

        // 4. Mostrar los datos en la interfaz
        tvTitulo.text = nombreRecibido
        tvMaster.text = "Director: $masterRecibido"

        // 5. Configurar el botón de guardar
        btnGuardar.setOnClickListener {
            // Aquí puedes añadir lógica para leer los EditText si quieres
            Toast.makeText(this, "Datos guardados para: $nombreRecibido", Toast.LENGTH_SHORT).show()

            // Regresamos a la pantalla principal
            finish()
        }
    }
}