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
        val dto = tvShowWithCategoriesDto
        val expected = expectedTvShow

        val result = dto.toEntity()

        assertThat(result).isEqualTo(expected)
    }

    companion object {
        private val tvShowWithCategoriesDto = TvShowWithCategories(
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

        private val expectedGenres = listOf(TvShowGenre.DRAMA, TvShowGenre.CRIME)

        private val expectedTvShow = TvShow(
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
    }
}