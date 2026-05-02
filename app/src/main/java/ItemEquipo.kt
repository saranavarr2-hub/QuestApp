package com.example.questapp // Asegúrate de que este sea tu paquete real

data class ItemEquipo(
    val id: String = "",       // ID único que genera Firebase
    val nombre: String = "",   // Ej: "Espada de plata" o "Hechizo de curación"
    val efecto: String = "",   // Ej: "1d8+2" o "Cura 2d4"
    val tipo: String = ""      // Usaremos "Arma" o "Hechizo" para filtrar después
)