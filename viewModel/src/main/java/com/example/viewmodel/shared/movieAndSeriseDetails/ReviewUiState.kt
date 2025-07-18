package com.example.viewmodel.shared.movieAndSeriseDetails

data class ReviewUiState(
        val author: String = "",
        val username: String = "",
        val rating: String = "",
        val content: String = "",
        val date: String = "",
        val imageUrl: String? = "",
    )