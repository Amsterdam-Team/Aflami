package com.amsterdam.viewmodel.home

import com.amsterdam.domain.exceptions.AflamiException
import com.amsterdam.domain.exceptions.NetworkException
import com.amsterdam.domain.useCase.continueWatching.GetContinueWatchingDataUseCase
import com.amsterdam.domain.useCase.mood.GetMoviesByMoodUseCase
import com.amsterdam.domain.useCase.popular.GetPopularMoviesUseCase
import com.amsterdam.domain.useCase.popular.GetPopularTvShowsUseCase
import com.amsterdam.domain.useCase.preferences.ManageLocaleLanguageUseCase
import com.amsterdam.domain.useCase.topRated.GetTopRatedMoviesUseCase
import com.amsterdam.domain.useCase.topRated.GetTopRatedTvShowsUseCase
import com.amsterdam.domain.useCase.upcoming.GetUpcomingMoviesUseCase
import com.amsterdam.entity.category.MovieGenre
import com.amsterdam.viewmodel.home.HomeEffect.NavigateToMovieDetailsEffect
import com.amsterdam.viewmodel.home.HomeUiState.HomeError
import com.amsterdam.viewmodel.home.HomeUiState.MoodPickerItemUiState
import com.amsterdam.viewmodel.home.HomeUiState.UpcomingMoviesUiState
import com.amsterdam.viewmodel.shared.uiStates.MediaType
import com.amsterdam.viewmodel.utils.TestDispatcherProvider
import com.amsterdam.viewmodel.utils.entityHelper.createMovie
import com.google.common.truth.Truth.assertThat
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
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

    private val manageLocaleLanguageUseCase: ManageLocaleLanguageUseCase = mockk(relaxed = true)
    private val getMoviesByMoodUseCase: GetMoviesByMoodUseCase = mockk(relaxed = true)
    private val getTopRatedMoviesUseCase: GetTopRatedMoviesUseCase = mockk(relaxed = true)
    private val getTopRatedTvShowsUseCase: GetTopRatedTvShowsUseCase = mockk(relaxed = true)
    private val getPopularMoviesUseCase: GetPopularMoviesUseCase = mockk(relaxed = true)
    private val getPopularTvShowsUseCase: GetPopularTvShowsUseCase = mockk(relaxed = true)
    private lateinit var dispatcherProvider: TestDispatcherProvider
    private lateinit var viewModel: HomeViewModel
    private lateinit var testScope: TestScope
    private lateinit var getUpcomingMoviesUseCase: GetUpcomingMoviesUseCase
    private lateinit var getContinueWatchingScreenDataUseCase: GetContinueWatchingDataUseCase

    @BeforeEach
    fun setUp() {
        dispatcherProvider = TestDispatcherProvider()
        testScope = TestScope(dispatcherProvider.testDispatcher)
        getUpcomingMoviesUseCase = mockk(relaxed = true)
        getContinueWatchingScreenDataUseCase = mockk(relaxed = true)
        viewModel = HomeViewModel(
            getUpcomingMoviesUseCase = getUpcomingMoviesUseCase,
            dispatcherProvider = dispatcherProvider,
            getMoviesByMoodUseCase = getMoviesByMoodUseCase,
            getContinueWatchingScreenDataUseCase = getContinueWatchingScreenDataUseCase,
            manageLocaleLanguageUseCase = manageLocaleLanguageUseCase,
            getTopRatedMoviesUseCase = getTopRatedMoviesUseCase,
            getTopRatedTvShowsUseCase = getTopRatedTvShowsUseCase,
            getPopularMoviesUseCase = getPopularMoviesUseCase,
            getPopularTvShowsUseCase = getPopularTvShowsUseCase,
        )
        Dispatchers.setMain(dispatcherProvider.testDispatcher)
    }

    @AfterEach
    fun tearDown() {
        clearAllMocks()
        Dispatchers.resetMain()
    }

    @Test
    fun `init should subscribe to app language changes and trigger data loading`() =
        testScope.runTest {
            coEvery { manageLocaleLanguageUseCase.getAppLanguage() } returns flowOf(
                ManageLocaleLanguageUseCase.Language.ENGLISH
            )

            viewModel = HomeViewModel(
                getUpcomingMoviesUseCase = getUpcomingMoviesUseCase,
                dispatcherProvider = dispatcherProvider,
                getMoviesByMoodUseCase = getMoviesByMoodUseCase,
                getContinueWatchingScreenDataUseCase = getContinueWatchingScreenDataUseCase,
                manageLocaleLanguageUseCase = manageLocaleLanguageUseCase,
                getTopRatedMoviesUseCase = getTopRatedMoviesUseCase,
                getTopRatedTvShowsUseCase = getTopRatedTvShowsUseCase,
                getPopularMoviesUseCase = getPopularMoviesUseCase,
                getPopularTvShowsUseCase = getPopularTvShowsUseCase,
            )
            advanceUntilIdle()

            coVerify(exactly = 1) { getTopRatedMoviesUseCase() }
            coVerify(exactly = 1) { getTopRatedTvShowsUseCase() }
            coVerify(exactly = 1) { getPopularMoviesUseCase() }
            coVerify(exactly = 1) { getPopularTvShowsUseCase() }
            coVerify(exactly = 1) { getContinueWatchingScreenDataUseCase(pageSize = 10) }
        }


    @Test
    fun `onChangeUpcomingMovieGenre should load and expose upcoming movies mapped to UI state when a genre is selected`() =
        testScope.runTest {
            coEvery { getUpcomingMoviesUseCase(any()) } returns upcomingMoviesList

            viewModel.onChangeUpcomingMovieGenre(defaultComedyGenre)
            advanceUntilIdle()

            assertThat(viewModel.state.value.upcomingMoviesSectionUiState.movies).isEqualTo(
                upcomingMoviesUiStateList
            )
        }

    @Test
    fun `onClickUpcomingMovieCard should send NavigateToMovieDetailsEffect when a movie card is clicked`() =
        testScope.runTest {
            val effects = mutableListOf<HomeEffect>()
            val job = launch { viewModel.effect.collect { effects.add(it) } }

            viewModel.onClickUpcomingMovieCard(defaultMovieId)
            advanceUntilIdle()
            job.cancel()

            assertThat(effects).contains(NavigateToMovieDetailsEffect(defaultMovieId))
        }

    @Test
    fun `onChangeUpcomingMovieGenre should update selected genre in UI state when a new genre is selected`() =
        testScope.runTest {
            coEvery { getUpcomingMoviesUseCase(defaultActionGenre) } returns upcomingMoviesList

            viewModel.onChangeUpcomingMovieGenre(defaultActionGenre)
            advanceUntilIdle()

            assertThat(viewModel.state.value.upcomingMoviesSectionUiState.getSelectedUpcomingMovieGenre()).isEqualTo(
                defaultActionGenre
            )
        }

    @Test
    fun `onChangeUpcomingMovieGenre should trigger upcoming movies fetch with new genre when a different genre is selected`() =
        testScope.runTest {
            coEvery { getUpcomingMoviesUseCase(defaultComedyGenre) } returns upcomingMoviesList

            viewModel.onChangeUpcomingMovieGenre(defaultComedyGenre)
            advanceUntilIdle()

            assertThat(viewModel.state.value.upcomingMoviesSectionUiState.movies).isEqualTo(
                upcomingMoviesUiStateList
            )
        }

    @Test
    fun `onChangeUpcomingMovieGenre should NOT trigger fetch when the same genre is selected again`() =
        testScope.runTest {
            coEvery { getUpcomingMoviesUseCase(defaultActionGenre) } returns upcomingMoviesList

            viewModel.onChangeUpcomingMovieGenre(defaultActionGenre)
            advanceUntilIdle()

            viewModel.onChangeUpcomingMovieGenre(defaultActionGenre)
            advanceUntilIdle()

            coVerify(exactly = 1) { getUpcomingMoviesUseCase(defaultActionGenre) }
        }

    @Test
    fun `onClickRetryLoading should update error state to NetworkError when a NetworkException is thrown`() =
        testScope.runTest {
            coEvery { getUpcomingMoviesUseCase(any()) } throws networkException

            viewModel.onClickRetryLoading()
            advanceUntilIdle()

            assertThat(viewModel.state.value.error).isInstanceOf(HomeError.NetworkError::class.java)
        }

    @Test
    fun `onClickRetryLoading should do nothing when section lists are not empty`() =
        testScope.runTest {
            viewModel.onClickRetryLoading()
            advanceUntilIdle()

            clearAllMocks()
            coVerify(exactly = 0) { getUpcomingMoviesUseCase(MovieGenre.ACTION) }
            coVerify(exactly = 0) { getContinueWatchingScreenDataUseCase() }
            coVerify(exactly = 0) { getTopRatedMoviesUseCase() }
        }


    @Test
    fun `onClickRetryLoading should call getContinueWatchingUseCase when triggered`() =
        testScope.runTest {
            coEvery {
                getContinueWatchingScreenDataUseCase(1, 10)
            } returns flow { }

            viewModel.onClickRetryLoading()
            advanceUntilIdle()

            coVerify(exactly = 1) { getContinueWatchingScreenDataUseCase(1, 10) }
        }


    @Test
    fun `onClickSearch should send NavigateToSearchScreenEffect when search is clicked`() =
        testScope.runTest {
            val effects = mutableListOf<HomeEffect>()
            val job = launch { viewModel.effect.collect { effects.add(it) } }

            viewModel.onClickSearch()
            advanceUntilIdle()
            job.cancel()

            assertThat(effects).contains(HomeEffect.NavigateToSearchScreenEffect)
        }

    @Test
    fun `onClickMediaItem should send NavigateToMovieDetailsEffect when media type is movie`() =
        testScope.runTest {
            val effects = mutableListOf<HomeEffect>()
            val job = launch { viewModel.effect.collect { effects.add(it) } }

            viewModel.onClickMediaItem(defaultMovieId, MediaType.MOVIE)
            advanceUntilIdle()
            job.cancel()

            assertThat(effects).contains(NavigateToMovieDetailsEffect(defaultMovieId))
        }

    @Test
    fun `onClickMediaItem should send NavigateToTvShowDetailsEffect when media type is TV show`() =
        testScope.runTest {
            val effects = mutableListOf<HomeEffect>()
            val job = launch { viewModel.effect.collect { effects.add(it) } }

            viewModel.onClickMediaItem(defaultTvId, MediaType.TV_SHOW)
            advanceUntilIdle()
            job.cancel()

            assertThat(effects).contains(HomeEffect.NavigateToTvShowDetailsEffect(defaultTvId))
        }

    @Test
    fun `onClickShowAllContinueWatchingMovies should send NavigateToContinueWatchingMoviesScreen when clicked`() =
        testScope.runTest {
            val effects = mutableListOf<HomeEffect>()
            val job = launch { viewModel.effect.collect { effects.add(it) } }

            viewModel.onClickShowAllContinueWatchingMovies()
            advanceUntilIdle()
            job.cancel()

            assertThat(effects).contains(HomeEffect.NavigateToContinueWatchingMoviesScreen)
        }

    @Test
    fun `onClickShowAllToRatedMovies should send NavigateToTopRatedMoviesEffect when clicked`() =
        testScope.runTest {
            val effects = mutableListOf<HomeEffect>()
            val job = launch { viewModel.effect.collect { effects.add(it) } }

            viewModel.onClickShowAllToRatedMovies()
            advanceUntilIdle()
            job.cancel()

            assertThat(effects).contains(HomeEffect.NavigateToTopRatedMoviesEffect)
        }

    @Test
    fun `onChangeMood should update selected mood in UI state`() = testScope.runTest {
        viewModel.onChangeMood(defaultSelectedMood)
        advanceUntilIdle()

        assertThat(viewModel.state.value.moodPickerUiState.selectedMood).isEqualTo(
            defaultSelectedMood
        )
    }

    @Test
    fun `onClickGetNow should update isLoadingMovies in UI state`() = testScope.runTest {
        viewModel.onClickGetNow()
        advanceUntilIdle()

        assertThat(viewModel.state.value.moodPickerUiState.isLoadingMovies).isTrue()
    }

    @Test
    fun `onClickGetNow should do nothing when selected mood is null`() = testScope.runTest {
        viewModel.onClickGetNow()
        advanceUntilIdle()

        assertThat(viewModel.state.value.moodPickerUiState.selectedMood).isNull()
        assertThat(viewModel.state.value.moodPickerUiState.movies).isEmpty()
    }


    @Test
    fun `onClickGetNow should return movies when selected mood is not null`() = testScope.runTest {
        coEvery { getMoviesByMoodUseCase(defaultSelectedMood) } returns upcomingMoviesList

        viewModel.onChangeMood(defaultSelectedMood)
        advanceUntilIdle()

        viewModel.onClickGetNow()
        advanceUntilIdle()

        assertThat(viewModel.state.value.moodPickerUiState.movies).isEqualTo(
            moodPickerItemUiStateList
        )
    }

    @Test
    fun `onClickGetAnotherMovie should return movies when movie list is not null`() =
        testScope.runTest {
            coEvery { getMoviesByMoodUseCase(defaultSelectedMood) } returns firstMoodMovies andThen secondMoodMovies

            viewModel.onChangeMood(defaultSelectedMood)
            advanceUntilIdle()

            viewModel.onClickGetNow()
            advanceUntilIdle()

            assertThat(viewModel.state.value.moodPickerUiState.movies).hasSize(1)

            viewModel.onClickGetAnotherMovie()
            advanceUntilIdle()

            assertThat(viewModel.state.value.moodPickerUiState.movies).hasSize(2)
        }


    @Test
    fun `onClickGetAnotherMovie should do nothing and close dialogue when no available movies`() =
        testScope.runTest {
            coEvery { getMoviesByMoodUseCase(defaultSelectedMood) } returns emptyList()

            viewModel.onChangeMood(defaultSelectedMood)
            advanceUntilIdle()

            viewModel.onClickGetNow()
            advanceUntilIdle()

            viewModel.onClickGetAnotherMovie()
            advanceUntilIdle()


            assertThat(viewModel.state.value.moodPickerUiState.openMovieDialog).isFalse()
        }


    @Test
    fun `onClickGetAnotherMovie should do nothing when state is loading`() =
        testScope.runTest {
            viewModel.onClickGetNow()
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

    @Test
    fun `onGetMoviesByMoodError should update state with error when GetMoviesByMoodUseCase throws`() =
        testScope.runTest {
            coEvery { getMoviesByMoodUseCase(any()) } throws aflamiException

            viewModel.onChangeMood(defaultSelectedMood)
            advanceUntilIdle()
            viewModel.onClickGetNow()
            advanceUntilIdle()

            assertThat(viewModel.state.value.moodPickerUiState.error).isNotNull()
        }

    @Test
    fun `onGetMoviesByMoodError should set isLoadingMovies to false when GetMoviesByMoodUseCase throws`() =
        testScope.runTest {
            coEvery { getMoviesByMoodUseCase(any()) } throws aflamiException

            viewModel.onChangeMood(defaultSelectedMood)
            advanceUntilIdle()
            viewModel.onClickGetNow()
            advanceUntilIdle()
            assertThat(viewModel.state.value.moodPickerUiState.isLoadingMovies).isFalse()
        }

    @Test
    fun `onGetMoviesByMoodError should set openMovieDialog to false when GetMoviesByMoodUseCase throws`() =
        testScope.runTest {
            coEvery { getMoviesByMoodUseCase(any()) } throws aflamiException

            viewModel.onChangeMood(defaultSelectedMood)
            advanceUntilIdle()
            viewModel.onClickGetNow()
            advanceUntilIdle()

            assertThat(viewModel.state.value.moodPickerUiState.openMovieDialog).isFalse()
        }

    @Test
    fun `onGetMoviesByMoodError should set selectedMood to null when GetMoviesByMoodUseCase throws`() =
        testScope.runTest {
            coEvery { getMoviesByMoodUseCase(any()) } throws aflamiException

            viewModel.onChangeMood(defaultSelectedMood)
            advanceUntilIdle()
            viewModel.onClickGetNow()
            advanceUntilIdle()

            assertThat(viewModel.state.value.moodPickerUiState.selectedMood).isNull()
        }

    @Test
    fun `onGetMoviesByMoodError should send ShowGetMoviesByMoodErrorSnackBar effect when GetMoviesByMoodUseCase throws`() =
        testScope.runTest {
            coEvery { getMoviesByMoodUseCase(any()) } throws aflamiException
            val effects = mutableListOf<HomeEffect>()
            val job = launch { viewModel.effect.collect { effects.add(it) } }

            viewModel.onChangeMood(defaultSelectedMood)
            advanceUntilIdle()
            viewModel.onClickGetNow()
            advanceUntilIdle()
            job.cancel()

            assertThat(effects).contains(HomeEffect.ShowGetMoviesByMoodErrorSnackBar)
        }

    private val defaultMovieId = 101L
    private val defaultTvId = 101L
    private val defaultSelectedMood = GetMoviesByMoodUseCase.Mood.ROMANTIC
    private val defaultComedyGenre = MovieGenre.COMEDY
    private val defaultActionGenre = MovieGenre.ACTION

    private val sampleMovie = createMovie(id = defaultMovieId, productionYear = 2020, rating = 8.5f)
    private val upcomingMoviesList = listOf(sampleMovie)
    private val upcomingMoviesUiStateList =
        listOf(UpcomingMoviesUiState(defaultMovieId, yearOfRelease = "2020", rate = "8.5"))

    private val firstMoodMovies = listOf(createMovie(id = defaultMovieId))
    private val secondMoodMovies = listOf(createMovie(id = 102), createMovie(id = 103))
    private val moodPickerItemUiStateList =
        listOf(MoodPickerItemUiState(defaultMovieId, yearOfRelease = "2020", rate = "8.5"))

    private val networkException = NetworkException()
    private val aflamiException = AflamiException()

}