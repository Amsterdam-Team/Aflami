package com.example.repository.mapper.remote

import com.example.repository.dto.remote.RemoteTvShowItemDto
import com.example.repository.dto.remote.RemoteTvShowResponse
import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test

class RemoteTvShowMapperTest {

    private val mapper = RemoteTvShowMapper()

    private fun createRemoteTvShowItemDto(
        id: Long,
        title: String,
        overview: String,
        posterPath: String? = "/poster.jpg",
        releaseDate: String = "2020-01-01",
        voteAverage: Double = 7.0,
        originCountry: List<String> = listOf("US")
    ): RemoteTvShowItemDto {
        return RemoteTvShowItemDto(
            adult = false,
            backdropPath = null,
            genreIds = listOf(18),
            id = id,
            originalLanguage = "en",
            originalTitle = title,
            overview = overview,
            popularity = 88.8,
            posterPath = posterPath,
            releaseDate = releaseDate,
            title = title,
            voteAverage = voteAverage,
            originCountry = originCountry,
            voteCount = 1234
        )
    }

    @Test
    fun `should map RemoteTvShowItemDto to TvShow correctly`() {
        val dto = createRemoteTvShowItemDto(
            id = 1L,
            title = "Loki",
            overview = "Time travel and mischief",
            posterPath = "/loki.jpg",
            releaseDate = "2021-06-09",
            voteAverage = 8.5
        )

        val result = mapper.mapToTvShow(dto)

        assertThat(result.id).isEqualTo(1L)
        assertThat(result.name).isEqualTo("Loki")
        assertThat(result.description).isEqualTo("Time travel and mischief")
        assertThat(result.poster).isEqualTo("/loki.jpg")
        assertThat(result.productionYear).isEqualTo(2021)
        assertThat(result.rating).isEqualTo(8.5f)
        assertThat(result.categories).isEmpty()
    }

    @Test
    fun `should return empty poster when posterPath is null`() {
        val dto = createRemoteTvShowItemDto(
            id = 2L,
            title = "No Image",
            overview = "No poster",
            posterPath = null
        )

        val result = mapper.mapToTvShow(dto)

        assertThat(result.poster).isEqualTo("")
    }

    @Test
    fun `should return 0 as productionYear when releaseDate is invalid`() {
        val dto = createRemoteTvShowItemDto(
            id = 3L,
            title = "Broken Date",
            overview = "Bad date format",
            releaseDate = "abcd"
        )

        val result = mapper.mapToTvShow(dto)

        assertThat(result.productionYear).isEqualTo(0)
    }

    @Test
    fun `should return 0 as productionYear when releaseDate is empty`() {
        val dto = createRemoteTvShowItemDto(
            id = 4L,
            title = "No Date",
            overview = "Missing release date",
            releaseDate = ""
        )

        val result = mapper.mapToTvShow(dto)

        assertThat(result.productionYear).isEqualTo(0)
    }

    @Test
    fun `should map list of RemoteTvShowItemDto to list of TvShow`() {
        val dtos = listOf(
            createRemoteTvShowItemDto(101L, "Show A", "Desc A"),
            createRemoteTvShowItemDto(102L, "Show B", "Desc B", releaseDate = "2015-03-05")
        )

        val response = RemoteTvShowResponse(
            page = 1,
            results = dtos,
            totalPages = 1,
            totalResults = 2
        )

        val result = mapper.mapToTvShows(response)

        assertThat(result).hasSize(2)
        assertThat(result[0].name).isEqualTo("Show A")
        assertThat(result[1].productionYear).isEqualTo(2015)
    }

    @Test
    fun `should return empty list when response has no results`() {
        val response = RemoteTvShowResponse(
            page = 1,
            results = emptyList(),
            totalPages = 0,
            totalResults = 0
        )

        val result = mapper.mapToTvShows(response)

        assertThat(result).isEmpty()
    }
}
