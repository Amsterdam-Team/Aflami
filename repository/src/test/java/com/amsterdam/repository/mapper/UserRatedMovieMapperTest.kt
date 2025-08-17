package com.amsterdam.repository.mapper

import com.amsterdam.domain.useCase.myRating.movie.GetUserRatedMoviesUseCase.UserRatedMovie
import com.amsterdam.repository.dto.remote.MovieItemRemoteDto
import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test

class UserRatedMovieMapperTest {
    @Test
    fun `toMovieUserRateEntity should map Dto to UserRatedMovie entity`() {
        val result = ratedMovieDto.toMovieUserRateEntity(ratedMovieDto)

        assertThat(result).isEqualTo(expectedUserRatedMovie)
    }

    @Test
    fun `toMovieUserRateEntity should map zero rating correctly`() {
        val result = ratedMovieDtoWithZeroRating.toMovieUserRateEntity(ratedMovieDtoWithZeroRating)

        assertThat(result).isEqualTo(expectedUserRatedMovieWithZeroRating)
    }

    @Test
    fun `toMovieUserRateEntityList should map a list of Dtos correctly`() {
        val dtoList = listOf(ratedMovieDto, ratedMovieDtoWithZeroRating)
        val result = dtoList.toMovieUserRateEntityList()

        assertThat(result).isEqualTo(expectedRatedMovieList)
        assertThat(result).hasSize(2)
        assertThat(result[0].userRate).isEqualTo(8)
        assertThat(result[1].movie.id).isEqualTo(102L)
    }

    @Test
    fun `toMovieUserRateEntityList should return an empty list when given an empty list`() {
        val emptyDtoList = emptyList<MovieItemRemoteDto>()
        val result = emptyDtoList.toMovieUserRateEntityList()

        assertThat(result).isEmpty()
    }

    private val ratedMovieDto = MovieItemRemoteDto(
        id = 101L, title = "Rated Movie", overview = "An overview.",
        posterPath = "/poster.jpg", backdropPath = null, releaseDate = "2023-10-26",
        voteAverage = 8.8, genreIds = listOf(28), popularity = 1234.5,
        originCountry = listOf("GB"), runtime = 120, adult = false,
        originalLanguage = "en", originalTitle = "Rated Movie", video = false,
        voteCount = 1000, rating = 8.5f
    )

    private val ratedMovieDtoWithZeroRating = ratedMovieDto.copy(
        id = 102L,
        rating = 0f
    )

    private val expectedUserRatedMovie = UserRatedMovie(
        movie = ratedMovieDto.toEntity(),
        userRate = 8
    )

    private val expectedUserRatedMovieWithZeroRating = UserRatedMovie(
        movie = ratedMovieDtoWithZeroRating.toEntity(),
        userRate = 0
    )

    private val expectedRatedMovieList = listOf(
        expectedUserRatedMovie,
        expectedUserRatedMovieWithZeroRating
    )
}