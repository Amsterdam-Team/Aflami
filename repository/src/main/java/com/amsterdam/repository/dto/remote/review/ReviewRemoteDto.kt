package com.amsterdam.repository.dto.remote.review
import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class ReviewRemoteDto(
    @SerialName("author")
    val author: String,
    @SerialName("author_details")
    val authorDetails: AuthorDetailsRemoteDto,
    @SerialName("content")
    val content: String,
    @SerialName("created_at")
    val createdAt: Instant,
    @SerialName("id")
    val id: String,
    @SerialName("updated_at")
    val updatedAt: Instant,
    @SerialName("url")
    val url: String
)
