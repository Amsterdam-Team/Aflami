package com.example.domain.common.ContentFilteringExtensions

import com.example.entity.common.RatingFilterable
import kotlin.math.roundToInt

fun <T> List<T>.filterByMinRating(minRating: Int): List<T> where T : RatingFilterable {
    return this.filter { item -> item.rating.roundToInt() >= minRating }
}