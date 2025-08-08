package com.amsterdam.viewmodel.shared.mappers

fun Float.toFormattedRating(): String {
    require(this in 0.0..10.0) { "Rating must be between 0 and 10, but was $this" }
    return if (this % 1.0 == 0.0) {
        this.toInt().toString()
    } else {
        String.format("%.1f", this)
    }
}