package com.amsterdam.domain.useCase.details

import com.amsterdam.domain.exceptions.AflamiException
import com.amsterdam.domain.repository.TvShowRepository
import com.amsterdam.domain.useCase.common.AddTvShowWatchHistoryUseCase
import com.amsterdam.domain.useCase.utils.fakeTvShowList
import com.amsterdam.entity.Actor
import com.amsterdam.entity.Gender
import com.amsterdam.entity.ProductionCompany
import com.amsterdam.entity.Review
import com.amsterdam.entity.Season
import com.amsterdam.entity.TvShow
import com.amsterdam.entity.category.TvShowGenre
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coJustRun
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.LocalDate
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class GetTvShowDetailsUseCaseTest {
    private val tvShowRepository: TvShowRepository = mockk(relaxed = true)
    private val addTvShowWatchHistoryUseCase: AddTvShowWatchHistoryUseCase = mockk(relaxed = true)
    private val getTvShowDetailsUseCase by lazy {
        GetTvShowDetailsUseCase(tvShowRepository, addTvShowWatchHistoryUseCase)
    }

    private val fakeActors = listOf(
        Actor(1, "Actor A", "a.jpg", 90.0, Gender.Male)
    )
    private val fakeSeasons = listOf(
        Season(1, 1, "Season 1", 10)
    )
    private val fakeReviews = listOf(
        Review(1, "Reviewer1", "user1", 4.5f, "Great", LocalDate(2023, 1, 1), "url1")
    )
    private val fakeSimilarTvShows = listOf(
        TvShow(
            2L, "Similar TV Show", "Desc", "poster2.jpg", LocalDate(2021, 1, 1),
            listOf(TvShowGenre.DRAMA), 7.0f, 90.0, 3, "USA",
        )
    )
    private val fakeGallery = listOf("gallery1.jpg", "gallery2.jpg")
    private val fakePosters = listOf("poster1.jpg", "poster2.jpg")
    private val fakeProductionCompanies = listOf(
        ProductionCompany(1, "logo.jpg", "Company A", "USA")
    )

    @BeforeEach
    fun setUp() {
        coEvery { tvShowRepository.getTvShowDetails(any()) } returns fakeTvShowDetails
        coJustRun { addTvShowWatchHistoryUseCase(any()) }
    }

    @Test
    fun `should fetch all tv show details and return TvShowDetails object`() = runTest {
        val result = getTvShowDetailsUseCase(tvShowId)

        assertThat(result).isEqualTo(fakeTvShowDetails)
    }

    @Test
    fun `should throw AflamiException when repository call fails`() = runTest {
        coEvery { tvShowRepository.getTvShowDetails(any()) } throws AflamiException()

        assertThrows<AflamiException> { getTvShowDetailsUseCase(tvShowId) }
    }

    @Test
    fun `should return empty lists when repository returns empty collections`() = runTest {
        coEvery { tvShowRepository.getTvShowDetails(any()) } returns fakeEmptyTvShowDetails

        val result = getTvShowDetailsUseCase(tvShowId)

        assertThat(result).isEqualTo(fakeEmptyTvShowDetails)
    }

    @Test
    fun `should handle a negative tvShowId gracefully`() = runTest {
        coEvery { tvShowRepository.getTvShowDetails(invalidTvShowId) } returns invalidFakeTvShowDetails

        val result = getTvShowDetailsUseCase(invalidTvShowId)

        assertThat(result.tvShow.id).isEqualTo(invalidTvShowId)
    }

    private val tvShowId = 1L

    private val invalidTvShowId = -1L

    private val fakeTvShowDetails = GetTvShowDetailsUseCase.TvShowDetails(
        tvShow = fakeTvShowList.first(),
        actors = fakeActors,
        seasons = fakeSeasons,
        reviews = fakeReviews,
        similarTvShows = fakeSimilarTvShows,
        gallery = fakeGallery,
        posters = fakePosters,
        productionsCompanies = fakeProductionCompanies,
        userRate = 1
    )

    private val fakeEmptyTvShowDetails = GetTvShowDetailsUseCase.TvShowDetails(
        tvShow = fakeTvShowList.first(),
        actors = emptyList(),
        seasons = emptyList(),
        reviews = emptyList(),
        similarTvShows = emptyList(),
        gallery = emptyList(),
        posters = emptyList(),
        productionsCompanies = emptyList(),
        userRate = 1
    )

    private val invalidFakeTvShowDetails = GetTvShowDetailsUseCase.TvShowDetails(
        tvShow = fakeTvShowList.first().copy(id = invalidTvShowId),
        actors = emptyList(),
        seasons = emptyList(),
        reviews = emptyList(),
        similarTvShows = emptyList(),
        gallery = emptyList(),
        posters = emptyList(),
        productionsCompanies = emptyList(),
        userRate = 1
    )
}