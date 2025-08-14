package com.amsterdam.repository.dto.remote.review
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ReviewsRemoteResponse(
    @SerialName("id")
    val id: Long? = null,
    @SerialName("page")
    val page: Int,
    @SerialName("results")
    val results: List<ReviewRemoteDto>,
    @SerialName("total_pages")
    val totalPages: Int,
    @SerialName("total_results")
    val totalResults: Int
)