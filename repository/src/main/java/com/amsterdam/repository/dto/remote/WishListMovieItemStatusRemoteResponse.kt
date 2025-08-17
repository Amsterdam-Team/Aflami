package com.amsterdam.repository.dto.remote

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class WishListMovieItemStatusRemoteResponse(
    @SerialName("id") val id: String,
    @SerialName("item_present") val itemPresent: Boolean
)
