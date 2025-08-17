package com.amsterdam.repository.mapper

import com.amsterdam.entity.Review
import com.amsterdam.repository.dto.remote.review.AuthorDetailsRemoteDto
import com.amsterdam.repository.dto.remote.review.ReviewRemoteDto
import com.google.common.truth.Truth.assertThat
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import org.junit.jupiter.api.Test

class ReviewMapperTest {
    @Test
    fun `toEntity should map full ReviewRemoteDto to Review entity`() {
        val result = fullReviewDto.toEntity()

        assertThat(result).isEqualTo(expectedFullReview)
    }

    @Test
    fun `toEntity should handle null values and map to default values`() {
        val result = minimalReviewDto.toEntity()

        assertThat(result).isEqualTo(expectedMinimalReview)
    }

    @Test
    fun `toEntityList should map a list of Dtos to a list of Review entities`() {
        val dtoList = listOf(fullReviewDto, minimalReviewDto)
        val result = dtoList.toEntityList()

        assertThat(result).isEqualTo(expectedReviewList)
    }

    @Test
    fun `toEntityList should return an empty list when given an empty list`() {
        val emptyDtoList = emptyList<ReviewRemoteDto>()
        val result = emptyDtoList.toEntityList()

        assertThat(result).isEmpty()
    }

    private val fullReviewDto = ReviewRemoteDto(
        id = "61e3d3c7a0924c0043813136",
        author = "John Doe",
        authorDetails = AuthorDetailsRemoteDto(
            name = "John D.",
            username = "johndoe123",
            rating = 8.5f,
            avatarPath = "/avatar.jpg"
        ),
        content = "This is a great movie!",
        createdAt = Instant.parse("2023-10-26T10:30:00Z"),
        updatedAt = Instant.parse("2023-10-26T11:00:00Z"),
        url = "http://example.com/review1"
    )

    private val minimalReviewDto = ReviewRemoteDto(
        id = "5a9f3b3d92514104f0000e31",
        author = "Jane Smith",
        authorDetails = AuthorDetailsRemoteDto(
            username = "janesmith",
            rating = null,
            avatarPath = null
        ),
        content = "An interesting film.",
        createdAt = Instant.parse("2024-01-15T00:00:00Z"),
        updatedAt = Instant.parse("2024-01-15T00:00:00Z"),
        url = "http://example.com/review2"
    )

    private val expectedFullReview = Review(
        id = "61e3d3c7a0924c0043813136".hashCode().toLong(),
        reviewerName = "John Doe",
        reviewerUsername = "johndoe123",
        rating = 8.5f,
        content = "This is a great movie!",
        date = LocalDate(2023, 10, 26),
        imageUrl = "https://image.tmdb.org/t/p/w500/avatar.jpg"
    )

    private val expectedMinimalReview = Review(
        id = "5a9f3b3d92514104f0000e31".hashCode().toLong(),
        reviewerName = "Jane Smith",
        reviewerUsername = "janesmith",
        rating = 0f,
        content = "An interesting film.",
        date = LocalDate(2024, 1, 15),
        imageUrl = "https://image.tmdb.org/t/p/w500null"
    )

    private val expectedReviewList = listOf(
        expectedFullReview,
        expectedMinimalReview
    )
}