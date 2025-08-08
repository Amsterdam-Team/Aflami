package com.amsterdam.repository.mapper.remote

import com.amsterdam.entity.category.MovieGenre
import com.amsterdam.repository.mapper.local.toDtoList
import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test

class CastRemoteMapperTest {

    @Test
    fun `given list of MovieGenre, when toDtoList is called, then it should return list of ids in order`() {
        // Given
        val genres = listOf(
            MovieGenre.ALL,
            MovieGenre.TV_MOVIE,
            MovieGenre.DOCUMENTARY
        )

        // When
        val result = genres.toDtoList()

        // Then
        assertThat(result).containsExactly(35L, 10770L, 99L).inOrder()
    }

    @Test
    fun `given empty list, when toDtoList is called, then it should return empty list`() {
        // Given
        val emptyGenres = emptyList<MovieGenre>()

        // When
        val result = emptyGenres.toDtoList()

        // Then
        assertThat(result).isEmpty()
    }


}
