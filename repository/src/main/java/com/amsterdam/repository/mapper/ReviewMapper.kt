package com.amsterdam.repository.mapper

import com.amsterdam.entity.Review
import com.amsterdam.repository.dto.remote.review.ReviewRemoteDto
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

fun ReviewRemoteDto.toEntity(): Review {
    return Review(
        id = id.hashCode().toLong(),
        reviewerName = author,
        reviewerUsername = authorDetails.username,
        rating = authorDetails.rating ?: 0f,
        content = content,
        date = createdAt.toLocalDateTime(TimeZone.UTC).date,
        imageUrl = authorDetails.fullAvatarPath.orEmpty()
    )
}

fun List<ReviewRemoteDto>.toEntityList(): List<Review> = map { it.toEntity() }