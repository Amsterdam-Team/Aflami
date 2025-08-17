package com.amsterdam.repository.mapper

import com.amsterdam.entity.category.MovieGenre
import com.amsterdam.repository.dto.local.MovieCategoryLocalDto
import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream

class MovieGenreMapperTest {
    @ParameterizedTest(name = "toEntity should map ID {0} to {1}")
    @MethodSource("idToGenreProvider")
    fun `toEntity should map every possible categoryId to its correct MovieGenre`(
        id: Long,
        expectedGenre: MovieGenre
    ) {
        val dto = MovieCategoryLocalDto(categoryId = id)
        val result = dto.toEntity()
        assertThat(result).isEqualTo(expectedGenre)
    }

    @ParameterizedTest(name = "toMovieGenre should map ID {0} to {1}")
    @MethodSource("idToGenreProvider")
    fun `toMovieGenre should map every possible Long ID to its correct MovieGenre`(
        id: Long,
        expectedGenre: MovieGenre
    ) {
        val result = toMovieGenre(id)
        assertThat(result).isEqualTo(expectedGenre)
    }

    @ParameterizedTest(name = "toDto should map {0} to ID {1}")
    @MethodSource("genreToIdProvider")
    fun `toDto should map every MovieGenre to its correct Long ID`(
        genre: MovieGenre,
        expectedId: Long
    ) {
        val result = genre.toDto()
        assertThat(result).isEqualTo(expectedId)
    }

    @Test
    fun `toDtoList should map a list of MovieGenre to a list of Longs`() {
        val genreList = listOf(MovieGenre.ACTION, MovieGenre.DRAMA, MovieGenre.ALL)
        val result = genreList.toDtoList()
        assertThat(result).containsExactly(28L, 18L, 35L).inOrder()
    }

    @Test
    fun `toDtoList should return an empty list when given an empty list`() {
        val emptyGenreList = emptyList<MovieGenre>()
        val result = emptyGenreList.toDtoList()
        assertThat(result).isEmpty()
    }

    companion object {
        @JvmStatic
        fun idToGenreProvider(): Stream<Arguments> = Stream.of(
            Arguments.of(28L, MovieGenre.ACTION),
            Arguments.of(12L, MovieGenre.ADVENTURE),
            Arguments.of(16L, MovieGenre.ANIMATION),
            Arguments.of(35L, MovieGenre.COMEDY),
            Arguments.of(80L, MovieGenre.CRIME),
            Arguments.of(99L, MovieGenre.DOCUMENTARY),
            Arguments.of(18L, MovieGenre.DRAMA),
            Arguments.of(10751L, MovieGenre.FAMILY),
            Arguments.of(14L, MovieGenre.FANTASY),
            Arguments.of(36L, MovieGenre.HISTORY),
            Arguments.of(27L, MovieGenre.HORROR),
            Arguments.of(10402L, MovieGenre.MUSIC),
            Arguments.of(9648L, MovieGenre.MYSTERY),
            Arguments.of(10749L, MovieGenre.ROMANCE),
            Arguments.of(878L, MovieGenre.SCIENCE_FICTION),
            Arguments.of(10770L, MovieGenre.TV_MOVIE),
            Arguments.of(53L, MovieGenre.THRILLER),
            Arguments.of(10752L, MovieGenre.WAR),
            Arguments.of(37L, MovieGenre.WESTERN),
            Arguments.of(9999L, MovieGenre.ALL)
        )

        @JvmStatic
        fun genreToIdProvider(): Stream<Arguments> = Stream.of(
            Arguments.of(MovieGenre.ACTION, 28L),
            Arguments.of(MovieGenre.ADVENTURE, 12L),
            Arguments.of(MovieGenre.ANIMATION, 16L),
            Arguments.of(MovieGenre.COMEDY, 35L),
            Arguments.of(MovieGenre.CRIME, 80L),
            Arguments.of(MovieGenre.DOCUMENTARY, 99L),
            Arguments.of(MovieGenre.DRAMA, 18L),
            Arguments.of(MovieGenre.FAMILY, 10751L),
            Arguments.of(MovieGenre.FANTASY, 14L),
            Arguments.of(MovieGenre.HISTORY, 36L),
            Arguments.of(MovieGenre.HORROR, 27L),
            Arguments.of(MovieGenre.MUSIC, 10402L),
            Arguments.of(MovieGenre.MYSTERY, 9648L),
            Arguments.of(MovieGenre.ROMANCE, 10749L),
            Arguments.of(MovieGenre.SCIENCE_FICTION, 878L),
            Arguments.of(MovieGenre.TV_MOVIE, 10770L),
            Arguments.of(MovieGenre.THRILLER, 53L),
            Arguments.of(MovieGenre.WAR, 10752L),
            Arguments.of(MovieGenre.WESTERN, 37L),
            Arguments.of(MovieGenre.ALL, 35L)
        )
    }
}