package com.example.repository.mapper.local

import com.example.entity.category.TvShowGenre
import com.example.repository.mapper.local.testFactory.createLocalTvShowCategoryDtoList
import com.example.repository.mapper.local.testFactory.createLocalTvShowDto
import com.example.repository.mapper.local.testFactory.createTvShow
import com.example.repository.mapper.local.testFactory.createTvShowWithCategory
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
        // Arrange
        val tvShowDto = createLocalTvShowDto()
        val categories = createLocalTvShowCategoryDtoList()
        val tvShowWithCategory = createTvShowWithCategory(dto = tvShowDto, categories = categories)

        val expectedGenres = listOf(TvShowGenre.DRAMA, TvShowGenre.CRIME)
        every { genreMapper.toEntityList(categories) } returns expectedGenres

        // Act
        val result = mapper.toEntity(tvShowWithCategory)

        // Assert
        val expected = createTvShow(
            id = tvShowDto.tvShowId,
            name = tvShowDto.name,
            description = tvShowDto.description,
            posterUrl = tvShowDto.poster,
            productionYear = tvShowDto.productionYear.toUInt(),
            rating = tvShowDto.rating,
            popularity = tvShowDto.popularity,
            categories = expectedGenres
        )

        assertThat(result).isEqualTo(expected)
    }
}
