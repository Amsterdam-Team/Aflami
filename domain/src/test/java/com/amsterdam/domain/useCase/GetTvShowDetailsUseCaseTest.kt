package com.amsterdam.domain.useCase

import com.amsterdam.domain.exceptions.AflamiException
import com.amsterdam.domain.repository.TvShowRepository
import com.amsterdam.domain.useCase.common.AddTvShowWatchHistoryUseCase
import com.amsterdam.domain.useCase.details.GetTvShowDetailsUseCase
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
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.LocalDate
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class GetTvShowDetailsUseCaseTest {
    private lateinit var tvShowRepository: TvShowRepository
    private lateinit var addTvShowWatchHistoryUseCase: AddTvShowWatchHistoryUseCase
    private lateinit var getTvShowDetailsUseCase: GetTvShowDetailsUseCase

    private val fakeTvShow = TvShow(
        id = 1L, name = "Test TV Show", description = "Desc", posterUrl = "poster.jpg",
        airDate = LocalDate(2020, 1, 1), categories = listOf(TvShowGenre.DRAMA, TvShowGenre.COMEDY),
        rating = 8.0f, popularity = 100.0, seasonCount = 5, originCountry = "USA",
        productionCompanies = emptyList()
    )
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
            listOf(TvShowGenre.DRAMA), 7.0f, 90.0, 3, "USA", emptyList()
        )
    )
    private val fakeGallery = listOf("gallery1.jpg", "gallery2.jpg")
    private val fakePosters = listOf("poster1.jpg", "poster2.jpg")
    private val fakeProductionCompanies = listOf(
        ProductionCompany(1, "logo.jpg", "Company A", "USA")
    )

    @BeforeEach
    fun setUp() {
        tvShowRepository = mockk(relaxed = true)
        addTvShowWatchHistoryUseCase = mockk(relaxed = true)
        getTvShowDetailsUseCase =
            GetTvShowDetailsUseCase(tvShowRepository, addTvShowWatchHistoryUseCase)

        coEvery { tvShowRepository.getTvShowDetails(any()) } returns GetTvShowDetailsUseCase.TvShowDetails(
            tvShow = fakeTvShow,
            actors = fakeActors,
            seasons = fakeSeasons,
            reviews = fakeReviews,
            similarTvShows = fakeSimilarTvShows,
            gallery = fakeGallery,
            posters = fakePosters,
            productionsCompanies = fakeProductionCompanies
        )
        coJustRun { addTvShowWatchHistoryUseCase(any()) }
    }

    @Test
    fun `should fetch all tv show details and return TvShowDetails object`() = runTest {
        // Given
        val tvShowId = 1L

        // When
        val result = getTvShowDetailsUseCase(tvShowId)

        // Then
        coVerify(exactly = 1) { tvShowRepository.getTvShowDetails(tvShowId) }
        coVerify(exactly = 1) { addTvShowWatchHistoryUseCase(tvShowId) }
        assertThat(result.tvShow).isEqualTo(fakeTvShow)
        assertThat(result.actors).isEqualTo(fakeActors)
        assertThat(result.seasons).isEqualTo(fakeSeasons)
        assertThat(result.reviews).isEqualTo(fakeReviews)
        assertThat(result.similarTvShows).isEqualTo(fakeSimilarTvShows)
        assertThat(result.gallery).isEqualTo(fakeGallery)
        assertThat(result.posters).isEqualTo(fakePosters)
        assertThat(result.productionsCompanies).isEqualTo(fakeProductionCompanies)
    }

    @Test
    fun `should throw AflamiException when repository call fails`() = runTest {
        // Given
        val tvShowId = 1L
        coEvery { tvShowRepository.getTvShowDetails(any()) } throws AflamiException()

        // When & Then
        assertThrows<AflamiException> { getTvShowDetailsUseCase(tvShowId) }
        coVerify(exactly = 1) { tvShowRepository.getTvShowDetails(tvShowId) }
        coVerify(exactly = 0) { addTvShowWatchHistoryUseCase(any()) }
    }

    @Test
    fun `should handle empty lists from repository gracefully`() = runTest {
        // Given
        val tvShowId = 1L
        coEvery { tvShowRepository.getTvShowDetails(any()) } returns GetTvShowDetailsUseCase.TvShowDetails(
            tvShow = fakeTvShow,
            actors = emptyList(),
            seasons = emptyList(),
            reviews = emptyList(),
            similarTvShows = emptyList(),
            gallery = emptyList(),
            posters = emptyList(),
            productionsCompanies = emptyList()
        )

        // When
        val result = getTvShowDetailsUseCase(tvShowId)

        // Then
        assertThat(result.actors).isEmpty()
        assertThat(result.seasons).isEmpty()
        assertThat(result.reviews).isEmpty()
        assertThat(result.similarTvShows).isEmpty()
        assertThat(result.gallery).isEmpty()
        assertThat(result.posters).isEmpty()
        assertThat(result.productionsCompanies).isEmpty()
    }

    @Test
    fun `should handle a negative tvShowId gracefully`() = runTest {
        // Given
        val invalidTvShowId = -1L
        coEvery { tvShowRepository.getTvShowDetails(invalidTvShowId) } returns GetTvShowDetailsUseCase.TvShowDetails(
            tvShow = fakeTvShow.copy(id = invalidTvShowId),
            actors = emptyList(),
            seasons = emptyList(),
            reviews = emptyList(),
            similarTvShows = emptyList(),
            gallery = emptyList(),
            posters = emptyList(),
            productionsCompanies = emptyList()
        )

        // When
        val result = getTvShowDetailsUseCase(invalidTvShowId)

        // Then
        coVerify(exactly = 1) { tvShowRepository.getTvShowDetails(invalidTvShowId) }
        coVerify(exactly = 1) { addTvShowWatchHistoryUseCase(invalidTvShowId) }
        assertThat(result.tvShow.id).isEqualTo(invalidTvShowId)
    }
}