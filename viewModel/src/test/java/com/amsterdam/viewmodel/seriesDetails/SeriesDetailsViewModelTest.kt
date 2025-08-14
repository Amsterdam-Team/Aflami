package com.amsterdam.viewmodel.seriesDetails

import com.amsterdam.domain.exceptions.NetworkException
import com.amsterdam.domain.exceptions.NoInternetException
import com.amsterdam.domain.useCase.authentication.GetsSessionType
import com.amsterdam.domain.useCase.details.GetEpisodeVideosUseCase
import com.amsterdam.domain.useCase.details.GetEpisodesBySeasonNumberUseCase
import com.amsterdam.domain.useCase.details.GetTvShowDetailsUseCase
import com.amsterdam.domain.useCase.myRating.tvShow.SetUserTvShowRatingUseCase
import com.amsterdam.domain.useCase.preferences.ManageLocaleLanguageUseCase
import com.amsterdam.domain.useCase.preferences.ManageLocaleLanguageUseCase.Language
import com.amsterdam.domain.utils.SessionType
import com.amsterdam.entity.Episode
import com.amsterdam.entity.Review
import com.amsterdam.entity.Season
import com.amsterdam.entity.TvShow
import com.amsterdam.entity.category.TvShowGenre
import com.amsterdam.viewmodel.seriesDetails.SeriesDetailsUiState.SeriesExtras
import com.amsterdam.viewmodel.utils.TestDispatcherProvider
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlinx.datetime.LocalDate
import kotlinx.datetime.toKotlinLocalDate
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SeriesDetailsViewModelTest {

    private lateinit var viewModel: SeriesDetailsViewModel
    private val getsSessionType: GetsSessionType = mockk(relaxed = true)
    private val testDispatcherProvider = TestDispatcherProvider()
    private val getTvShowDetailsUseCase: GetTvShowDetailsUseCase = mockk(relaxed = true)
    private val getEpisodesBySeasonNumberUseCase: GetEpisodesBySeasonNumberUseCase =
        mockk(relaxed = true)
    private val setUserTvShowRatingUseCase: SetUserTvShowRatingUseCase = mockk(relaxed = true)
    private val getEpisodeVideosUseCase: GetEpisodeVideosUseCase = mockk(relaxed = true)
    private val manageLocaleLanguageUseCase: ManageLocaleLanguageUseCase = mockk(relaxed = true)
    private var testArgs: SeriesDetailsArgs = mockk(relaxed = true)

    @BeforeEach
    fun setUp() {
        Dispatchers.setMain(testDispatcherProvider.testDispatcher)
        viewModel = SeriesDetailsViewModel(
            args = testArgs,
            getTvShowDetailsUseCase = getTvShowDetailsUseCase,
            getEpisodesBySeasonNumberUseCase = getEpisodesBySeasonNumberUseCase,
            getsSessionType = getsSessionType,
            setUserTvShowRatingUseCase = setUserTvShowRatingUseCase,
            manageLocaleLanguageUseCase = manageLocaleLanguageUseCase,
            getEpisodeVideosUseCase = getEpisodeVideosUseCase,
            dispatcherProvider = testDispatcherProvider
        )
        every { testArgs.tvShowId } returns 100L
        every { manageLocaleLanguageUseCase.getAppLanguage() } returns flowOf(Language.ENGLISH)
    }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
    }

    private fun mockSuccessfulTvShowDetails(userRate: Int? = null) {
        val tvShowDetails = GetTvShowDetailsUseCase.TvShowDetails(
            tvShow = TvShow(
                id = 100L,
                name = "Test Series",
                description = "Description",
                posterUrl = "",
                airDate = LocalDate(2023, 1, 1),
                categories = listOf(TvShowGenre.ACTION_ADVENTURE),
                rating = 8.0f,
                popularity = 100.0,
                seasonCount = 1,
                originCountry = "US",
                videoUrl = "video_url"
            ),
            reviews = emptyList(),
            actors = emptyList(),
            similarTvShows = emptyList(),
            gallery = emptyList(),
            posters = emptyList(),
            productionsCompanies = emptyList(),
            seasons = listOf(
                Season(
                    id = 1,
                    seasonNumber = 1,
                    episodeCount = 10,
                    title = ""
                )
            ),
            userRate = userRate
        )
        coEvery { getTvShowDetailsUseCase.invoke(100L) } returns tvShowDetails
    }

    @Disabled
    @Test
    fun `init should update state with received tv show id`() = runTest {
        // Given
        val tvShowId = 1L
        every { testArgs.tvShowId } returns tvShowId
        coEvery { getTvShowDetailsUseCase.invoke(any()) } throws NoInternetException()

        // When
        advanceUntilIdle()

        // Then
        assertThat(viewModel.state.value.tvShowId).isEqualTo(tvShowId)
    }


    @Disabled
    @Test
    fun `init should update error state and stop loading when failed load the data`() = runTest {
        // Given
        coEvery { getTvShowDetailsUseCase.invoke(any()) } throws NoInternetException()

        // When
        advanceUntilIdle()

        // Then
        assertThat(viewModel.state.value.networkError).isTrue()
        assertThat(viewModel.state.value.isLoading).isFalse()
    }

    @Disabled
    @Test
    fun `init should handle NetworkException and stop loading`() = runTest {
        // Given
        coEvery { getTvShowDetailsUseCase.invoke(any()) } throws NetworkException()

        // When
        advanceUntilIdle()

        // Then
        assertThat(viewModel.state.value.networkError).isTrue()
        assertThat(viewModel.state.value.isLoading).isFalse()
    }

    @Test
    fun `onClickSeriesExtraItem should update the state to be true for selected series extras`() =
        runTest {
            // Given
            val selectedExtras = SeriesExtras.REVIEWS
            advanceUntilIdle()

            // When
            viewModel.onClickSeriesExtraItem(selectedExtras)
            advanceUntilIdle()

            // Then
            val selectedItem = viewModel.state.value.extraItem.find { it.item == selectedExtras }
            assertThat(selectedItem?.isSelected).isTrue()
            assertThat(viewModel.state.value.extraItem.filter { it.isSelected }).hasSize(1)
        }

    @Test
    fun `onNavigateBack should send NavigateBack effect`() = runTest {
        // Given
        val effects = mutableListOf<SeriesDetailsEffect>()
        val collectJob = launch { viewModel.effect.collect { effects.add(it) } }

        // When
        viewModel.onNavigateBack()
        advanceUntilIdle()

        // Then
        assertThat(effects).containsExactly(SeriesDetailsEffect.NavigateBack)
        collectJob.cancel()
    }

    @Disabled
    @Test
    fun `onClickRetryButton should call getTvShowDetailsUseCase`() = runTest {
        // Given
        val tvShowId = 100L
        every { testArgs.tvShowId } returns tvShowId
        coEvery { getTvShowDetailsUseCase.invoke(any()) } throws NoInternetException()
        advanceUntilIdle()
        assertThat(viewModel.state.value.networkError).isTrue()

        // When
        mockSuccessfulTvShowDetails()
        viewModel.onClickRetryButton()
        advanceUntilIdle()

        // Then
        coVerify(exactly = 2) { getTvShowDetailsUseCase.invoke(tvShowId) }
        assertThat(viewModel.state.value.networkError).isFalse()
        assertThat(viewModel.state.value.isLoading).isFalse()
    }

    @Test
    fun `onClickShowAllCast should send NavigateToCastScreen effect`() = runTest {
        // Given
        val effects = mutableListOf<SeriesDetailsEffect>()
        val collectJob = launch { viewModel.effect.collect { effects.add(it) } }

        // When
        viewModel.onClickShowAllCast()
        advanceUntilIdle()

        // Then
        assertThat(effects).containsExactly(SeriesDetailsEffect.NavigateToCastScreen)
        collectJob.cancel()
    }

    @Test
    fun `onClickRate should show login dialog when user is a guest`() = runTest {
        // Given
        coEvery { getsSessionType.invoke() } returns SessionType.GUEST
        mockSuccessfulTvShowDetails()
        advanceUntilIdle()

        // When
        viewModel.onClickRate()
        advanceUntilIdle()

        // Then
        assertThat(viewModel.state.value.isLoginDialogVisible).isTrue()
    }

    @Disabled
    @Test
    fun `onClickSeasonMenu should expand season and load episodes when not loaded`() = runTest {
        // Given
        val seasonNumber = 1
        val episodes = listOf(
            Episode(
                id = 1L,
                title = "Episode 1",
                episodeNumber = 1,
                description = "Overview 1",
                episodeImageUrl = "Still Path 1",
                rating = 8.0f,
                airDate = LocalDate(2023, 1, 1),
                seasonNumber = seasonNumber,
                runTimeInMinutes = 45,
                videoUrl = "video_url_1"
            ),
            Episode(
                id = 2L,
                title = "Episode 2",
                episodeNumber = 2,
                description = "Overview 2",
                episodeImageUrl = "Still Path 2",
                rating = 8.5f,
                airDate = LocalDate(2023, 1, 8),
                seasonNumber = seasonNumber,
                runTimeInMinutes = 48,
                videoUrl = "video_url_2"
            )
        )
        mockSuccessfulTvShowDetails()
        coEvery { getEpisodesBySeasonNumberUseCase.invoke(any(), seasonNumber) } returns episodes
        advanceUntilIdle()

        // When
        viewModel.onClickSeasonMenu(seasonNumber)
        advanceUntilIdle()

        // Then
        val seasonState = viewModel.state.value.seasons.find { it.seasonNumber == seasonNumber }
        assertThat(seasonState?.isExpanded).isTrue()
        assertThat(seasonState?.episodes).hasSize(2)
        coVerify { getEpisodesBySeasonNumberUseCase.invoke(any(), seasonNumber) }
    }

    @Disabled
    @Test
    fun `onClickSeasonMenu should not call useCase if episodes are already loaded`() = runTest {
        // Given
        val seasonNumber = 1
        val episodes = listOf(
            Episode(
                id = 1L,
                title = "Episode 1",
                episodeNumber = 1,
                description = "Overview 1",
                episodeImageUrl = "Still Path 1",
                rating = 8.0f,
                airDate = LocalDate(2023, 1, 1),
                seasonNumber = seasonNumber,
                runTimeInMinutes = 45,
                videoUrl = "video_url_1"
            )
        )
        mockSuccessfulTvShowDetails()
        coEvery { getEpisodesBySeasonNumberUseCase.invoke(any(), seasonNumber) } returns episodes
        advanceUntilIdle()

        // First click to load episodes
        viewModel.onClickSeasonMenu(seasonNumber)
        advanceUntilIdle()
        assertThat(viewModel.state.value.seasons.first().episodes).hasSize(1)
        assertThat(viewModel.state.value.seasons.first().isExpanded).isTrue()

        // Second click to collapse
        viewModel.onClickSeasonMenu(seasonNumber)
        advanceUntilIdle()

        // Then
        val seasonState = viewModel.state.value.seasons.find { it.seasonNumber == seasonNumber }
        assertThat(seasonState?.isExpanded).isFalse()
        coVerify(exactly = 1) { getEpisodesBySeasonNumberUseCase.invoke(any(), seasonNumber) }
    }

    @Test
    fun `onNavigateToLoginClicked should send NavigateToLoginScreenEffect`() = runTest {
        // Given
        val effects = mutableListOf<SeriesDetailsEffect>()
        val collectJob = launch { viewModel.effect.collect { effects.add(it) } }

        // When
        viewModel.onNavigateToLoginClicked()
        advanceUntilIdle()

        // Then
        assertThat(effects).containsExactly(SeriesDetailsEffect.NavigateToLoginScreenEffect)
        collectJob.cancel()
    }

    @Test
    fun `onClickSimilarMovie should send NavigateToSeriesDetails effect`() = runTest {
        // Given
        val similarTvShowId = 200L
        val effects = mutableListOf<SeriesDetailsEffect>()
        val collectJob = launch { viewModel.effect.collect { effects.add(it) } }

        // When
        viewModel.onClickSimilarMovie(similarTvShowId)
        advanceUntilIdle()

        // Then
        assertThat(effects).containsExactly(
            SeriesDetailsEffect.NavigateToSeriesDetails(
                similarTvShowId
            )
        )
        collectJob.cancel()
    }

    @Test
    fun `onDescriptionExpansionToggled should toggle isDescriptionExpanded state`() = runTest {
        // Given
        advanceUntilIdle()
        assertThat(viewModel.state.value.isDescriptionExpanded).isFalse()

        // When
        viewModel.onDescriptionExpansionToggled()
        advanceUntilIdle()

        // Then
        assertThat(viewModel.state.value.isDescriptionExpanded).isTrue()
    }

    @Disabled
    @Test
    fun `onReviewExpansionToggled should toggle isExpanded for specific review`() = runTest {
        // Given
        val reviewUsername = "user1"
        val reviews = listOf(
            Review(
                id = 1L,
                reviewerName = "Author 1",
                reviewerUsername = reviewUsername,
                rating = 8.5f,
                content = "This is a great series!",
                date = java.time.LocalDate.of(2023, 1, 1).toKotlinLocalDate(),
                imageUrl = "url1"
            ),
            Review(
                id = 2L,
                reviewerName = "Author 2",
                reviewerUsername = "user2",
                rating = 7.0f,
                content = "It was okay.",
                date = java.time.LocalDate.of(2023, 2, 1).toKotlinLocalDate(),
                imageUrl = "url2"
            )
        )
        coEvery { getTvShowDetailsUseCase.invoke(any()) } returns GetTvShowDetailsUseCase.TvShowDetails(
            tvShow = TvShow(
                id = 100L,
                name = "Test Series",
                description = "Description",
                posterUrl = "",
                airDate = LocalDate(2023, 1, 1),
                categories = listOf(TvShowGenre.ACTION_ADVENTURE),
                rating = 8.0f,
                popularity = 100.0,
                seasonCount = 1,
                originCountry = "US"
            ),
            reviews = reviews, actors = emptyList(), similarTvShows = emptyList(),
            gallery = emptyList(), posters = emptyList(),
            productionsCompanies = emptyList(), seasons = emptyList(), userRate = null
        )
        advanceUntilIdle()

        // When
        viewModel.onReviewExpansionToggled(reviewUsername)
        advanceUntilIdle()

        // Then
        assertThat(viewModel.state.value.reviews.find { it.username == reviewUsername }?.isExpanded).isTrue()
    }

    @Test
    fun `onChangeRating should update selected star and enable submit button when rate changes`() =
        runTest {
            // Given
            val tvShowId = 100L
            val previousRating = 0
            every { testArgs.tvShowId } returns tvShowId
            coEvery { getsSessionType.invoke() } returns SessionType.LOGGED_IN
            mockSuccessfulTvShowDetails(userRate = previousRating)

            advanceUntilIdle()

            // When
            viewModel.onChangeRating(4)
            advanceUntilIdle()

            // Then
            assertThat(viewModel.state.value.rateDialogUiState.selectedStarIndex).isEqualTo(4)
            assertThat(viewModel.state.value.rateDialogUiState.isSubmittingEnabled).isTrue()
        }


    @Disabled
    @Test
    fun `onClickSubmit should call setUserTvShowRatingUseCase and send success effect`() = runTest {
        // Given
        val tvShowId = 100L
        val rating = 4
        every { testArgs.tvShowId } returns tvShowId
        coEvery { getsSessionType.invoke() } returns SessionType.LOGGED_IN
        coEvery { setUserTvShowRatingUseCase.setUserMovieRate(rating, tvShowId) } returns Unit

        val effects = mutableListOf<SeriesDetailsEffect>()
        val collectJob = launch { viewModel.effect.collect { effects.add(it) } }

        // When
        viewModel.onClickRate()
        viewModel.onChangeRating(rating)
        viewModel.onClickSubmit()
        advanceUntilIdle()

        // Then
        coVerify { setUserTvShowRatingUseCase.setUserMovieRate(rating, tvShowId) }
        assertThat(effects).contains(SeriesDetailsEffect.ShowRatingSuccessSnackBar)
        assertThat(viewModel.state.value.rateDialogUiState.isVisible).isFalse()
        collectJob.cancel()
    }
}