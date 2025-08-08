package com.amsterdam.repository.mapper.remote

import com.amsterdam.repository.mapper.remote.testFactory.createFakeReviewDto
import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test

class ReviewRemoteMapperTest {

    private val dto = createFakeReviewDto()

    @Test
    fun `toEntity should map id correctly`() {
        val result = dto.toEntity()
        assertThat(result.id).isEqualTo(dto.id.hashCode().toLong())
    }

    @Test
    fun `toEntity should map reviewerName correctly`() {
        val result = dto.toEntity()
        assertThat(result.reviewerName).isEqualTo(dto.author)
    }

    @Test
    fun `toEntity should map reviewerUsername correctly`() {
        val result = dto.toEntity()
        assertThat(result.reviewerUsername).isEqualTo(dto.authorDetails.username)
    }

    @Test
    fun `toEntity should map rating correctly`() {
        val result = dto.toEntity()
        assertThat(result.rating).isEqualTo(dto.authorDetails.rating)
    }

    @Test
    fun `toEntity should map content correctly`() {
        val result = dto.toEntity()
        assertThat(result.content).isEqualTo(dto.content)
    }

    @Test
    fun `toEntity should map date correctly`() {
        val result = dto.toEntity()
        assertThat(result.date.toString()).isEqualTo("2024-07-20")
    }

    @Test
    fun `toEntity should map imageUrl correctly`() {
        val result = dto.toEntity()
        assertThat(result.imageUrl).endsWith("/avatar.png")
    }

    @Test
    fun `toEntity should return 0f rating when rating is null`() {
        val dtoWithNullRating = createFakeReviewDto(rating = null)
        val result = dtoWithNullRating.toEntity()
        assertThat(result.rating).isEqualTo(0f)
    }
}
