package com.example.repository.dto.remote.review
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.datetime.Instant


@Serializable
data class ReviewDto(
    @SerialName("author")
    val author: String,
    @SerialName("author_details")
    val authorDetails: AuthorDetailsDto,
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
