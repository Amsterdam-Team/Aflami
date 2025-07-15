package com.example.domain.common.ContentFilteringExtensions

import com.example.entity.common.PopularitySortable

fun <T : PopularitySortable> List<T>.sortByPopularityDescending(): List<T> {
    return this.sortedByDescending { it.popularity }
}