package com.example.repository.mapper.local

import com.example.entity.TvShow
import com.example.entity.category.TvShowGenre
import com.example.repository.dto.local.LocalTvShowCategoryDto
import com.example.repository.dto.local.LocalTvShowDto
import com.example.repository.dto.local.relation.TvShowWithCategory
import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockk
import org.junit.Before
import org.junit.Test

class TvShowWithCategoryLocalMapperTest {

 private lateinit var mapper: TvShowWithCategoryLocalMapper
 private val genreMapper: TvShowGenreLocalMapper = mockk()

 @Before
 fun setUp() {
  mapper = TvShowWithCategoryLocalMapper(genreMapper)
 }

 @Test
 fun `toEntity maps TvShowWithCategory to TvShow correctly`() {
  // Arrange: fake input
  val localTvShow = LocalTvShowDto(
   tvShowId = 1L,
   name = "Breaking Bad",
   description = "A high school chemistry teacher turns to a life of crime.",
   poster = "https://poster.url/breakingbad.jpg",
   productionYear = 2008,
   rating = 9.5f,
   popularity = 1000.0
  )

  val localCategories = listOf(
   LocalTvShowCategoryDto(categoryId = 10L, name = "DRAMA"),
   LocalTvShowCategoryDto(categoryId = 11L, name = "CRIME")
  )

  val tvShowWithCategory = TvShowWithCategory(
   tvShow = localTvShow,
   categories = localCategories
  )

  val expectedGenres = listOf(TvShowGenre.DRAMA, TvShowGenre.CRIME)

  // Mock behavior
  every { genreMapper.toEntityList(localCategories) } returns expectedGenres

  // Act
  val result = mapper.toEntity(tvShowWithCategory)

  // Assert
  assertThat(result).isEqualTo(
   TvShow(
    id = 1L,
    name = "Breaking Bad",
    description = "A high school chemistry teacher turns to a life of crime.",
    posterUrl = "https://poster.url/breakingbad.jpg",
    productionYear = 2008u,
    rating = 9.5f,
    categories = expectedGenres,
    popularity = 1000.0
   )
  )
 }
}
