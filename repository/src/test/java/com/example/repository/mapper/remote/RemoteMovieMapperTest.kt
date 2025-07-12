package com.example.repository.mapper.remote

import com.example.repository.dto.remote.RemoteMovieItemDto
import com.example.repository.dto.remote.RemoteMovieResponse
import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test

class RemoteMovieMapperTest {

    private val mapper = RemoteMovieMapper()

    private fun createRemoteMovieItemDto(
        id: Long,
        title: String,
        overview: String,
        posterPath: String? = "/poster.jpg",
        releaseDate: String = "2020-01-01",
        voteAverage: Double = 7.0
    ): RemoteMovieItemDto {
        return RemoteMovieItemDto(
            adult = false,
            backdropPath = null,
            genreIds = listOf(28, 12),
            id = id,
            originalLanguage = "en",
            originalTitle = title,
            overview = overview,
            popularity = 123.4,
            posterPath = posterPath,
            releaseDate = releaseDate,
            title = title,
            video = false,
            voteAverage = voteAverage,
            voteCount = 1000
        )
    }

    @Test
    fun `should map RemoteMovieItemDto to Movie correctly`() {
        val dto = createRemoteMovieItemDto(
            id = 1L,
            title = "Inception",
            overview = "A mind-bending thriller",
            posterPath = "/inception.jpg",
            releaseDate = "2010-07-16",
            voteAverage = 8.8
        )

        val result = mapper.mapToDomain(dto)

        assertThat(result.id).isEqualTo(1L)
        assertThat(result.name).isEqualTo("Inception")
        assertThat(result.description).isEqualTo("A mind-bending thriller")
        assertThat(result.poster).isEqualTo("/inception.jpg")
        assertThat(result.productionYear).isEqualTo(2010)
        assertThat(result.rating).isEqualTo(8.8f)
        assertThat(result.categories).isEmpty()
    }

    @Test
    fun `should return empty poster when posterPath is null`() {
        val dto = createRemoteMovieItemDto(
            id = 2L,
            title = "No Poster",
            overview = "No poster test",
            posterPath = null
        )

        val result = mapper.mapToDomain(dto)

        assertThat(result.poster).isEqualTo("")
    }

    @Test
    fun `should return 0 as productionYear when releaseDate is invalid`() {
        val dto = createRemoteMovieItemDto(
            id = 3L,
            title = "Bad Date",
            overview = "Invalid date",
            releaseDate = "invalid-date"
        )

        val result = mapper.mapToDomain(dto)

        assertThat(result.productionYear).isEqualTo(0)
    }

    @Test
    fun `should return 0 as productionYear when releaseDate is empty`() {
        val dto = createRemoteMovieItemDto(
            id = 4L,
            title = "Empty Date",
            overview = "No date provided",
            releaseDate = ""
        )

        val result = mapper.mapToDomain(dto)

        assertThat(result.productionYear).isEqualTo(0)
    }

    @Test
    fun `should map list of RemoteMovieItemDto to list of Movies`() {
        val movieList = listOf(
            createRemoteMovieItemDto(10L, "Movie A", "Overview A"),
            createRemoteMovieItemDto(11L, "Movie B", "Overview B", releaseDate = "2021-06-10")
        )

        val response = RemoteMovieResponse(
            page = 1,
            results = movieList,
            totalPages = 1,
            totalResults = 2
        )

        val result = mapper.mapResponseToDomain(response)

        assertThat(result).hasSize(2)
        assertThat(result[0].name).isEqualTo("Movie A")
        assertThat(result[1].productionYear).isEqualTo(2021)
    }

    @Test
    fun `should return empty list when response has no results`() {
        val response = RemoteMovieResponse(
            page = 1,
            results = emptyList(),
            totalPages = 0,
            totalResults = 0
        )

        val result = mapper.mapResponseToDomain(response)

        assertThat(result).isEmpty()
    }

}
