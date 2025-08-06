package com.amsterdam.viewmodel.home

import com.amsterdam.domain.exceptions.NetworkException
import com.amsterdam.domain.models.Mood
import com.amsterdam.domain.useCase.home.GetContinueWatchingScreenDataUseCase
import com.amsterdam.domain.useCase.home.GetHomeScreenDataUseCase
import com.amsterdam.domain.useCase.home.GetMoviesByMoodUseCase
import com.amsterdam.domain.useCase.home.GetUpcomingMoviesUseCase
import com.amsterdam.domain.useCase.preferences.ManageLocaleLanguageUseCase
import com.amsterdam.entity.category.MovieGenre
import com.amsterdam.viewmodel.continueWatching.ContinueWatchingUiStateMapper
import com.amsterdam.viewmodel.home.HomeEffect.NavigateToMovieDetailsEffect
import com.amsterdam.viewmodel.home.HomeUiState.HomeError
import com.amsterdam.viewmodel.home.fake.FakeHomeScreenData.comedyGenre
import com.amsterdam.viewmodel.home.fake.FakeHomeScreenData.continueWatchingData
import com.amsterdam.viewmodel.home.fake.FakeHomeScreenData.expectedComedyUiState
import com.amsterdam.viewmodel.home.fake.FakeHomeScreenData.expectedMovies
import com.amsterdam.viewmodel.home.fake.FakeHomeScreenData.expectedUiState
import com.amsterdam.viewmodel.home.fake.FakeHomeScreenData.upcomingComedyMovies
import com.amsterdam.viewmodel.home.fake.FakeHomeScreenData.upcomingMovies
import com.amsterdam.viewmodel.shared.uiStates.media.MediaType.MOVIE
import com.amsterdam.viewmodel.shared.uiStates.media.MediaType.TV_SHOW
import com.amsterdam.viewmodel.utils.TestDispatcherProvider
import com.google.common.truth.Truth.assertThat
import io.mockk.clearAllMocks
import io.mockk.clearMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test


