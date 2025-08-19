package com.amsterdam.viewmodel.categories


import app.cash.turbine.test
import com.amsterdam.domain.useCase.details.GetMoviesByGenreUseCase
import com.amsterdam.entity.category.MovieGenre
import com.amsterdam.viewmodel.categoriesDetails.movies.CategoriesMovieDetailsArgs
import com.amsterdam.viewmodel.categoriesDetails.movies.CategoriesMoviesDetailsUiEffect
import com.amsterdam.viewmodel.categoriesDetails.movies.CategoriesMoviesDetailsUiState
import com.amsterdam.viewmodel.categoriesDetails.movies.CategoriesMoviesDetailsViewModel
import com.amsterdam.viewmodel.utils.TestDispatcherProvider
import com.amsterdam.viewmodel.utils.TestExtension
import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@OptIn(ExperimentalCoroutinesApi::class)
@ExtendWith(TestExtension::class)
class CategoriesMoviesDetailsViewModelTest {
    private val getMoviesByGenreIdUseCase: GetMoviesByGenreUseCase = mockk(relaxed = true)
    private val args: CategoriesMovieDetailsArgs = mockk(relaxed = true)

    private val viewModel by lazy {
        CategoriesMoviesDetailsViewModel(
            getMoviesByGenreIdUseCase,
            categoriesMovieDetailsArgs = args,
            dispatcherProvider = TestDispatcherProvider()
        )
    }

    private lateinit var initialState: CategoriesMoviesDetailsUiState

    @BeforeEach
    fun setUp() {
        every { args.genreName } returns MovieGenre.COMEDY.name
        initialState = viewModel.state.value
    }

    @Test
    fun `onClickBack should send NavigateBack effect`() = runTest {
        viewModel.effect.test {
            viewModel.onClickBack()
            assertThat(awaitItem()).isEqualTo(CategoriesMoviesDetailsUiEffect.NavigateBack)
        }
    }

    @Test
    fun `onClickMovieCard should send NavigateToMovieDetails effect`() = runTest {
        viewModel.effect.test {
            viewModel.onClickMovieCard(movieId)
            assertThat(awaitItem()).isEqualTo(
                CategoriesMoviesDetailsUiEffect.NavigateToMovieDetails(movieId)
            )
        }
    }

    @Test
    fun `onClickGenre with same genre should not reload`() = runTest {
        advanceUntilIdle()
        val stableState = viewModel.state.value
        viewModel.onClickGenre(stableState.selectedGenre)
        assertThat(viewModel.state.value).isEqualTo(stableState)
    }

    @Test
    fun `onClickGenre with different genre should update state and reload movies`() = runTest {
        val newGenre = MovieGenre.COMEDY
        viewModel.onClickGenre(newGenre)
        advanceUntilIdle()
        assertThat(viewModel.state.value.selectedGenre).isEqualTo(newGenre)
    }

    @Test
    fun `onClickRetryRequest should reload movies`() = runTest {
        viewModel.onClickRetryRequest()
        assertThat(viewModel.state.value.isLoading).isTrue()
        advanceUntilIdle()
        assertThat(viewModel.state.value.isLoading).isFalse()
    }

    private val movieId = 42L
}