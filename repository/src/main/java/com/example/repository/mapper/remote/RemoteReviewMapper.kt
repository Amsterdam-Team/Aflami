package com.example.repository.mapper.remote

import  com.example.entity.Review
import com.example.repository.BuildConfig
import com.example.repository.dto.remote.review.ReviewsResponse
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

class RemoteReviewMapper {

    fun mapResponseToDomain(response: ReviewsResponse): List<Review> {
        return response.results.map { dto ->
            Review(
                id = dto.id.hashCode().toLong(),
                author = dto.author,
                username = dto.authorDetails.username,
                rating = dto.authorDetails.rating ?: 0f,
                content = dto.content,
                date = dto.createdAt.toLocalDateTime(TimeZone.UTC).date,
                imageUrl = BuildConfig.BASE_IMAGE_URL + dto.authorDetails.avatarPath
            )
        }

    }
}