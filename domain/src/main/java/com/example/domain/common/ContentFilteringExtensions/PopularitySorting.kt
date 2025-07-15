package com.example.domain.common.ContentFilteringExtensions

import com.example.entity.common.PopularitySortable

fun <T> List<T>.sortByPopularityDescending(): List<T> where T: PopularitySortable {
    return this.sortedByDescending { it.popularity }
}