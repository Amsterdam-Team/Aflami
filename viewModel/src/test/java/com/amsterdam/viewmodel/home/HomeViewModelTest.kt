package com.amsterdam.viewmodel.home

import com.amsterdam.domain.exceptions.NetworkException
import com.amsterdam.domain.models.Mood
import com.amsterdam.domain.useCase.home.GetContinueWatchingScreenDataUseCase
import com.amsterdam.domain.useCase.home.GetHomeScreenDataUseCase
import com.amsterdam.domain.useCase.home.GetMoviesByMoodUseCase
import com.amsterdam.domain.useCase.home.GetUpcomingMoviesUseCase
import com.amsterdam.domain.useCase.preferences.ManageLocaleLanguageUseCase
import com.amsterdam.entity.Movie
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

    private val getHomeScreenDataUseCase: GetHomeScreenDataUseCase = mockk(relaxed = true)

    private val manageLocaleLanguageUseCase: ManageLocaleLanguageUseCase = mockk(relaxed = true)
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
            dispatcherProvider = dispatcherProvider,
            getMoviesByMoodUseCase = getMoviesByMoodUseCase,
            getHomeScreenDataUseCase = getHomeScreenDataUseCase,
            getContinueWatchingScreenDataUseCase = getContinueWatchingScreenDataUseCase,
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
    fun `init should subscribe to app language changes and trigger data loading`() =
        testScope.runTest {
            // Given
            coEvery { manageLocaleLanguageUseCase.getAppLanguage() } returns flowOf(
                ManageLocaleLanguageUseCase.Language.ENGLISH
            )

            // When
            viewModel = HomeViewModel(
                getUpcomingMoviesUseCase = getUpcomingMoviesUseCase,
                dispatcherProvider = dispatcherProvider,
                getMoviesByMoodUseCase = getMoviesByMoodUseCase,
                getHomeScreenDataUseCase = getHomeScreenDataUseCase,
                getContinueWatchingScreenDataUseCase = getContinueWatchingScreenDataUseCase,
                manageLocaleLanguageUseCase = manageLocaleLanguageUseCase,
            )
            advanceUntilIdle()

            // Then
            coVerify(exactly = 1) { getHomeScreenDataUseCase() }
            coVerify(exactly = 1) { getContinueWatchingScreenDataUseCase(pageSize = 10) }
        }


    @Test
    fun `when user selects upcoming genre, load and expose upcoming movies mapped to UI state`() =
        testScope.runTest {
            // Given
            val comedyGenre = MovieGenre.COMEDY
            val upcomingMovies = listOf(createMovie(id = 101, productionYear = 2020, rating = 8.5f))
            coEvery { getUpcomingMoviesUseCase(any()) } returns upcomingMovies

            // When
            viewModel.onChangeUpcomingMovieGenre(comedyGenre)
            advanceUntilIdle()

            // Then
            assertThat(viewModel.state.value.upcomingMoviesSectionUiState.movies).isEqualTo(
                listOf(UpcomingMoviesUiState(101, yearOfRelease = "2020", rate = "8.5"))
            )

        }

    @Test
    fun `onClickUpcomingMovieCard should send NavigateToMovieDetailsEffect`() = testScope.runTest {
        // Given
        val movieId = 101L
        val effects = mutableListOf<HomeEffect>()
        val job = launch { viewModel.effect.collect { it.let { effects.add(it) } } }

        // When
        viewModel.onClickUpcomingMovieCard(movieId)
        advanceUntilIdle()
        job.cancel()

        // Then
        assertThat(effects).contains(NavigateToMovieDetailsEffect(movieId))
    }

    val upcomingMovies = listOf(
        createMovie(id = 101),
        createMovie(id = 102)
    )

    @Test
    fun `onChangeUpcomingMovieGenre should updates selected genre in UI state`() =
        testScope.runTest {
            // Given
            val upcomingMovies = listOf(
                createMovie(id = 101),
                createMovie(id = 102)
            )
            val newGenre = MovieGenre.ACTION
            coEvery { getUpcomingMoviesUseCase(newGenre) } returns upcomingMovies

            // When
            viewModel.onChangeUpcomingMovieGenre(newGenre)
            advanceUntilIdle()

            // Then
            assertThat(viewModel.state.value.upcomingMoviesSectionUiState.getSelectedUpcomingMovieGenre()).isEqualTo(
                newGenre
            )
        }

    @Test
    fun `onChangeUpcomingMovieGenre triggers upcoming movies fetch with new genre`() =
        testScope.runTest {
            // Given
            val upcomingMovies = listOf(
                createMovie(id = 101, productionYear = 2020, rating = 8.5f)
            )
            val comedyGenre = MovieGenre.COMEDY
            coEvery { getUpcomingMoviesUseCase(comedyGenre) } returns upcomingMovies

            // When
            viewModel.onChangeUpcomingMovieGenre(comedyGenre)
            advanceUntilIdle()

            // Then
            assertThat(viewModel.state.value.upcomingMoviesSectionUiState.movies).isEqualTo(
                listOf(UpcomingMoviesUiState(101, yearOfRelease = "2020", rate = "8.5"))
            )
        }

    @Test
    fun `onChangeUpcomingMovieGenre should NOT trigger fetch if same genre is selected`() =
        testScope.runTest {
            // Given
            val genre = MovieGenre.ACTION
            val upcomingMovies = listOf<Movie>()
            coEvery { getUpcomingMoviesUseCase(genre) } returns upcomingMovies

            // When
            viewModel.onChangeUpcomingMovieGenre(genre)
            advanceUntilIdle()

            viewModel.onChangeUpcomingMovieGenre(genre)
            advanceUntilIdle()

            // Then
            coVerify(exactly = 1) { getUpcomingMoviesUseCase(genre) }
        }

    @Test
    fun `onClickRetryLoading should update error state, when NetworkError thrown`() =
        testScope.runTest {
            // Given
            coEvery { getUpcomingMoviesUseCase(any()) } throws NetworkException()

            // When
            viewModel.onClickRetryLoading()
            advanceUntilIdle()

            // Then
            assertThat(viewModel.state.value.error).isInstanceOf(HomeError.NetworkError::class.java)
        }

    @Test
    fun `onClickRetryLoading should update continue watching items, when called`() =
        testScope.runTest {
            // Given
            coEvery { getUpcomingMoviesUseCase(any()) } throws NetworkException()

            // When
            viewModel.onClickRetryLoading()
            advanceUntilIdle()

            // Then
            assertThat(viewModel.state.value.error).isInstanceOf(HomeError.NetworkError::class.java)
        }

    @Test
    fun `onClickRetryLoading should do nothing when section lists is not empty`() =
        testScope.runTest {
            // When
            viewModel.onClickRetryLoading()
            advanceUntilIdle()

            clearAllMocks()
            // Then
            coVerify(exactly = 0) { getUpcomingMoviesUseCase(MovieGenre.ACTION) }
            coVerify(exactly = 0) { getContinueWatchingScreenDataUseCase() }
            coVerify(exactly = 0) { getHomeScreenDataUseCase() }
        }


    @Test
    fun `onClickRetryLoading should call getContinueWatching successfully, when called`() =
        testScope.runTest {
            // Given
            coEvery {
                getContinueWatchingScreenDataUseCase(1, 10)
            } returns flow { }

            // When
            viewModel.onClickRetryLoading()
            advanceUntilIdle()

            // Then
            coVerify(exactly = 1) { getContinueWatchingScreenDataUseCase(1, 10) }
        }


    @Test
    fun `onClickSearch should send NavigateToSearchScreenEffect`() = testScope.runTest {
        // Given
        val effects = mutableListOf<HomeEffect>()
        val job = launch { viewModel.effect.collect { it.let { effects.add(it) } } }

        // When
        viewModel.onClickSearch()
        advanceUntilIdle()
        job.cancel()

        // Then
        assertThat(effects).contains(HomeEffect.NavigateToSearchScreenEffect)
    }

    @Test
    fun `onClickMovie should send NavigateToMovieDetailsEffect, when media type is movie`() =
        testScope.runTest {
            // Given
            val movieId = 101L
            val effects = mutableListOf<HomeEffect>()
            val job = launch { viewModel.effect.collect { it.let { effects.add(it) } } }

            // When
            viewModel.onClickMediaItem(movieId, MediaType.MOVIE)
            advanceUntilIdle()
            job.cancel()

            // Then
            assertThat(effects).contains(NavigateToMovieDetailsEffect(movieId))
        }

    @Test
    fun `onClickMovie should send NavigateToTvShowDetailsEffect, when media type is tv show`() =
        testScope.runTest {
            // Given
            val tvId = 101L
            val effects = mutableListOf<HomeEffect>()
            val job = launch { viewModel.effect.collect { it.let { effects.add(it) } } }

            // When
            viewModel.onClickMediaItem(tvId, MediaType.TV_SHOW)
            advanceUntilIdle()
            job.cancel()

            // Then
            assertThat(effects).contains(HomeEffect.NavigateToTvShowDetailsEffect(tvId))
        }

    @Test
    fun `onClickShowAllContinueWatchingMovies should send NavigateToContinueWatchingMoviesScreen`() =
        testScope.runTest {
            // Given
            val effects = mutableListOf<HomeEffect>()
            val job = launch { viewModel.effect.collect { it.let { effects.add(it) } } }

            // When
            viewModel.onClickShowAllContinueWatchingMovies()
            advanceUntilIdle()
            job.cancel()

            // Then
            assertThat(effects).contains(HomeEffect.NavigateToContinueWatchingMoviesScreen)
        }

    @Test
    fun `onClickShowAllToRatedMovies should send NavigateToContinueWatchingMoviesScreen`() =
        testScope.runTest {
            // Given
            val effects = mutableListOf<HomeEffect>()
            val job = launch { viewModel.effect.collect { it.let { effects.add(it) } } }

            // When
            viewModel.onClickShowAllToRatedMovies()
            advanceUntilIdle()
            job.cancel()

            // Then
            assertThat(effects).contains(HomeEffect.NavigateToTopRatedMoviesEffect)
        }

    @Test
    fun `onClickMood should update selected mood in UI state`() = testScope.runTest {
        // Given
        val selectedMood = Mood.ROMANTIC

        // When
        viewModel.onChangeMood(selectedMood)
        advanceUntilIdle()

        // Then
        assertThat(viewModel.state.value.moodPickerUiState.selectedMood).isEqualTo(selectedMood)
    }

    @Test
    fun `onClickGetNow should update isLoadingMovies in UI state`() = testScope.runTest {
        // When
        viewModel.onClickGetNow()
        advanceUntilIdle()

        // Then
        assertThat(viewModel.state.value.moodPickerUiState.isLoadingMovies).isTrue()
    }

    @Test
    fun `onClickGetNow should do nothing, when selected mood is null`() = testScope.runTest {
        // When
        viewModel.onClickGetNow()
        advanceUntilIdle()

        // Then
        assertThat(viewModel.state.value.moodPickerUiState.selectedMood).isNull()
        assertThat(viewModel.state.value.moodPickerUiState.movies).isEmpty()
    }


    @Test
    fun `onClickGetNow return movies, when selected mood is not null`() = testScope.runTest {
        // Given
        val selectedMood = Mood.ROMANTIC
        val moodMovies = listOf(
            createMovie(id = 101, productionYear = 2020, rating = 8.5f)
        )
        coEvery { getMoviesByMoodUseCase(selectedMood) } returns moodMovies

        // When
        viewModel.onChangeMood(selectedMood)
        advanceUntilIdle()

        viewModel.onClickGetNow()
        advanceUntilIdle()

        // Then
        assertThat(viewModel.state.value.moodPickerUiState.movies).isEqualTo(
            listOf(
                MoodPickerItemUiState(101, yearOfRelease = "2020", rate = "8.5")
            )
        )
    }

    @Test
    fun `onClickGetAnotherMovie return movies, when movie list is still not null`() =
        testScope.runTest {
            // Given
            val selectedMood = Mood.ROMANTIC
            val firstMoodMovies = listOf(
                createMovie(id = 101)
            )
            val secondMoodMovies = listOf(
                createMovie(id = 102),createMovie(id = 103)
            )
            coEvery{ getMoviesByMoodUseCase(selectedMood) } returns firstMoodMovies andThen secondMoodMovies

            // When and Then
            viewModel.onChangeMood(selectedMood)
            advanceUntilIdle()

            viewModel.onClickGetNow()
            advanceUntilIdle()

            assertThat(viewModel.state.value.moodPickerUiState.movies).hasSize(1)

            viewModel.onClickGetAnotherMovie()
            advanceUntilIdle()

            assertThat(viewModel.state.value.moodPickerUiState.movies).hasSize(2)
        }


    @Test
    fun `onClickGetAnotherMovie should do nothing and close dialogue, when no available movies`() =
        testScope.runTest {
            // Given
            val selectedMood = Mood.ROMANTIC
            coEvery { getMoviesByMoodUseCase(selectedMood) } returns emptyList()

            // When
            viewModel.onChangeMood(selectedMood)
            advanceUntilIdle()

            viewModel.onClickGetNow()
            advanceUntilIdle()

            viewModel.onClickGetAnotherMovie()
            advanceUntilIdle()


            // Then
            assertThat(viewModel.state.value.moodPickerUiState.openMovieDialog).isFalse()
        }


    @Test
    fun `onClickGetAnotherMovie should do nothing, when state  is loading`() =
        testScope.runTest {
            // When
            viewModel.onClickGetNow()
            viewModel.onClickGetAnotherMovie()
            advanceUntilIdle()

            // Then
            assertThat(viewModel.state.value.moodPickerUiState.isLoadingMovies).isTrue()
            assertThat(viewModel.state.value.moodPickerUiState.movies).isEmpty()
        }

    @Test
    fun `onClickViewDetails should update openMovieDialog in UI state`() = testScope.runTest {
        // When
        viewModel.onClickViewDetails()
        advanceUntilIdle()

        // Then
        assertThat(viewModel.state.value.moodPickerUiState.openMovieDialog).isFalse()
    }
}