package com.example.repository.dto.remote

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RemoteCastAndCrewResponse(
    @SerialName("id")
    val id: Int,
    
    @SerialName("cast")
    val cast: List<RemoteCastDto> = emptyList(),
    
    @SerialName("crew")
    val crew: List<RemoteCrewDto> = emptyList()
)