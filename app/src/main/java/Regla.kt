package com.example.questapp

data class Regla(
    var id: String = "",         // <-- AÑADE ESTA LÍNEA (debe ser var)
    val titulo: String = "",
    val descripcion: String = "",
    val autor: String = ""
)