//package com.amsterdam.repository.mapper.remote
//
//import com.amsterdam.repository.mapper.remote.testFactory.createFakeReviewDto
//import com.google.common.truth.Truth.assertThat
//import org.junit.jupiter.api.Test
//
//class ReviewRemoteMapperTest {
//
//    private val mapper = ReviewRemoteMapper()
//    private val dto = createFakeReviewDto()
//
//    @Test
//    fun `toEntity should map id correctly`() {
//        val result = mapper.toEntity(dto)
//        assertThat(result.id).isEqualTo(dto.id.hashCode().toLong())
//    }
//
//    @Test
//    fun `toEntity should map reviewerName correctly`() {
//        val result = mapper.toEntity(dto)
//        assertThat(result.reviewerName).isEqualTo(dto.author)
//    }
//
//    @Test
//    fun `toEntity should map reviewerUsername correctly`() {
//        val result = mapper.toEntity(dto)
//        assertThat(result.reviewerUsername).isEqualTo(dto.authorDetails.username)
//    }
//
//    @Test
//    fun `toEntity should map rating correctly`() {
//        val result = mapper.toEntity(dto)
//        assertThat(result.rating).isEqualTo(dto.authorDetails.rating)
//    }
//
//    @Test
//    fun `toEntity should map content correctly`() {
//        val result = mapper.toEntity(dto)
//        assertThat(result.content).isEqualTo(dto.content)
//    }
//
//    @Test
//    fun `toEntity should map date correctly`() {
//        val result = mapper.toEntity(dto)
//        assertThat(result.date.toString()).isEqualTo("2024-07-20")
//    }
//
//    @Test
//    fun `toEntity should map imageUrl correctly`() {
//        val result = mapper.toEntity(dto)
//        assertThat(result.imageUrl).endsWith("/avatar.png")
//    }
//
//    @Test
//    fun `toEntity should return 0f rating when rating is null`() {
//        val dtoWithNullRating = createFakeReviewDto(rating = null)
//        val result = mapper.toEntity(dtoWithNullRating)
//        assertThat(result.rating).isEqualTo(0f)
//    }
//}
