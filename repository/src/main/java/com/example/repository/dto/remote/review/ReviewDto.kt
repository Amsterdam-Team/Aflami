package com.example.repository.dto.remote.review
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.datetime.Instant


@Serializable
data class ReviewDto(
    val author: String,
    @SerialName("author_details")
    val authorDetails: AuthorDetailsDto,
    val content: String,
    @SerialName("created_at")
    val createdAt: Instant,
    val id: String,
    @SerialName("updated_at")
    val updatedAt: Instant,
    val url: String
)
