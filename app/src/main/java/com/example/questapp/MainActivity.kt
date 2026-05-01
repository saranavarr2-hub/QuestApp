package com.example.questapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog // Para el diálogo de confirmación
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private val db = FirebaseFirestore.getInstance()

    private val listaDePartidas = mutableListOf<Partida>()
    private lateinit var adapter: PartidaAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        auth = FirebaseAuth.getInstance()
        val user = auth.currentUser

        val tvEmail = findViewById<TextView>(R.id.tv_user_email)
        val rvPartidas = findViewById<RecyclerView>(R.id.rv_partidas)
        val btnCrearPartida = findViewById<Button>(R.id.btn_crear_partida)
        val btnRecursos = findViewById<Button>(R.id.btn_ir_recursos)
        val btnLogout = findViewById<Button>(R.id.btn_logout)

        user?.let {
            tvEmail.text = it.email
        }

        // --- CONFIGURACIÓN DEL ADAPTADOR CON CLIC Y CLIC LARGO ---
        adapter = PartidaAdapter(
            listaDePartidas,
            onItemClick = { partida ->
                // Acción al pulsar normal: Ir a detalles
                val intent = Intent(this, DetallePartidaActivity::class.java)
                intent.putExtra("PARTIDA_ID", partida.id)
                intent.putExtra("NOMBRE_PARTIDA", partida.nombre)
                intent.putExtra("MASTER_PARTIDA", partida.master)
                startActivity(intent)
            },
            onItemLongClick = { partida ->
                // Acción al mantener pulsado: Borrar partida
                mostrarDialogoEliminar(partida)
            }
        )

        rvPartidas.adapter = adapter
        rvPartidas.layoutManager = LinearLayoutManager(this)

        cargarPartidasDesdeFirebase()

        btnCrearPartida.setOnClickListener {
            val nuevaPartidaRef = db.collection("partidas").document()
            val nuevoId = nuevaPartidaRef.id

            val num = listaDePartidas.size + 1
            val nuevaPartida = Partida(
                id = nuevoId,
                nombre = "La llamada de Cthulhu #$num",
                master = user?.email?.split("@")?.get(0) ?: "Master"
            )

            nuevaPartidaRef.set(nuevaPartida)
                .addOnSuccessListener {
                    listaDePartidas.add(nuevaPartida)
                    adapter.notifyItemInserted(listaDePartidas.size - 1)
                    rvPartidas.scrollToPosition(listaDePartidas.size - 1)
                    Toast.makeText(this, "Partida creada en la nube", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                }
        }

        btnRecursos.setOnClickListener {
            startActivity(Intent(this, RecursosActivity::class.java))
        }

        btnLogout.setOnClickListener {
            auth.signOut()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }

    private fun cargarPartidasDesdeFirebase() {
        db.collection("partidas")
            .get()
            .addOnSuccessListener { result ->
                listaDePartidas.clear()
                for (document in result) {
                    val partida = document.toObject(Partida::class.java)
                    val partidaConId = partida.copy(id = document.id)
                    listaDePartidas.add(partidaConId)
                }
                adapter.notifyDataSetChanged()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Error al cargar datos", Toast.LENGTH_SHORT).show()
            }
    }

    // --- FUNCIÓN PARA MOSTRAR EL DIÁLOGO DE ELIMINAR ---
    private fun mostrarDialogoEliminar(partida: Partida) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("¿Eliminar partida?")
        builder.setMessage("¿Estás seguro de que quieres borrar '${partida.nombre}'? No podrás recuperarla.")

        builder.setPositiveButton("Eliminar") { _, _ ->
            db.collection("partidas").document(partida.id)
                .delete()
                .addOnSuccessListener {
                    Toast.makeText(this, "Partida eliminada", Toast.LENGTH_SHORT).show()
                    cargarPartidasDesdeFirebase() // Refrescamos la lista
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "No se pudo eliminar: ${e.message}", Toast.LENGTH_SHORT).show()
                }
        }

        builder.setNegativeButton("Cancelar", null)
        builder.show()
    }

    override fun onResume() {
        super.onResume()
        cargarPartidasDesdeFirebase()
    }
}