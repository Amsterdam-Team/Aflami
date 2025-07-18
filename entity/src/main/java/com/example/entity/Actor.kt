package com.example.entity

data class Actor(
    val id: Long,
    val name: String,
    val imageUrl: String,
    val popularity: Double,
    val gender: Gender
)

enum class Gender {
    Male,
    Female,
}