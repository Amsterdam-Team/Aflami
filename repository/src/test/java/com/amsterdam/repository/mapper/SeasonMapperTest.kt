package com.amsterdam.repository.mapper

import com.amsterdam.entity.Season
import com.amsterdam.repository.dto.remote.SeasonRemoteDto
import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test

class SeasonMapperTest {
    @Test
    fun `toEntity should map SeasonRemoteDto to Season entity`() {
        val result = season1Dto.toEntity()

        assertThat(result).isEqualTo(expectedSeason1)
    }

    @Test
    fun `toEntity should correctly map Dto with null airDate`() {
        val result = season2DtoWithNullDate.toEntity()

        assertThat(result).isEqualTo(expectedSeason2)
    }

    @Test
    fun `toEntityList should map a list of Dtos to a list of Season entities`() {
        val dtoList = listOf(season1Dto, season2DtoWithNullDate)
        val result = dtoList.toEntityList()

        assertThat(result).isEqualTo(expectedSeasonList)
    }

    @Test
    fun `toEntityList should return an empty list when given an empty list`() {
        val emptyDtoList = emptyList<SeasonRemoteDto>()
        val result = emptyDtoList.toEntityList()

        assertThat(result).isEmpty()
    }

    private val season1Dto = SeasonRemoteDto(
        id = 1001L,
        title = "Season 1: The Beginning",
        airDate = "2022-01-01",
        seasonNumber = 1,
        episodeCount = 10
    )

    private val season2DtoWithNullDate = SeasonRemoteDto(
        id = 1002L,
        title = "Season 2: The Rise",
        airDate = null,
        seasonNumber = 2,
        episodeCount = 8
    )

    private val expectedSeason1 = Season(
        id = 1001L,
        seasonNumber = 1,
        title = "Season 1: The Beginning",
        episodeCount = 10
    )

    private val expectedSeason2 = Season(
        id = 1002L,
        seasonNumber = 2,
        title = "Season 2: The Rise",
        episodeCount = 8
    )

    private val expectedSeasonList = listOf(
        expectedSeason1,
        expectedSeason2
    )
}