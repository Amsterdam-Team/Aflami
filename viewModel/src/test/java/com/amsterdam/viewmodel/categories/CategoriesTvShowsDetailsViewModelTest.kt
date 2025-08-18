package com.amsterdam.viewmodel.categories

import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import app.cash.turbine.test
import com.amsterdam.entity.category.TvShowGenre
import com.amsterdam.viewmodel.categoriesDetails.tvShow.CategoriesTvShowDetailsPagingSource
import com.amsterdam.viewmodel.categoriesDetails.tvShow.CategoriesTvShowsDetailsArgs
import com.amsterdam.viewmodel.categoriesDetails.tvShow.CategoriesTvShowsDetailsUiEffect
import com.amsterdam.viewmodel.categoriesDetails.tvShow.CategoriesTvShowsDetailsUiState
import com.amsterdam.viewmodel.categoriesDetails.tvShow.CategoriesTvShowsDetailsViewModel
import com.amsterdam.viewmodel.utils.TestDispatcherProvider
import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MainDispatcherExtension::class)
class CategoriesTvShowsDetailsViewModelTest {
    private val categoriesTvShowDetailsPagingSource: CategoriesTvShowDetailsPagingSource = mockk(relaxed = true)
    private val args: CategoriesTvShowsDetailsArgs = mockk(relaxed = true)
    private val viewModel by lazy {
        CategoriesTvShowsDetailsViewModel(
            categoriesTvShowDetailsPagingSource,
            categoriesTvShowsDetailsArgs = args,
            dispatcherProvider = TestDispatcherProvider()
        )
    }

    private lateinit var initialState: CategoriesTvShowsDetailsUiState
    private lateinit var initialGenre: TvShowGenre

    @BeforeEach
    fun setUp() {
        every { args.genreName } returns TvShowGenre.COMEDY.name

        initialState = viewModel.state.value
        initialGenre = viewModel.state.value.selectedGenre
    }

    @Test
    fun `init should set initial genre`() = runTest {
        assertThat(viewModel.state.value.selectedGenre).isEqualTo(TvShowGenre.COMEDY)
    }

    @Test
    fun `onClickBack should send NavigateBack effect`() = runTest {
        viewModel.effect.test {
            viewModel.onClickBack()
            assertThat(awaitItem()).isEqualTo(CategoriesTvShowsDetailsUiEffect.NavigateBack)
        }
    }

    @Test
    fun `onClickTvShowCard should send NavigateToTvShowDetails effect`() = runTest {
        viewModel.effect.test {
            viewModel.onClickTvShowCard(tvShowId)
            assertThat(awaitItem()).isEqualTo(
                CategoriesTvShowsDetailsUiEffect.NavigateToTvShowDetails(tvShowId)
            )
        }
    }

    @Test
    fun `onClickGenre with different genre should update selectedGenre`() = runTest {
        val newGenre = TvShowGenre.COMEDY
        viewModel.onClickGenre(newGenre)
        advanceUntilIdle()
        assertThat(viewModel.state.value.selectedGenre).isEqualTo(newGenre)
    }

    @Test
    fun `onClickGenre with same genre should not change state`() = runTest {
        advanceUntilIdle()
        val stableState = viewModel.state.value

        viewModel.onClickGenre(stableState.selectedGenre) // Use the now-stable genre
        advanceUntilIdle()

        assertThat(viewModel.state.value).isEqualTo(stableState)
    }

    @Test
    fun `onClickRetryRequest should finish loading`() = runTest {
        viewModel.onClickRetryRequest()
        advanceUntilIdle()
        assertThat(viewModel.state.value.isLoading).isFalse()
    }

    @Test
    fun `onPagingLoadStateChanged NotLoading should set isLoading false`() = runTest {
        val loadStates = mockk<CombinedLoadStates> {
            every { refresh } returns LoadState.NotLoading(endOfPaginationReached = false)
        }
        viewModel.onPagingLoadStateChanged(loadStates)
        advanceUntilIdle()
        assertThat(viewModel.state.value.isLoading).isFalse()
    }

    private val tvShowId = 123L
}