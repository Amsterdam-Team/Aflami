package com.amsterdam.repository.mapper

import com.amsterdam.entity.Episode
import com.amsterdam.repository.dto.remote.EpisodeRemoteDto
import com.google.common.truth.Truth.assertThat
import kotlinx.datetime.LocalDate
import org.junit.jupiter.api.Test

class EpisodeMapperTest {
    @Test
    fun `toEntity should map full EpisodeRemoteDto to Episode entity`() {
        val result = fullEpisodeDto.toEntity()

        assertThat(result).isEqualTo(expectedFullEpisode)
    }

    @Test
    fun `toEntity should handle null values and map to default values in Episode entity`() {
        val result = minimalEpisodeDto.toEntity()

        assertThat(result).isEqualTo(expectedMinimalEpisode)
    }

    @Test
    fun `toEntityList should map list of Dtos to list of Episode entities`() {
        val dtoList = listOf(fullEpisodeDto, minimalEpisodeDto)
        val result = dtoList.toEntityList()

        assertThat(result).isEqualTo(expectedEpisodeList)
    }

    @Test
    fun `toEntityList should return an empty list when given an empty list`() {
        val emptyDtoList = emptyList<EpisodeRemoteDto>()
        val result = emptyDtoList.toEntityList()

        assertThat(result).isEmpty()
    }

    private val fullEpisodeDto = EpisodeRemoteDto(
        id = 1L,
        title = "Winter Is Coming",
        episodeNumber = 1,
        overview = "The first episode of the series.",
        stillPath = "/winter_is_coming.jpg",
        voteAverage = 9.1,
        airDate = "2011-04-17",
        seasonNumber = 1,
        runtime = "62"
    )

    private val minimalEpisodeDto = EpisodeRemoteDto(
        id = 2L,
        title = "The Iron Throne",
        episodeNumber = 6,
        overview = "The final episode of the series.",
        stillPath = null,
        voteAverage = 4.0,
        airDate = null,
        seasonNumber = 8,
        runtime = null
    )

    private val expectedFullEpisode = Episode(
        id = 1L,
        title = "Winter Is Coming",
        episodeNumber = 1,
        description = "The first episode of the series.",
        episodeImageUrl = "https://image.tmdb.org/t/p/w500/winter_is_coming.jpg",
        rating = 9.1f,
        airDate = LocalDate.parse("2011-04-17"),
        seasonNumber = 1,
        runTimeInMinutes = 62,
        videoUrl = ""
    )

    private val expectedMinimalEpisode = Episode(
        id = 2L,
        title = "The Iron Throne",
        episodeNumber = 6,
        description = "The final episode of the series.",
        episodeImageUrl = "",
        rating = 4.0f,
        airDate = null,
        seasonNumber = 8,
        runTimeInMinutes = 0,
        videoUrl = ""
    )

    private val expectedEpisodeList = listOf(
        expectedFullEpisode,
        expectedMinimalEpisode
    )
}