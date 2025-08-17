package com.amsterdam.repository.mapper

import com.amsterdam.domain.useCase.myRating.tvShow.GetUserRatedTvShowsUseCase.UserRatedTvShow
import com.amsterdam.repository.dto.remote.TvShowItemRemoteDto
import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test

class TvShowRateMapperTest {
    @Test
    fun `toTvShowUserRateEntity should map Dto to UserRatedTvShow entity`() {
        val result = ratedTvShowDto.toTvShowUserRateEntity(ratedTvShowDto)

        assertThat(result).isEqualTo(expectedUserRatedTvShow)
    }

    @Test
    fun `toTvShowUserRateEntity should map zero rating correctly`() {
        val result = ratedTvShowDtoWithZeroRating.toTvShowUserRateEntity(ratedTvShowDtoWithZeroRating)

        assertThat(result).isEqualTo(expectedUserRatedTvShowWithZeroRating)
    }

    @Test
    fun `toTvShowUserRateEntityList should map a list of Dtos correctly`() {
        val dtoList = listOf(ratedTvShowDto, ratedTvShowDtoWithZeroRating)
        val result = dtoList.toTvShowUserRateEntityList()

        assertThat(result).isEqualTo(expectedUserRatedList)
        assertThat(result).hasSize(2)
        assertThat(result[0].userRate).isEqualTo(9)
        assertThat(result[1].userRate).isEqualTo(0)
    }

    @Test
    fun `toTvShowUserRateEntityList should return an empty list when given an empty list`() {
        val emptyDtoList = emptyList<TvShowItemRemoteDto>()
        val result = emptyDtoList.toTvShowUserRateEntityList()

        assertThat(result).isEmpty()
    }

    private val ratedTvShowDto = TvShowItemRemoteDto(
        id = 201L,
        title = "Rated TV Show",
        overview = "An overview.",
        posterPath = "/poster.jpg",
        backdropPath = null,
        releaseDate = "2023-10-26",
        voteAverage = 8.8,
        genreIds = emptyList(),
        popularity = 1234.5,
        seasonCount = 3,
        originCountry = listOf("US"),
        adult = false,
        originalLanguage = "en",
        originalTitle = "Rated TV Show",
        voteCount = 1000,
        rating = 9.5f
    )

    private val ratedTvShowDtoWithZeroRating = ratedTvShowDto.copy(
        id = 202L,
        rating = 0f
    )

    private val expectedUserRatedTvShow = UserRatedTvShow(
        tvShow = ratedTvShowDto.toEntity(),
        userRate = 9
    )

    private val expectedUserRatedTvShowWithZeroRating = UserRatedTvShow(
        tvShow = ratedTvShowDtoWithZeroRating.toEntity(),
        userRate = 0
    )

    private val expectedUserRatedList = listOf(
        expectedUserRatedTvShow,
        expectedUserRatedTvShowWithZeroRating
    )
}