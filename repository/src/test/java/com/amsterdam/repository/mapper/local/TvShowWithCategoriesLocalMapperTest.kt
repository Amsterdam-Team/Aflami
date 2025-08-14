package com.amsterdam.repository.mapper.local

import com.amsterdam.entity.TvShow
import com.amsterdam.entity.category.TvShowGenre
import com.amsterdam.repository.dto.local.TvShowCategoryLocalDto
import com.amsterdam.repository.dto.local.TvShowLocalDto
import com.amsterdam.repository.dto.local.relation.TvShowWithCategories
import com.amsterdam.repository.mapper.toEntity
import com.google.common.truth.Truth.assertThat
import kotlinx.datetime.LocalDate
import org.junit.Test

class TvShowWithCategoriesLocalMapperTest {


    @Test
    fun `toEntity maps TvShowWithCategory to TvShow correctly`() {
        // Arrange
        val dto = TvShowWithCategories(
            tvShow = TvShowLocalDto(
                tvShowId = 1,
                name = "Drama",
                description = "A drama movie",
                poster = "",
                storedLanguage = "en",
                airDate = LocalDate.parse("2020-01-01"),
                rating = 3.6f,
                popularity = 4.0,
                seasonCount = 3,
                originCountry = "",
            ),
            categories = listOf(
                TvShowCategoryLocalDto(
                    categoryId = 1,
                )
            )
        )
        val expectedGenres = listOf(TvShowGenre.DRAMA, TvShowGenre.CRIME)

        // Act
        val result = dto.toEntity()

        // Assert
        val expected = TvShow(
            id = 1,
            name = "Drama",
            description = "A drama movie",
            posterUrl = "",
            airDate = LocalDate.parse("2020-01-01"),
            rating = 3.6f,
            categories = expectedGenres,
            popularity = 4.0,
            seasonCount = 3,
            originCountry = "",
        )

        assertThat(result).isEqualTo(expected)
    }
}

