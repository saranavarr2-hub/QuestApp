package com.example.questapp

data class Partida(
    val id: String = "", // ID único para Firebase
    val nombre: String = "",
    val master: String = "",
    var jugadores: String = "",
    var vida: String = "",
    var notas: String = ""
)
