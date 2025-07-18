package com.example.repository.mapper.remote

import com.example.domain.mapper.DomainMapper
import  com.example.entity.Review
import com.example.repository.BuildConfig
import com.example.repository.dto.remote.review.ReviewsResponse
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

class ReviewRemoteMapper: DomainMapper<List<Review>, ReviewsResponse> {

    override fun toDomain(dto: ReviewsResponse): List<Review> {
        return dto.results.map { dto ->
            Review(
                id = dto.id.hashCode().toLong(),
                reviewerName = dto.author,
                reviewerUsername = dto.authorDetails.username,
                rating = dto.authorDetails.rating ?: 0f,
                content = dto.content,
                date = dto.createdAt.toLocalDateTime(TimeZone.UTC).date,
                imageUrl = BuildConfig.BASE_IMAGE_URL + dto.authorDetails.avatarPath
            )
        }
    }
}