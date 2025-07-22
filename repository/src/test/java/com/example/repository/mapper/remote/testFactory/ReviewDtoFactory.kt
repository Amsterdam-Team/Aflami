package com.example.repository.mapper.remote.testFactory

import com.example.repository.dto.remote.review.AuthorDetailsDto
import com.example.repository.dto.remote.review.ReviewDto
import kotlinx.datetime.Instant

fun createFakeReviewDto(
    author: String = "Sarah",
    username: String = "sarah123",
    rating: Float? = 4.5f,
    content: String = "Great review!",
    avatarPath: String? = "/avatar.png",
    createdAt: Instant = Instant.parse("2024-07-20T12:34:56Z"),
    id: String = "rev123"
): ReviewDto {
    return ReviewDto(
        author = author,
        authorDetails = AuthorDetailsDto(
            username = username,
            rating = rating,
            avatarPath = avatarPath
        ),
        content = content,
        createdAt = createdAt,
        updatedAt = createdAt,
        id = id,
        url = "https://example.com/review"
    )
}

