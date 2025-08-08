package com.amsterdam.repository.mapper.local

import com.amsterdam.entity.category.MovieGenre
import com.amsterdam.repository.dto.local.LocalMovieCategoryDto
import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test

class MovieGenreLocalMapperTest {


    @Test
    fun `toEntity should return ALL genre when categoryId is invalid`() {
        // Given
        val dto = LocalMovieCategoryDto(
            categoryId = 100,
        )

        // When
        val result = dto.toMovieGenreEntity()

        // Then
        assertThat(result).isEqualTo(MovieGenre.ALL)
    }

    @Test
    fun `toEntity should return ALL genre when categoryId is 0`() {
        val dto = LocalMovieCategoryDto(
            categoryId = 0,
            )

        val result = dto.toMovieGenreEntity()

        assertThat(result).isEqualTo(MovieGenre.ALL)
    }

    @Test
    fun `given list of MovieGenre, when toDtoList is called, then it should return list of ids in order`() {
        // Given
        val genres = listOf(
            MovieGenre.ALL,
            MovieGenre.DRAMA,
            MovieGenre.COMEDY,
            MovieGenre.FAMILY,
            MovieGenre.ROMANCE,
            MovieGenre.TV_MOVIE,
            MovieGenre.DOCUMENTARY,
            MovieGenre.FANTASY,
            MovieGenre.HISTORY,
            MovieGenre.HORROR,
            MovieGenre.MUSIC,
            MovieGenre.MYSTERY,
            MovieGenre.THRILLER,
            MovieGenre.WAR,
            MovieGenre.WESTERN,
            MovieGenre.ACTION,
            MovieGenre.ADVENTURE,
            MovieGenre.ANIMATION,
            MovieGenre.CRIME,
            MovieGenre.SCIENCE_FICTION
        )

        // When
        val result = genres.toDtoList()

        // Then
        assertThat(result).containsExactly(
            35L,
            18L,
            35L,
            10751L,
            10749L,
            10770L,
            99L,
            14L,
            36L,
            27L,
            10402L,
            9648L,
            53L,
            10752L,
            37L,
            28L,
            12L,
            16L,
            80L,
            878L
        ).inOrder()
    }

}