@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModelTest {

    private val getHomeScreenDataUseCase: GetHomeScreenDataUseCase = mockk(relaxed = true)

    private val manageLocaleLanguageUseCase: ManageLocaleLanguageUseCase = mockk(relaxed = true)
    private val homeUiStateMapper: HomeUiStateMapper = mockk(relaxed = true)
    private val continueWatchingUiStateMapper: ContinueWatchingUiStateMapper = mockk(relaxed = true)
    private val getMoviesByMoodUseCase: GetMoviesByMoodUseCase = mockk(relaxed = true)
    private lateinit var dispatcherProvider: TestDispatcherProvider
    private lateinit var viewModel: HomeViewModel
    private lateinit var testScope: TestScope
    private lateinit var getUpcomingMoviesUseCase: GetUpcomingMoviesUseCase
    private lateinit var getContinueWatchingScreenDataUseCase: GetContinueWatchingScreenDataUseCase

    @BeforeEach
    fun setUp() {
        dispatcherProvider = TestDispatcherProvider()
        testScope = TestScope(dispatcherProvider.testDispatcher)
        getUpcomingMoviesUseCase = mockk(relaxed = true)
        getContinueWatchingScreenDataUseCase = mockk(relaxed = true)
        viewModel = HomeViewModel(
            getUpcomingMoviesUseCase = getUpcomingMoviesUseCase,
            homeUiStateMapper = homeUiStateMapper,
            dispatcherProvider = dispatcherProvider,
            getMoviesByMoodUseCase = getMoviesByMoodUseCase,
            getHomeScreenDataUseCase = getHomeScreenDataUseCase,
            getContinueWatchingScreenDataUseCase = getContinueWatchingScreenDataUseCase,
            continueWatchingUiStateMapper = continueWatchingUiStateMapper,
            manageLocaleLanguageUseCase = manageLocaleLanguageUseCase,
        )
        Dispatchers.setMain(dispatcherProvider.testDispatcher)
    }

    @AfterEach
    fun tearDown() {
        clearAllMocks()
        Dispatchers.resetMain()
    }

    @Test
    fun `when user selects upcoming genre, load and expose upcoming movies mapped to UI state`() =
        testScope.runTest {
            coEvery { getUpcomingMoviesUseCase(any()) } returns upcomingMovies
            every { homeUiStateMapper.moviesToMoviesItemsUiState(upcomingMovies) } returns expectedUiState

            viewModel.onChangeUpcomingMovieGenre(comedyGenre)
            advanceUntilIdle()

            assertThat(viewModel.state.value.upcomingMoviesSectionUiState.movies).isEqualTo(
                expectedUiState
            )

        }

    @Test
    fun `onClickRetryLoading should update error state, when NetworkError thrown`() =
        testScope.runTest {
            coEvery { getUpcomingMoviesUseCase(any()) } throws NetworkException()

            viewModel.onClickRetryLoading()
            advanceUntilIdle()

            assertThat(viewModel.state.value.error).isInstanceOf(HomeError.NetworkError::class.java)
        }

    @Test
    fun `onClickRetryLoading should update continue watching items, when called`() =
        testScope.runTest {
            coEvery { getUpcomingMoviesUseCase(any()) } throws NetworkException()

            viewModel.onClickRetryLoading()
            advanceUntilIdle()

            assertThat(viewModel.state.value.error).isInstanceOf(HomeError.NetworkError::class.java)
        }


    @Test
    fun `onClickRetryLoading should call getContinueWatching successfully, when called`() =
        testScope.runTest {

            coEvery {
                getContinueWatchingScreenDataUseCase(
                    1,
                    10
                )
            } returns continueWatchingData


            viewModel.onClickRetryLoading()
            advanceUntilIdle()

            coVerify(exactly = 1) { getContinueWatchingScreenDataUseCase(1, 10) }
        }

    @Test
    fun `onClickUpcomingMovieCard should send NavigateToMovieDetailsEffect`() = testScope.runTest {
        val movieId = 101L
        val effects = mutableListOf<HomeEffect>()
        val job = launch { viewModel.effect.collect { it.let { effects.add(it) } } }

        viewModel.onClickUpcomingMovieCard(movieId)
        advanceUntilIdle()
        job.cancel()

        assertThat(effects).contains(NavigateToMovieDetailsEffect(movieId))
    }

    @Test
    fun `onChangeUpcomingMovieGenre should updates selected genre in UI state`() =
        testScope.runTest {
            val newGenre = MovieGenre.ACTION

            coEvery { getUpcomingMoviesUseCase(newGenre) } returns upcomingMovies
            every { homeUiStateMapper.moviesToMoviesItemsUiState(upcomingMovies) } returns expectedUiState

            viewModel.onChangeUpcomingMovieGenre(newGenre)
            advanceUntilIdle()

            assertThat(viewModel.state.value.upcomingMoviesSectionUiState.getSelectedUpcomingMovieGenre()).isEqualTo(
                newGenre
            )
        }

    @Test
    fun `onChangeUpcomingMovieGenre triggers upcoming movies fetch with new genre`() =
        testScope.runTest {

            coEvery { getUpcomingMoviesUseCase(comedyGenre) } returns upcomingComedyMovies
            every { homeUiStateMapper.moviesToMoviesItemsUiState(upcomingComedyMovies) } returns expectedComedyUiState

            viewModel.onChangeUpcomingMovieGenre(comedyGenre)
            advanceUntilIdle()

            assertThat(viewModel.state.value.upcomingMoviesSectionUiState.movies).isEqualTo(
                expectedComedyUiState
            )
        }

    @Test
    fun `onChangeUpcomingMovieGenre should NOT trigger fetch if same genre is selected`() =
        testScope.runTest {
            val genre = MovieGenre.ACTION

            coEvery { getUpcomingMoviesUseCase(genre) } returns upcomingMovies
            every { homeUiStateMapper.moviesToMoviesItemsUiState(upcomingMovies) } returns expectedUiState

            viewModel = HomeViewModel(
                getUpcomingMoviesUseCase = getUpcomingMoviesUseCase,
                homeUiStateMapper = homeUiStateMapper,
                dispatcherProvider = dispatcherProvider,
                getMoviesByMoodUseCase = getMoviesByMoodUseCase,
                getHomeScreenDataUseCase = getHomeScreenDataUseCase,
                getContinueWatchingScreenDataUseCase = getContinueWatchingScreenDataUseCase,
                continueWatchingUiStateMapper = continueWatchingUiStateMapper,
                manageLocaleLanguageUseCase = manageLocaleLanguageUseCase,
            )

            viewModel.onChangeUpcomingMovieGenre(genre)
            advanceUntilIdle()

            val previousState = viewModel.state.value
            clearMocks(getUpcomingMoviesUseCase)

            //changing to the same genre again
            viewModel.onChangeUpcomingMovieGenre(genre)
            advanceUntilIdle()

            assertThat(viewModel.state.value).isEqualTo(previousState)
        }

    @Test
    fun `onClickSearch should send NavigateToSearchScreenEffect`() = testScope.runTest {
        val effects = mutableListOf<HomeEffect>()
        val job = launch { viewModel.effect.collect { it.let { effects.add(it) } } }
        viewModel.onClickSearch()
        advanceUntilIdle()
        job.cancel()
        assertThat(effects).contains(HomeEffect.NavigateToSearchScreenEffect)

    }

    @Test
    fun `onClickMovie should send NavigateToMovieDetailsEffect, when media type is movie`() =
        testScope.runTest {
            val movieId = 101L
            val effects = mutableListOf<HomeEffect>()
            val job = launch { viewModel.effect.collect { it.let { effects.add(it) } } }
            viewModel.onClickMediaItem(movieId, MOVIE)
            advanceUntilIdle()
            job.cancel()
            assertThat(effects).contains(NavigateToMovieDetailsEffect(movieId))
        }

    @Test
    fun `onClickMovie should send NavigateToTvShowDetailsEffect, when media type is tv show`() =
        testScope.runTest {
            val tvId = 101L
            val effects = mutableListOf<HomeEffect>()
            val job = launch { viewModel.effect.collect { it.let { effects.add(it) } } }
            viewModel.onClickMediaItem(tvId, TV_SHOW)
            advanceUntilIdle()
            job.cancel()
            assertThat(effects).contains(HomeEffect.NavigateToTvShowDetailsEffect(tvId))
        }

    @Test
    fun `onClickShowAllContinueWatchingMovies should send NavigateToContinueWatchingMoviesScreen`() =
        testScope.runTest {
            val effects = mutableListOf<HomeEffect>()
            val job = launch { viewModel.effect.collect { it.let { effects.add(it) } } }
            viewModel.onClickShowAllContinueWatchingMovies()
            advanceUntilIdle()
            job.cancel()
            assertThat(effects).contains(HomeEffect.NavigateToContinueWatchingMoviesScreen)
        }

    @Test
    fun `onClickShowAllToRatedMovies should send NavigateToContinueWatchingMoviesScreen`() =
        testScope.runTest {
            val effects = mutableListOf<HomeEffect>()
            val job = launch { viewModel.effect.collect { it.let { effects.add(it) } } }
            viewModel.onClickShowAllToRatedMovies()
            advanceUntilIdle()
            job.cancel()
            assertThat(effects).contains(HomeEffect.NavigateToTopRatedMoviesEffect)
        }

    @Test
    fun `onClickMood should update selected mood in UI state`() = testScope.runTest {
        val selectedMood = Mood.ROMANTIC
        viewModel.onClickMood(selectedMood)
        advanceUntilIdle()
        assertThat(viewModel.state.value.moodPickerUiState.selectedMood).isEqualTo(selectedMood)
    }

    @Test
    fun `onClickGetNow should update isLoadingMovies in UI state`() = testScope.runTest {
        viewModel.onClickGetNow()
        advanceUntilIdle()
        assertThat(viewModel.state.value.moodPickerUiState.isLoadingMovies).isTrue()
    }

    @Test
    fun `onClickGetNow should do nothing, when selected mood is null`() = testScope.runTest {
        viewModel.onClickGetNow()
        advanceUntilIdle()

        assertThat(viewModel.state.value.moodPickerUiState.selectedMood).isNull()
        assertThat(viewModel.state.value.moodPickerUiState.movies).isEmpty()
    }

    @Test
    fun `onClickGetNow should not fetch movies, when selected mood is not null`() = testScope.runTest {
        coEvery {
            getMoviesByMoodUseCase(Mood.SAD)
        } returns expectedMovies

        viewModel.onClickGetNow()
        advanceUntilIdle()

        val result = homeUiStateMapper.moviesToMoviesItemsUiState(expectedMovies)
        assertThat(viewModel.state.value.moodPickerUiState.movies).isEqualTo(result)
    }


    @Test
    fun `onClickGetAnotherMovie should get movies, when called`() = testScope.runTest {
        coEvery {
            getMoviesByMoodUseCase(Mood.SAD)
        } returns expectedMovies

        viewModel.onClickGetAnotherMovie()
        advanceUntilIdle()

        val result = homeUiStateMapper.moviesToMoviesItemsUiState(expectedMovies)
        assertThat(viewModel.state.value.moodPickerUiState.movies).isEqualTo(result)
    }

    @Test
    fun `onClickGetAnotherMovie should do nothing, when state are  is loading`() = testScope.runTest {
        viewModel.onClickGetAnotherMovie()
        advanceUntilIdle()

        assertThat(viewModel.state.value.moodPickerUiState.isLoadingMovies).isTrue()
        assertThat(viewModel.state.value.moodPickerUiState.movies).isEmpty()
    }

    @Test
    fun `onClickViewDetails should update openMovieDialog in UI state`() = testScope.runTest {
        viewModel.onClickViewDetails()
        advanceUntilIdle()
        assertThat(viewModel.state.value.moodPickerUiState.openMovieDialog).isFalse()
    }
}