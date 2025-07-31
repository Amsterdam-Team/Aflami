package com.amsterdam.viewmodel.shared.movieAndSeriseDetails

data class ReviewUiState(
        val author: String = "",
        val username: String = "",
        val rating: String = "",
        val content: String = "",
        val date: String = "",
        val imageUrl: String? = "",
        val isExpanded: Boolean = false,
    )