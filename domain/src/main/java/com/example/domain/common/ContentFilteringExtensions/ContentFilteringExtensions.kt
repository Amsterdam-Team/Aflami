package com.example.domain.common.ContentFilteringExtensions

import com.example.entity.common.Categorizable

fun <T> List<T>.filterByCategory(genreId: Int): List<T> where T : Categorizable {
    if (genreId == 0) return this

    return this.filter { item ->
        item.categories.any { it.id == genreId.toLong() }
    }
}