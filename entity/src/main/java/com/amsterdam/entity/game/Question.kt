package com.amsterdam.entity.game


sealed class Question <T>(
    open val options: List<T>,
    open val correctOption: T,
) {
    data class ImageQuestion <T>(
        val imageUrl: String,
        override val options: List<T>,
        override val correctOption: T,
    ): Question<T>(options, correctOption)

    data class TextQuestion <T>(
        val textQuestion: String,
        override val options: List<T>,
        override val correctOption: T,
    ): Question<T>(options, correctOption)
}