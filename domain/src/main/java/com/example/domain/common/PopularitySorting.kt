package com.example.domain.common

import com.example.entity.common.PopularitySortable

fun <T : PopularitySortable> List<T>.sortByPopularityDescending(): List<T> {
    return this.sortedByDescending { it.popularity }
}