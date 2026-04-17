package com.example.questapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import kotlin.jvm.java

class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    // 1. Lista donde guardaremos las partidas y el adaptador
    private val listaDePartidas = mutableListOf<Partida>()
    private lateinit var adapter: PartidaAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Inicializar Firebase
        auth = FirebaseAuth.getInstance()
        val user = auth.currentUser

        // 2. Referencias a los componentes del XML
        val tvEmail = findViewById<TextView>(R.id.tv_user_email)
        val rvPartidas = findViewById<RecyclerView>(R.id.rv_partidas)
        val btnCrearPartida = findViewById<Button>(R.id.btn_crear_partida)
        val btnRecursos = findViewById<Button>(R.id.btn_ir_recursos)
        val btnLogout = findViewById<Button>(R.id.btn_logout)

        // 3. Mostrar el email del usuario logueado
        user?.let {
            tvEmail.text = it.email
        }

        // 4. Configurar el RecyclerView con el Adapter ACTUALIZADO
        // Fíjate que ahora pasamos la lista Y las llaves { } con la acción del clic
        adapter = PartidaAdapter(listaDePartidas) { partida ->
            // --- ESTO ES LO QUE PASA AL TOCAR UNA FICHA ---
            val intent = Intent(this, DetallePartidaActivity::class.java)
            intent.putExtra("NOMBRE_PARTIDA", partida.nombre)
            intent.putExtra("MASTER_PARTIDA", partida.master)
            startActivity(intent)
        }

        rvPartidas.adapter = adapter
        rvPartidas.layoutManager = LinearLayoutManager(this)
        // --- LÓGICA DE LOS BOTONES ---

        // Botón para generar una nueva ficha de partida
        btnCrearPartida.setOnClickListener {
            // Creamos una nueva partida con datos automáticos
            val num = listaDePartidas.size + 1
            val nuevaPartida = Partida(
                nombre = "La llamada de Cthulhu #$num",
                master = user?.email?.split("@")?.get(0) ?: "Master"
            )

            // Añadimos a la lista y notificamos al adaptador
            listaDePartidas.add(nuevaPartida)
            adapter.notifyItemInserted(listaDePartidas.size - 1)

            // Hace scroll automático hasta la nueva partida
            rvPartidas.scrollToPosition(listaDePartidas.size - 1)

            Toast.makeText(this, "Partida creada localmente", Toast.LENGTH_SHORT).show()
        }

        // Botón para ir a Recursos
        btnRecursos.setOnClickListener {
            val intent = Intent(this, RecursosActivity::class.java)
            startActivity(intent)
        }

        // Botón para Cerrar Sesión
        btnLogout.setOnClickListener {
            auth.signOut()
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish() // Cierra la MainActivity para que no pueda volver atrás
        }
    }
}