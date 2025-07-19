package com.example.repository.mapper.remote

import com.example.entity.Review
import com.example.repository.dto.remote.review.ReviewDto
import com.example.repository.mapper.shared.EntityMapper
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

class ReviewRemoteMapper : EntityMapper<ReviewDto, Review> {
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