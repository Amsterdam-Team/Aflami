package com.amsterdam.repository.dto.remote

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable(with = RatedSerializer::class)
sealed class Rated {
    @Serializable
    data class RatedValue(@SerialName("value") val value: Float) : Rated()

    @Serializable
    object NotRated : Rated()
}
