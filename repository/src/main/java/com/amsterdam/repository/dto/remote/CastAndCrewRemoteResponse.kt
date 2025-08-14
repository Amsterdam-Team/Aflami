package com.amsterdam.repository.dto.remote

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CastAndCrewRemoteResponse(
    @SerialName("id")
    val id: Long? = null,

    @SerialName("cast")
    val cast: List<CastRemoteDto> = emptyList(),

    @SerialName("crew")
    val crew: List<CrewRemoteDto> = emptyList()
)