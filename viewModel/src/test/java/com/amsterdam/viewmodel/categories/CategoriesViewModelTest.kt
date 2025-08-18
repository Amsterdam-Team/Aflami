package com.amsterdam.viewmodel.categories

import app.cash.turbine.test
import com.amsterdam.viewmodel.shared.TabOption
import com.amsterdam.viewmodel.utils.TestDispatcherProvider
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MainDispatcherExtension::class)
class CategoriesViewModelTest {

    private val viewModel by lazy {
        CategoriesViewModel(
            dispatcherProvider = TestDispatcherProvider()
        )
    }

    @Test
    fun `onClickMovieGenreCard should send NavigateToCategoriesMoviesDetailsScreenEffect`() =
        runTest {
            viewModel.effect.test {
                viewModel.onClickMovieGenreCard(genreName)
                assertThat(awaitItem()).isEqualTo(
                    CategoriesUiEffect.NavigateToCategoriesMoviesDetailsScreen(genreName)
                )
            }
        }

    @Test
    fun `onClickTvShowGenreCard should send NavigateToCategoriesTvShowsDetailsScreenEffect`() =
        runTest {
            viewModel.effect.test {
                viewModel.onClickTvShowGenreCard(genreName)
                assertThat(awaitItem()).isEqualTo(
                    CategoriesUiEffect.NavigateToCategoriesTvShowsDetailsScreen(genreName)
                )
            }
        }

    @Test
    fun `onChangeTabOption should update selectedTabOption in state`() = runTest {
        viewModel.onChangeTabOption(tabOption)
        advanceUntilIdle()
        assertThat(viewModel.state.value.selectedTabOption).isEqualTo(tabOption)
    }

    private val genreName = "action"
    private val tabOption = TabOption.MOVIES
}