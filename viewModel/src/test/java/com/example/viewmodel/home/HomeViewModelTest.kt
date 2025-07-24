package com.example.viewmodel.home

import com.example.domain.exceptions.NetworkException
import com.example.domain.useCase.GetHomeScreenDataUseCase
import com.example.domain.useCase.GetUpcomingMoviesUseCase
import com.example.entity.Movie
import com.example.entity.category.MovieGenre
import com.example.viewmodel.home.HomeEffect.NavigateToMovieDetailsEffect
import com.example.viewmodel.home.HomeUiState.HomeError
import com.example.viewmodel.shared.uiStates.MovieItemUiState
import com.example.viewmodel.utils.TestDispatcherProvider
import com.google.common.truth.Truth.assertThat
import io.mockk.clearMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModelTest {

    private lateinit var getUpcomingMoviesUseCase: GetUpcomingMoviesUseCase
    private lateinit var getHomeScreenDataUseCase: GetHomeScreenDataUseCase
    private lateinit var homeUiStateMapper: HomeUiStateMapper
    private lateinit var dispatcherProvider: TestDispatcherProvider
    private lateinit var viewModel: HomeViewModel
    private lateinit var testScope: TestScope

    @BeforeEach
    fun setUp() {
        getHomeScreenDataUseCase = mockk()
        getUpcomingMoviesUseCase = mockk()
        homeUiStateMapper = mockk()
        dispatcherProvider = TestDispatcherProvider()
        testScope = TestScope(dispatcherProvider.testDispatcher)
        viewModel = HomeViewModel(
            getUpcomingMoviesUseCase = getUpcomingMoviesUseCase,
            homeUiStateMapper = homeUiStateMapper,
            getHomeScreenDataUseCase = getHomeScreenDataUseCase,
            dispatcherProvider = dispatcherProvider
        )
    }

    @Test
    fun `init should load and expose upcoming movies mapped to UI state`() = testScope.runTest {
        coEvery { getUpcomingMoviesUseCase(any()) } returns upcomingMovies
        every { homeUiStateMapper.moviesToMoviesItemsUiState(upcomingMovies) } returns expectedUiState

        viewModel = HomeViewModel(
            getHomeScreenDataUseCase = getHomeScreenDataUseCase,
            getUpcomingMoviesUseCase = getUpcomingMoviesUseCase,
            homeUiStateMapper = homeUiStateMapper,
            dispatcherProvider = dispatcherProvider
        )
        advanceUntilIdle()

        assertThat(viewModel.state.value.upcomingMovies).isEqualTo(expectedUiState)
    }

    @Test
    fun `onClickRetryLoading should clear error state`() = testScope.runTest {
        coEvery { getUpcomingMoviesUseCase(any()) } throws NetworkException()

        viewModel.onClickRetryLoading()
        advanceUntilIdle()

        assertThat(viewModel.state.value.error).isInstanceOf(HomeError.NetworkError::class.java)

        clearMocks(getUpcomingMoviesUseCase)
        coEvery { getUpcomingMoviesUseCase(any()) } returns upcomingMovies
        every { homeUiStateMapper.moviesToMoviesItemsUiState(upcomingMovies)  } returns expectedUiState

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

        assertThat(viewModel.state.value.upcomingMovies).isEqualTo(expectedUiState)
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

            assertThat(viewModel.state.value.getSelectedUpcomingMovieGenre()).isEqualTo(newGenre)
        }

    @Test
    fun `onChangeUpcomingMovieGenre triggers upcoming movies fetch with new genre`() =
        testScope.runTest {

            coEvery { getUpcomingMoviesUseCase(comedyGenre) } returns upcomingComedyMovies
            every { homeUiStateMapper.moviesToMoviesItemsUiState(upcomingComedyMovies) } returns expectedComedyUiState

            viewModel.onChangeUpcomingMovieGenre(comedyGenre)
            advanceUntilIdle()

            assertThat(viewModel.state.value.upcomingMovies).isEqualTo(expectedComedyUiState)
        }

    @Test
    fun `onChangeUpcomingMovieGenre should NOT trigger fetch if same genre is selected`() =
        testScope.runTest {
            val genre = MovieGenre.ACTION

            coEvery { getUpcomingMoviesUseCase(genre) } returns upcomingMovies
            every { homeUiStateMapper.moviesToMoviesItemsUiState(upcomingMovies) } returns expectedUiState

            viewModel = HomeViewModel(
                getHomeScreenDataUseCase = getHomeScreenDataUseCase,
                getUpcomingMoviesUseCase = getUpcomingMoviesUseCase,
                homeUiStateMapper = homeUiStateMapper,
                dispatcherProvider = dispatcherProvider
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

    companion object {
        private val upcomingMovies = listOf(
            Movie(
                id = 101,
                name = "Galactic Odyssey",
                posterUrl = "galactic_odyssey.jpg",
                rating = 9.1f,
                description = "A space epic.",
                productionYear = 2025u,
                categories = listOf(MovieGenre.SCIENCE_FICTION),
                popularity = 98.6,
                originCountry = "US",
                runTime = 145,
                hasVideo = true
            ),
            Movie(
                id = 102,
                name = "Laugh Out Loud",
                posterUrl = "laugh_out_loud.jpg",
                rating = 7.8f,
                description = "British comedy.",
                productionYear = 2025u,
                categories = listOf(MovieGenre.COMEDY),
                popularity = 87.3,
                originCountry = "UK",
                runTime = 100,
                hasVideo = false
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
                productionYear = 2025u,
                categories = listOf(MovieGenre.COMEDY),
                popularity = 87.3,
                originCountry = "UK",
                runTime = 100,
                hasVideo = false
            )
        )
    }
}