package com.example.questapp

/**
 * Esta es una "Data Class".
 * En Kotlin, se usa específicamente para clases que solo sirven para portar datos.
 */
data class Regla(
    val id: String = "",         // Para guardar el ID único de Firestore
    val titulo: String = "",     // El título de la regla
    val descripcion: String = "", // El cuerpo o texto de la regla
    val autor: String = ""       // Quién la creó o editó
)