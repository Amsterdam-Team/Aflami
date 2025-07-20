package com.example.repository.mapper.local

import com.example.entity.TvShow
import com.example.repository.dto.local.LocalTvShowDto
import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class TvShowLocalMapperTest {

 private lateinit var mapper: TvShowLocalMapper

 @BeforeEach
 fun setUp() {
  mapper = TvShowLocalMapper()
 }

 @Test
 fun `toEntity should map LocalTvShowDto to TvShow correctly`() {
  // Given
  val dto = LocalTvShowDto(
   tvShowId = 1L,
   name = "Breaking Bad",
   description = "A high school chemistry teacher turned meth producer.",
   poster = "/breaking_bad.jpg",
   productionYear = 2008,
   rating = 9.5f,
   popularity = 99.9
  )

  // When
  val result = mapper.toEntity(dto)

  // Then
  assertThat(result.id).isEqualTo(dto.tvShowId)
  assertThat(result.name).isEqualTo(dto.name)
  assertThat(result.description).isEqualTo(dto.description)
  assertThat(result.posterUrl).isEqualTo(dto.poster)
  assertThat(result.productionYear.toInt()).isEqualTo(dto.productionYear)
  assertThat(result.rating).isEqualTo(dto.rating)
  assertThat(result.popularity).isEqualTo(dto.popularity)
  assertThat(result.categories).isEmpty()
 }

 @Test
 fun `toDto should map TvShow to LocalTvShowDto correctly`() {
  // Given
  val entity = TvShow(
   id = 2L,
   name = "Stranger Things",
   description = "A group of kids uncover supernatural mysteries.",
   posterUrl = "/stranger_things.jpg",
   productionYear = 2016u,
   rating = 8.8f,
   popularity = 95.0,
   categories = emptyList()
  )

  // When
  val result = mapper.toDto(entity)

  // Then
  assertThat(result.tvShowId).isEqualTo(entity.id)
  assertThat(result.name).isEqualTo(entity.name)
  assertThat(result.description).isEqualTo(entity.description)
  assertThat(result.poster).isEqualTo(entity.posterUrl)
  assertThat(result.productionYear).isEqualTo(entity.productionYear.toInt())
  assertThat(result.rating).isEqualTo(entity.rating)
  assertThat(result.popularity).isEqualTo(entity.popularity)
 }
}
