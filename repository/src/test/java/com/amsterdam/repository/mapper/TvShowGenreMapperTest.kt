package com.amsterdam.repository.mapper

import com.amsterdam.entity.category.TvShowGenre
import com.amsterdam.repository.dto.local.TvShowCategoryLocalDto
import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

class TvShowGenreMapperTest {
    @Nested
    inner class DtoToEntityMapping {
        @Test
        fun `toEntity should map known categoryId to correct TvShowGenre`() {
            val actionDto = TvShowCategoryLocalDto(categoryId = 28L)

            val result = actionDto.toEntity()

            assertThat(result).isEqualTo(TvShowGenre.ACTION_ADVENTURE)
        }

        @Test
        fun `toEntity should map another known categoryId to correct TvShowGenre`() {
            val kidsDto = TvShowCategoryLocalDto(categoryId = 10762L)

            val result = kidsDto.toEntity()

            assertThat(result).isEqualTo(TvShowGenre.KIDS)
        }

        @Test
        fun `toEntity should map unknown categoryId to ALL as default`() {
            val unknownDto = TvShowCategoryLocalDto(categoryId = 9999L)

            val result = unknownDto.toEntity()

            assertThat(result).isEqualTo(TvShowGenre.ALL)
        }
    }

    @Nested
    inner class EntityToDtoMapping {
        @ParameterizedTest(name = "toDto should map {0} to ID {1}")
        @CsvSource(
            "ACTION_ADVENTURE, 28",
            "ANIMATION, 16",
            "COMEDY, 35",
            "CRIME, 80",
            "DOCUMENTARY, 99",
            "DRAMA, 18",
            "FAMILY, 10751",
            "MYSTERY, 9648",
            "WESTERN, 37",
            "KIDS, 10762",
            "NEWS, 10763",
            "REALITY, 10764",
            "SOAP, 10765",
            "TALK, 10766",
            "WAR_POLITICS, 10767",
            "SCIENCE_FICTION_FANTASY, 10768",
            "ALL, 35"
        )
        fun `toDto should map every TvShowGenre to its correct Long ID`(
            genre: TvShowGenre,
            expectedId: Long
        ) {
            val result = genre.toDto()

            assertThat(result).isEqualTo(expectedId)
        }

        @Test
        fun `toDtoList should map a list of TvShowGenre to a list of Longs`() {
            val genreList = listOf(TvShowGenre.ANIMATION, TvShowGenre.DRAMA, TvShowGenre.ALL)

            val result = genreList.toDtoList()

            assertThat(result).containsExactly(16L, 18L, 35L).inOrder()
        }

        @Test
        fun `toDtoList should return an empty list when given an empty list`() {
            val emptyGenreList = emptyList<TvShowGenre>()

            val result = emptyGenreList.toDtoList()

            assertThat(result).isEmpty()
        }
    }

    @Nested
    inner class TopLevelFunctionMapping {
        @Test
        fun `toTvShowGenre should map known ID to correct TvShowGenre`() {
            val actionAdventureId = 10759L

            val result = toTvShowGenre(actionAdventureId)

            assertThat(result).isEqualTo(TvShowGenre.ACTION_ADVENTURE)
        }

        @Test
        fun `toTvShowGenre should map another known ID to correct TvShowGenre`() {
            val sciFiFantasyId = 10765L

            val result = toTvShowGenre(sciFiFantasyId)

            assertThat(result).isEqualTo(TvShowGenre.SCIENCE_FICTION_FANTASY)
        }

        @Test
        fun `toTvShowGenre should map unknown ID to ALL as default`() {
            val unknownId = -1L

            val result = toTvShowGenre(unknownId)

            assertThat(result).isEqualTo(TvShowGenre.ALL)
        }
    }
}