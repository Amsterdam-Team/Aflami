package com.amsterdam.viewmodel.categories


import app.cash.turbine.test
import com.amsterdam.entity.category.MovieGenre
import com.amsterdam.viewmodel.categoriesDetails.movies.CategoriesMovieDetailsArgs
import com.amsterdam.viewmodel.categoriesDetails.movies.CategoriesMoviesDetailsPagingSource
import com.amsterdam.viewmodel.categoriesDetails.movies.CategoriesMoviesDetailsUiEffect
import com.amsterdam.viewmodel.categoriesDetails.movies.CategoriesMoviesDetailsUiState
import com.amsterdam.viewmodel.categoriesDetails.movies.CategoriesMoviesDetailsViewModel
import com.amsterdam.viewmodel.utils.TestDispatcherProvider
import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.AfterEachCallback
import org.junit.jupiter.api.extension.BeforeEachCallback
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.api.extension.ExtensionContext

@ExperimentalCoroutinesApi
class MainDispatcherExtension(
    private val testDispatcher: TestDispatcher = UnconfinedTestDispatcher()
) : BeforeEachCallback, AfterEachCallback {

    override fun beforeEach(context: ExtensionContext?) {
        Dispatchers.setMain(testDispatcher)
    }

    override fun afterEach(context: ExtensionContext?) {
        Dispatchers.resetMain()
    }
}

@OptIn(ExperimentalCoroutinesApi::class)
@ExtendWith(MainDispatcherExtension::class)
class CategoriesMoviesDetailsViewModelTest {
    private val categoriesMoviesDetailsPagingSource: CategoriesMoviesDetailsPagingSource =
        mockk(relaxed = true)
    private val args: CategoriesMovieDetailsArgs = mockk(relaxed = true)

    private val viewModel by lazy {
        CategoriesMoviesDetailsViewModel(
            categoriesMoviesDetailsPagingSource,
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