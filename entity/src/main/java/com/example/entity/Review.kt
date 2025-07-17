package com.example.entity

import kotlinx.datetime.LocalDate

data class Review(
    val id : Long,
    val author: String,
    val username: String,
    val rating: Float,
    val content: String,
    val date: LocalDate,
    val imageUrl: String,
)