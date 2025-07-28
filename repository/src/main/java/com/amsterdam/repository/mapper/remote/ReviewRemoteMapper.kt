package com.amsterdam.repository.mapper.remote

import com.amsterdam.entity.Review
import com.amsterdam.repository.dto.remote.review.ReviewDto
import com.amsterdam.repository.mapper.shared.EntityMapper
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import javax.inject.Inject

class ReviewRemoteMapper @Inject constructor(): EntityMapper<ReviewDto, Review> {
    override fun toEntity(dto: ReviewDto): Review {
        return Review(
            id = dto.id.hashCode().toLong(),
            reviewerName = dto.author,
            reviewerUsername = dto.authorDetails.username,
            rating = dto.authorDetails.rating ?: 0f,
            content = dto.content,
            date = dto.createdAt.toLocalDateTime(TimeZone.UTC).date,
            imageUrl = dto.authorDetails.fullAvatarPath.orEmpty()
        )
    }
}