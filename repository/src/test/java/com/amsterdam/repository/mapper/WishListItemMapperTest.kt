package com.amsterdam.repository.mapper

import com.amsterdam.entity.Movie
import com.amsterdam.entity.TvShow
import com.amsterdam.entity.category.MovieGenre
import com.amsterdam.entity.category.TvShowGenre
import com.amsterdam.repository.dto.remote.WishListItemRemoteDto
import com.google.common.truth.Truth.assertThat
import kotlinx.datetime.LocalDate
import org.junit.jupiter.api.Test

class WishListItemMapperTest {
    @Test
    fun `toMovieEntity should map movie Dto with poster image`() {
        val result = movieDto.toMovieEntity()

        assertThat(result).isEqualTo(expectedMovie)
    }

    @Test
    fun `toMovieEntity should map with backdrop image when isPoster is false`() {
        val result = movieDto.toMovieEntity(isPoster = false)

        assertThat(result.posterUrl).isEqualTo("https://image.tmdb.org/t/p/w300/backdrop.jpg")
    }

    @Test
    fun `toMovieEntity should handle null values and map to default values`() {
        val result = dtoWithNulls.toMovieEntity()

        assertThat(result).isEqualTo(expectedMovieWithNulls)
    }

    @Test
    fun `toTvShowEntity should map with backdrop image when isPoster is false`() {
        val result = tvShowDto.toTvShowEntity(isPoster = false)

        assertThat(result.posterUrl).isEqualTo("https://image.tmdb.org/t/p/w300/tv_backdrop.jpg")
    }

    @Test
    fun `toTvShowEntity should handle null values and map to default values`() {
        val result = dtoWithNulls.toTvShowEntity()

        assertThat(result).isEqualTo(expectedTvShowWithNulls)
    }

    private val movieDto = WishListItemRemoteDto(
        id = 101L,
        title = "Test Movie",
        name = null,
        overview = "An overview.",
        posterPath = "/poster.jpg",
        backdropPath = "/backdrop.jpg",
        releaseDate = "2023-10-26",
        firstAirDate = null,
        voteAverage = 8.8,
        genreIds = listOf(28),
        popularity = 1234.5,
        originalLanguage = "en",
        mediaType = "movie",
        adult = false
    )

    private val tvShowDto = WishListItemRemoteDto(
        id = 201L,
        title = null,
        name = "Test TV Show",
        overview = "A TV overview.",
        posterPath = "/tv_poster.jpg",
        backdropPath = "/tv_backdrop.jpg",
        releaseDate = null,
        firstAirDate = "2022-12-25",
        voteAverage = 9.1,
        genreIds = listOf(10759),
        popularity = 567.8,
        originalLanguage = "fr",
        mediaType = "tv",
        adult = false
    )

    private val dtoWithNulls = WishListItemRemoteDto(
        id = 301L,
        title = null,
        name = null,
        overview = "Another overview.",
        posterPath = null,
        backdropPath = null,
        releaseDate = null,
        firstAirDate = null,
        voteAverage = null,
        genreIds = null,
        popularity = null,
        originalLanguage = "de",
        mediaType = "person",
        adult = true
    )

    private val expectedMovie = Movie(
        id = 101L,
        name = "Test Movie",
        posterUrl = "https://image.tmdb.org/t/p/w500/poster.jpg",
        releaseDate = LocalDate.parse("2023-10-26"),
        rating = 8.8f,
        categories = listOf(MovieGenre.ACTION),
        description = "An overview.",
        popularity = 1234.5,
        runTimeInMinutes = 0,
        originCountry = "en"
    )

    private val expectedTvShow = TvShow(
        id = 201L,
        name = "Test TV Show",
        posterUrl = "https://image.tmdb.org/t/p/w500/tv_poster.jpg",
        airDate = LocalDate.parse("2022-12-25"),
        rating = 9.1f,
        categories = listOf(TvShowGenre.ACTION_ADVENTURE),
        description = "A TV overview.",
        popularity = 567.8,
        seasonCount = 0,
        originCountry = "fr"
    )

    private val expectedMovieWithNulls = Movie(
        id = 301L,
        name = "",
        posterUrl = "",
        releaseDate = LocalDate.fromEpochDays(0),
        rating = 0f,
        categories = emptyList(),
        description = "Another overview.",
        popularity = 0.0,
        runTimeInMinutes = 0,
        originCountry = "de"
    )

    private val expectedTvShowWithNulls = TvShow(
        id = 301L,
        name = "",
        posterUrl = "",
        airDate = LocalDate.fromEpochDays(0),
        rating = 0f,
        categories = emptyList(),
        description = "Another overview.",
        popularity = 0.0,
        seasonCount = 0,
        originCountry = "de"
    )
}