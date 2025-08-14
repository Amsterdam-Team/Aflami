package com.amsterdam.repository.dto.remote

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserListDetailsRemoteResponse(
    @SerialName("id") val id: Long,
    @SerialName("name") val name: String,
    @SerialName("description") val description: String,
    @SerialName("item_count") val itemCount: Int,
    @SerialName("favorite_count") val favoriteCount: Int,
    @SerialName("created_by") val createdBy: String,
    @SerialName("items") val items: List<UserListItemRemoteDto>,
    @SerialName("poster_path") val posterPath: String?
)

