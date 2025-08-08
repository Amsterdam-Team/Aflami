package com.amsterdam.repository.mapper.remote

import com.amsterdam.repository.dto.remote.review.AuthorDetailsDto
import com.amsterdam.repository.dto.remote.review.ReviewDto
import com.google.common.truth.Truth.assertThat
import kotlinx.datetime.Instant
import org.junit.jupiter.api.Test

class ReviewRemoteMapperTest {

    @Test
    fun `given valid ReviewDto, when toEntity called, then return correct Review`() {
        // Given
        val dto = ReviewDto(
            id = "review123",
            author = "John Doe",
            content = "Great movie!",
            createdAt = Instant.parse("2025-08-08T12:34:56.000Z"),
            authorDetails = AuthorDetailsDto(
                username = "johndoe",
                rating = 4.5f,
            ),
            updatedAt = Instant.parse("2025-08-08T12:34:56.000Z"),
            url = ""
        )

        // When
        val result = dto.toEntity()

        // Then
        assertThat(result.id).isEqualTo(dto.id.hashCode().toLong())
        assertThat(result.reviewerName).isEqualTo("John Doe")
        assertThat(result.reviewerUsername).isEqualTo("johndoe")
        assertThat(result.rating).isEqualTo(4.5f)
        assertThat(result.content).isEqualTo("Great movie!")
        assertThat(result.date.toString()).isEqualTo("2025-08-08")
        assertThat(result.imageUrl).isEqualTo("https://image.tmdb.org/t/p/w500null")
    }
    @Test
    fun `given list of ReviewDto, when toEntityList called, then return list of Review`() {
        // Given
        val dtos = listOf(
            ReviewDto(
                id = "r1",
                author = "Alice",
                content = "Nice!",
                createdAt = Instant.parse("2025-08-08T12:34:56.000Z"),
                authorDetails = AuthorDetailsDto(
                    username = "johndoe",
                    rating = 4.5f,
            ),

                updatedAt = Instant.parse("2025-08-08T12:34:56.000Z"),
                url = ""
            ),

        )

        // When
        val result = dtos.toEntityList()

        // Then
        assertThat(result).hasSize(1)
        assertThat(result[0].reviewerName).isEqualTo("Alice")
    }


}
