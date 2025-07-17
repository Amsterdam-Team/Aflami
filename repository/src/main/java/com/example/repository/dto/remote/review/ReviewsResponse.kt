package com.example.repository.dto.remote.review
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ReviewsResponse(
    @SerialName("id")
    val id: Long,
    @SerialName("page")
    val page: Int,
    @SerialName("results")
    val results: List<ReviewDto>,
    @SerialName("total_pages")
    val totalPages: Int,
    @SerialName("total_results")
    val totalResults: Int
)