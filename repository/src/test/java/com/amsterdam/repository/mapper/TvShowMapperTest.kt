package com.amsterdam.repository.mapper

import com.amsterdam.entity.TvShow
import com.amsterdam.repository.dto.local.TvShowLocalDto
import com.amsterdam.repository.dto.remote.TvShowItemRemoteDto
import com.google.common.truth.Truth.assertThat
import kotlinx.datetime.LocalDate
import org.junit.jupiter.api.Test

class TvShowMapperTest {
    @Test
    fun `toEntity should map TvShowLocalDto to TvShow entity`() {
        val result = localDto.toEntity()

        assertThat(result).isEqualTo(expectedEntityFromLocal)
    }

    @Test
    fun `toEntity should map with poster image when isPoster is true`() {
        val result = remoteDto.toEntity(isPoster = true)

        assertThat(result.posterUrl).isEqualTo("https://image.tmdb.org/t/p/w500/poster.jpg")
        assertThat(result.id).isEqualTo(remoteDto.id)
        assertThat(result.name).isEqualTo(remoteDto.title)
    }

    @Test
    fun `toEntity should map with backdrop image when isPoster is false`() {
        val result = remoteDto.toEntity(isPoster = false)

        assertThat(result.posterUrl).isEqualTo("https://image.tmdb.org/t/p/w300/backdrop.jpg")
        assertThat(result.id).isEqualTo(remoteDto.id)
        assertThat(result.name).isEqualTo(remoteDto.title)
    }

    @Test
    fun `toLocalDto should map remote DTO correctly`() {
        val result = remoteDto.toLocalDto(storedLanguage = "ar")

        assertThat(result).isEqualTo(expectedLocalDtoFromRemote)
    }

    @Test
    fun `toEntityList should map list correctly`() {
        val dtoList = listOf(remoteDto, remoteDto.copy(id = 102L))
        val result = dtoList.toEntityList()

        assertThat(result).hasSize(2)
        assertThat(result[0].id).isEqualTo(101L)
        assertThat(result[1].id).isEqualTo(102L)
    }

    @Test
    fun `toLocalTvShowDtoList should map list correctly`() {
        val dtoList = listOf(remoteDto, remoteDto.copy(id = 102L))
        val result = dtoList.toLocalTvShowDtoList(storedLanguage = "fr")

        assertThat(result).hasSize(2)
        assertThat(result[0].storedLanguage).isEqualTo("fr")
        assertThat(result[1].tvShowId).isEqualTo(102L)
    }

    private val localDto = TvShowLocalDto(
        tvShowId = 1L, storedLanguage = "en", name = "Local Show",
        description = "A show from local.", poster = "/local.jpg",
        airDate = LocalDate.parse("2023-01-01"), rating = 7.5f,
        popularity = 150.0, seasonCount = 2, originCountry = "US"
    )

    private val remoteDto = TvShowItemRemoteDto(
        id = 101L,
        title = "Remote Show",
        overview = "An overview.",
        posterPath = "/poster.jpg",
        backdropPath = "/backdrop.jpg",
        releaseDate = "2023-10-26",
        voteAverage = 8.8,
        genreIds = listOf(28),
        popularity = 1234.5,
        seasonCount = 5,
        originCountry = listOf("GB"),
        adult = false,
        originalLanguage = "en",
        originalTitle = "Remote Show Original",
        voteCount = 500
    )

    private val expectedEntityFromLocal = TvShow(
        id = 1L,
        name = "Local Show",
        description = "A show from local.",
        posterUrl = "/local.jpg",
        airDate = LocalDate.parse("2023-01-01"),
        rating = 7.5f,
        categories = emptyList(),
        popularity = 150.0,
        seasonCount = 2,
        originCountry = "US"
    )

    private val expectedLocalDtoFromRemote = TvShowLocalDto(
        tvShowId = 101L,
        storedLanguage = "ar",
        name = "Remote Show",
        description = "An overview.",
        poster = "https://image.tmdb.org/t/p/w500/poster.jpg",
        airDate = LocalDate.parse("2023-10-26"),
        rating = 8.8f,
        popularity = 1234.5,
        seasonCount = 5,
        originCountry = "GB"
    )
}