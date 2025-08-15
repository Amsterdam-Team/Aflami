package com.amsterdam.repository.mapper.local

import com.amsterdam.entity.Movie
import com.amsterdam.repository.dto.local.MovieLocalDto
import com.amsterdam.repository.mapper.toEntity
import com.google.common.truth.Truth.assertThat
import kotlinx.datetime.LocalDate
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class MovieLocalMapperTest {
    @Test
    @DisplayName("should return Movie entity when converting from LocalMovieDto")
    fun `toEntity should return Movie when given LocalMovieDto`() {
        val dto = movieLocalDto

        val result = dto.toEntity()

        assertThat(result).isEqualTo(expectedMovie)
    }

    companion object {
        private const val MOVIE_ID = 101L
        private const val MOVIE_NAME = "Inception"
        private const val MOVIE_DESCRIPTION = "A mind-bending thriller"
        private const val MOVIE_POSTER = "poster_url.jpg"
        private const val MOVIE_RATING = 8.8f
        private const val MOVIE_POPULARITY = 99.5
        private const val MOVIE_LENGTH = 148
        private const val MOVIE_ORIGIN_COUNTRY = "USA"
        private const val MOVIE_STORED_LANGUAGE = "en"
        private val MOVIE_RELEASE_DATE = LocalDate.parse("2020-01-01")

        private val movieLocalDto = MovieLocalDto(
            movieId = MOVIE_ID,
            name = MOVIE_NAME,
            description = MOVIE_DESCRIPTION,
            poster = MOVIE_POSTER,
            releaseDate = MOVIE_RELEASE_DATE,
            rating = MOVIE_RATING,
            popularity = MOVIE_POPULARITY,
            movieLength = MOVIE_LENGTH,
            originCountry = MOVIE_ORIGIN_COUNTRY,
            storedLanguage = MOVIE_STORED_LANGUAGE,
        )

        private val expectedMovie = Movie(
            id = MOVIE_ID,
            name = MOVIE_NAME,
            description = MOVIE_DESCRIPTION,
            posterUrl = MOVIE_POSTER,
            rating = MOVIE_RATING,
            popularity = MOVIE_POPULARITY,
            runTimeInMinutes = MOVIE_LENGTH,
            originCountry = MOVIE_ORIGIN_COUNTRY,
            releaseDate = MOVIE_RELEASE_DATE,
            categories = emptyList(),
            videoUrl = "",
        )
    }
}