package com.example.viewmodel.entityHelper

import com.example.entity.Category
import com.example.entity.Movie

fun createMovie(
    id: Long = 1L,
    name: String = "Default Movie",
    description: String = "A sample movie description.",
    poster: String = "https://example.com/poster.jpg",
    productionYear: Int = 2024,
    categories: List<Category> = listOf(),
    rating: Float = 4.5f
): Movie {
    return Movie(
        id = id,
        name = name,
        description = description,
        poster = poster,
        productionYear = productionYear,
        categories = categories,
        rating = rating
    )
}