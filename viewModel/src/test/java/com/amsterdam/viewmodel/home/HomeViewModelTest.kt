package com.amsterdam.viewmodel.home

import com.amsterdam.domain.exceptions.NetworkException
import com.amsterdam.domain.models.Mood
import com.amsterdam.domain.useCase.home.GetContinueWatchingMoviesUseCase
import com.amsterdam.domain.useCase.home.GetMoviesByMoodUseCase
import com.amsterdam.domain.useCase.home.GetPopularMoviesUseCase
import com.amsterdam.domain.useCase.home.GetTopRatedMoviesUseCase
import com.amsterdam.domain.useCase.home.GetUpcomingMoviesUseCase
import com.amsterdam.entity.Movie
import com.amsterdam.entity.category.MovieGenre
import com.amsterdam.viewmodel.home.HomeEffect.NavigateToMovieDetailsEffect
import com.amsterdam.viewmodel.home.HomeUiState.HomeError
import com.amsterdam.viewmodel.shared.uiStates.MovieItemUiState
import com.amsterdam.viewmodel.utils.TestDispatcherProvider
import com.google.common.truth.Truth.assertThat
import io.mockk.clearMocks
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.LocalDate
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModelTest {

    private val getPopularMoviesUseCase: GetPopularMoviesUseCase = mockk()
    private val getTopRatedMoviesUseCase: GetTopRatedMoviesUseCase = mockk()
    private val getUpcomingMoviesUseCase: GetUpcomingMoviesUseCase = mockk()
    private val getContinueWatchingMoviesUseCase: GetContinueWatchingMoviesUseCase = mockk()
    private val homeUiStateMapper: HomeUiStateMapper = mockk()
    private val getMoviesByMoodUseCase: GetMoviesByMoodUseCase = mockk()
    private lateinit var dispatcherProvider: TestDispatcherProvider
    private lateinit var viewModel: HomeViewModel
    private lateinit var testScope: TestScope

    @BeforeEach
    fun setUp() {

        dispatcherProvider = TestDispatcherProvider()
        testScope = TestScope(dispatcherProvider.testDispatcher)
        viewModel = HomeViewModel(
            getUpcomingMoviesUseCase = getUpcomingMoviesUseCase,
            homeUiStateMapper = homeUiStateMapper,
            dispatcherProvider = dispatcherProvider,
            getPopularMoviesUseCase = getPopularMoviesUseCase,
            getTopRatedMoviesUseCase = getTopRatedMoviesUseCase,
            getContinueWatchingMoviesUseCase = getContinueWatchingMoviesUseCase,
            getMoviesByMoodUseCase = getMoviesByMoodUseCase
        )
    }

    @Test
    fun `init should load and expose upcoming movies mapped to UI state`() = testScope.runTest {
        coEvery { getUpcomingMoviesUseCase(any()) } returns upcomingMovies
        every { homeUiStateMapper.moviesToMoviesItemsUiState(upcomingMovies) } returns expectedUiState

        viewModel = HomeViewModel(
            getUpcomingMoviesUseCase = getUpcomingMoviesUseCase,
            homeUiStateMapper = homeUiStateMapper,
            dispatcherProvider = dispatcherProvider,
            getPopularMoviesUseCase = getPopularMoviesUseCase,
            getTopRatedMoviesUseCase = getTopRatedMoviesUseCase,
            getContinueWatchingMoviesUseCase = getContinueWatchingMoviesUseCase,
            getMoviesByMoodUseCase = getMoviesByMoodUseCase
        )
        advanceUntilIdle()

        assertThat(viewModel.state.value.upcomingMoviesSectionUiState.movies).isEqualTo(expectedUiState)

    }

    @Test
    fun `onClickRetryLoading should clear error state`() = testScope.runTest {
        coEvery { getUpcomingMoviesUseCase(any()) } throws NetworkException()

        viewModel.onClickRetryLoading()
        advanceUntilIdle()

        assertThat(viewModel.state.value.error).isInstanceOf(HomeError.NetworkError::class.java)

        clearMocks(getUpcomingMoviesUseCase)
        coEvery { getUpcomingMoviesUseCase(any()) } returns upcomingMovies
        every { homeUiStateMapper.moviesToMoviesItemsUiState(upcomingMovies) } returns expectedUiState

        viewModel.onClickRetryLoading()
        advanceUntilIdle()

        assertThat(viewModel.state.value.error).isNull()
    }

    @Test
    fun `onClickRetryLoading should clear error and reload movies`() = testScope.runTest {
        coEvery { getUpcomingMoviesUseCase(any()) } throws NetworkException()

        viewModel.onClickRetryLoading()
        advanceUntilIdle()

        assertThat(viewModel.state.value.error).isInstanceOf(HomeError.NetworkError::class.java)

        clearMocks(getUpcomingMoviesUseCase)
        coEvery { getUpcomingMoviesUseCase(any()) } returns upcomingMovies
        every { homeUiStateMapper.moviesToMoviesItemsUiState(upcomingMovies) } returns expectedUiState

        viewModel.onClickRetryLoading()
        advanceUntilIdle()
        assertThat(viewModel.state.value.error).isNull()

    }

    @Test
    fun `onClickUpcomingMovieCard should send NavigateToMovieDetailsEffect`() = testScope.runTest {
        val movieId = 101L
        val effects = mutableListOf<HomeEffect>()
        val job = launch { viewModel.effect.collect { it?.let { effects.add(it) } } }

        viewModel.onClickUpcomingMovieCard(movieId)
        advanceUntilIdle()
        job.cancel()

        assertThat(effects).contains(NavigateToMovieDetailsEffect(movieId))
    }

    @Test
    fun `onClickRetryLoading should show error when getUpcomingMoviesUseCase throws NetworkException`() =
        testScope.runTest {
            coEvery { getUpcomingMoviesUseCase(MovieGenre.ALL) } throws NetworkException()

            viewModel.onClickRetryLoading()
            advanceUntilIdle()

            assertThat(viewModel.state.value.error).isEqualTo(HomeError.NetworkError)
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

            assertThat(viewModel.state.value.upcomingMoviesSectionUiState.movies).isEqualTo(expectedComedyUiState)
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
                getPopularMoviesUseCase = getPopularMoviesUseCase,
                getTopRatedMoviesUseCase = getTopRatedMoviesUseCase,
                getContinueWatchingMoviesUseCase = getContinueWatchingMoviesUseCase,
                getMoviesByMoodUseCase = getMoviesByMoodUseCase
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
        val job = launch { viewModel.effect.collect { it?.let { effects.add(it) } } }
        viewModel.onClickSearch()
        advanceUntilIdle()
        job.cancel()
        assertThat(effects).contains(HomeEffect.NavigateToSearchScreenEffect)

    }
    @Test
    fun `onClickMovie should send NavigateToMovieDetailsEffect`() = testScope.runTest {
        val movieId = 101L
        val effects = mutableListOf<HomeEffect>()
        val job = launch { viewModel.effect.collect { it?.let { effects.add(it) } } }
        viewModel.onClickMovie(movieId)
        advanceUntilIdle()
        job.cancel()
        assertThat(effects).contains(NavigateToMovieDetailsEffect(movieId))
    }
    @Test
    fun `onClickShowAllContinueWatchingMovies should send NavigateToContinueWatchingMoviesScreen`() = testScope.runTest {
            val effects = mutableListOf<HomeEffect>()
            val job = launch { viewModel.effect.collect { it?.let { effects.add(it) } } }
            viewModel.onClickShowAllContinueWatchingMovies()
            advanceUntilIdle()
            job.cancel()
            assertThat(effects).contains(HomeEffect.NavigateToContinueWatchingMoviesScreen)
        }
    @Test
    fun `onClickShowAllToRatedMovies should send NavigateToContinueWatchingMoviesScreen`() = testScope.runTest {
        val effects = mutableListOf<HomeEffect>()
        val job = launch { viewModel.effect.collect { it?.let { effects.add(it) } } }
        viewModel.onClickShowAllToRatedMovies()
        advanceUntilIdle()
        job.cancel()
        assertThat(effects).contains(HomeEffect.NavigateToTopRatedMoviesEffect)
    }
  @Test
  fun `onClickMood should update selected mood in UI state`() = testScope.runTest {
      val selectedMood = Mood.IN_LOVE
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
    fun `onClickViewDetails should update openMovieDialog in UI state`() = testScope.runTest {
        viewModel.onClickViewDetails()
        advanceUntilIdle()
        assertThat(viewModel.state.value.moodPickerUiState.openMovieDialog).isFalse()
    }








    companion object {
        private val upcomingMovies = listOf(
            Movie(
                id = 101,
                name = "Galactic Odyssey",
                posterUrl = "galactic_odyssey.jpg",
                rating = 9.1f,
                description = "A space epic.",
                categories = listOf(MovieGenre.SCIENCE_FICTION),
                popularity = 98.6,
                originCountry = "US",
                runTimeInMinutes = 145,
                hasVideo = true,
                releaseDate = LocalDate(2024, 1, 1)
            ),
            Movie(
                id = 102,
                name = "Laugh Out Loud",
                posterUrl = "laugh_out_loud.jpg",
                rating = 7.8f,
                description = "British comedy.",
                categories = listOf(MovieGenre.COMEDY),
                popularity = 87.3,
                originCountry = "UK",
                runTimeInMinutes = 100,
                hasVideo = false,
                releaseDate = LocalDate(2024, 1, 1)

            )
        )

        private val expectedUiState = listOf(
            MovieItemUiState(
                id = 101,
                name = "Galactic Odyssey",
                posterImageUrl = "galactic_odyssey.jpg",
                rate = "9.1"
            ),
            MovieItemUiState(
                id = 102,
                name = "Laugh Out Loud",
                posterImageUrl = "laugh_out_loud.jpg",
                rate = "7.8"
            )
        )

        private val expectedComedyUiState = listOf(
            MovieItemUiState(
                id = 102L,
                name = "Laugh Out Loud",
                posterImageUrl = "laugh_out_loud.jpg",
                yearOfRelease = "2025",
                rate = "7.8"
            )
        )

        val comedyGenre = MovieGenre.COMEDY
        val upcomingComedyMovies = listOf(
            Movie(
                id = 102,
                name = "Laugh Out Loud",
                posterUrl = "laugh_out_loud.jpg",
                rating = 7.8f,
                description = "British comedy.",
                categories = listOf(MovieGenre.COMEDY),
                popularity = 87.3,
                originCountry = "UK",
                runTimeInMinutes = 100,
                hasVideo = false,
                releaseDate = LocalDate(2025, 1, 1)
            )
        )
    }
}