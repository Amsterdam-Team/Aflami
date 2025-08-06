package com.amsterdam.repository.dto.remote

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RemoteAccountStatesDto(
    @SerialName("favorite") val favorite: Boolean = false,
    @SerialName("watchlist") val watchlist: Boolean = false,
    @SerialName("rated") val rated: Rated = Rated.NotRated
)