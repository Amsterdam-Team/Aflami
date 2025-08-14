package com.amsterdam.repository.mapper.local

import com.amsterdam.repository.dto.local.TvShowCategoryLocalDto
import com.amsterdam.repository.dto.local.TvShowLocalDto
import com.amsterdam.repository.dto.local.relation.TvShowWithCategory
import com.amsterdam.repository.mapper.toEntity
import com.google.common.truth.Truth.assertThat
import kotlinx.datetime.LocalDate
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class TvShowCategoryLocalMapperTest {


    @Test
    @DisplayName("should map LocalTvShowCategoryDto to Category correctly")
    fun `toEntity should map correctly`() {
        // Arrange
        val dto = TvShowWithCategory(
            tvShow = TvShowLocalDto(
                tvShowId = 1,
                name = "Drama",
                description = "A drama movie",
                poster = "",
                storedLanguage ="en",
                airDate = LocalDate.parse("2020-01-01"),
                rating = 3.6f,
                popularity = 4.0,
                seasonCount = 3,
                originCountry ="",
            ),
            categories = listOf(
                TvShowCategoryLocalDto(
                    categoryId = 1,
                )
            )
        )

        // Act
        val result = dto.toEntity()

        // Assert
        assertThat(result.id).isEqualTo(1)
        assertThat(result.name).isEqualTo("Drama")
    }


}
