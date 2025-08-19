package com.amsterdam.viewmodel.search.actorSearch

import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.paging.LoadStates
import app.cash.turbine.test
import com.amsterdam.domain.exceptions.AflamiException
import com.amsterdam.domain.exceptions.NetworkException
import com.amsterdam.domain.useCase.preferences.ManageLocaleLanguageUseCase
import com.amsterdam.domain.useCase.preferences.ManageLocaleLanguageUseCase.Language
import com.amsterdam.domain.useCase.search.GetMoviesByActorUseCase
import com.amsterdam.viewmodel.shared.errorUiState.ErrorUiState
import com.amsterdam.viewmodel.utils.TestDispatcherProvider
import com.amsterdam.viewmodel.utils.TestExtension
import com.amsterdam.viewmodel.utils.helper.createMovie
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@OptIn(ExperimentalCoroutinesApi::class)
@ExtendWith(TestExtension::class)
class ActorSearchViewModelTest {

    private val getMoviesByActorUseCase: GetMoviesByActorUseCase = mockk()
    private val manageLocaleLanguageUseCase: ManageLocaleLanguageUseCase = mockk()

    private val viewModel by lazy {
        ActorSearchViewModel(
            getMoviesByActorUseCase,
            TestDispatcherProvider()
        )
    }

    @BeforeEach
    fun setUp() {
        coEvery { manageLocaleLanguageUseCase.getAppLanguage() } returns flowOf(Language.ENGLISH)
    }

    @Test
    fun `onUserSearchChange should update keyword when different from current state`() = runTest {
        val keyword = "Tom Hanks"

        viewModel.onUserSearchChange(keyword)
        advanceUntilIdle()

        assertThat(viewModel.state.value.keyword).isEqualTo(keyword)
    }

    @Test
    fun `onUserSearchChange should not update keywordFlow when keyword is same as current state`() =
        runTest {
            val keyword = "Tom Hanks"
            viewModel.onUserSearchChange(keyword)
            advanceUntilIdle()

            viewModel.onUserSearchChange(keyword)
            advanceUntilIdle()

            assertThat(viewModel.state.value.keyword).isEqualTo(keyword)
        }

    @Test
    fun `onUserSearchChange should handle whitespace differences correctly`() = runTest {
        val keyword1 = "Tom Hanks"
        val keyword2 = "  Tom Hanks  "

        viewModel.onUserSearchChange(keyword1)
        advanceUntilIdle()
        viewModel.onUserSearchChange(keyword2)
        advanceUntilIdle()

        assertThat(viewModel.state.value.keyword).isEqualTo(keyword2)
    }

    @Test
    fun `executeActorSearch should set loading state to true`() = runTest {
        val query = "Tom Hanks"
        coEvery { getMoviesByActorUseCase(query) } returns emptyList()

        viewModel.onUserSearchChange(query)
        advanceTimeBy(300)
        advanceUntilIdle()

        assertThat(viewModel.state.value.keyword).isEqualTo(query)
    }

    @Test
    fun `handleSearchResults should update movies in state`() = runTest {
        val query = "Tom Hanks"
        val movies = listOf(createMovie(id = 1, name = "Forrest Gump"))
        coEvery { getMoviesByActorUseCase(query) } returns movies

        viewModel.onUserSearchChange(query)
        advanceTimeBy(300)
        advanceUntilIdle()

        assertThat(viewModel.state.value.movies).isNotNull()
    }

    @Test
    fun `onClickNavigateBack should emit NavigateBack effect`() = runTest {
        viewModel.onClickNavigateBack()

        viewModel.effect.test {
            assertThat(awaitItem()).isEqualTo(ActorSearchEffect.NavigateBack)
        }
    }

    @Test
    fun `onClickMovie should emit NavigateToDetailsScreen effect with movieId`() = runTest {
        viewModel.onClickMovie(movieId = 123)

        viewModel.effect.test {
            assertThat(awaitItem()).isEqualTo(ActorSearchEffect.NavigateToDetailsScreen(123))
        }
    }

    @Test
    fun `onPagingLoadStateChanged should update state with isLoading to true when LoadState is loading`() =
        runTest {
            val keyword = "Tom Hanks"
            viewModel.onUserSearchChange(keyword)
            advanceUntilIdle()

            viewModel.onPagingLoadStateChanged(createCombinedLoadStates(LoadState.Loading))
            advanceUntilIdle()

            viewModel.state.test {
                assertThat(awaitItem().isLoading).isTrue()
            }
        }

    @Test
    fun `onPagingLoadStateChanged should set loading to false for LoadState with blank keyword`() =
        runTest {
            viewModel.onUserSearchChange("")
            advanceUntilIdle()

            viewModel.onPagingLoadStateChanged(createCombinedLoadStates(LoadState.Loading))
            advanceUntilIdle()

            viewModel.state.test {
                assertThat(awaitItem().isLoading).isFalse()
            }
        }

    @Test
    fun `onPagingLoadStateChanged should set isLoading to false when LoadState is NotLoading`() =
        runTest {
            viewModel.onPagingLoadStateChanged(createCombinedLoadStates(LoadState.NotLoading(false)))
            advanceUntilIdle()

            viewModel.state.test {
                assertThat(awaitItem().isLoading).isFalse()
            }
        }

    @Test
    fun `onPagingLoadStateChanged should set error state when LoadState throws NetworkException`() =
        runTest {
            viewModel.onPagingLoadStateChanged(
                createCombinedLoadStates(
                    LoadState.Error(
                        NetworkException()
                    )
                )
            )
            advanceUntilIdle()

            viewModel.errorState.test { assertThat(awaitItem()).isEqualTo(ErrorUiState.NoInternetError) }
        }

    @Test
    fun `onPagingLoadStateChanged should handle Error state with generic exception`() = runTest {
        val loadStates = createCombinedLoadStates(LoadState.Error(Exception("Generic error")))

        viewModel.onPagingLoadStateChanged(loadStates)
        advanceUntilIdle()

        viewModel.errorState.test { assertThat(awaitItem()).isEqualTo(ErrorUiState.NoInternetError) }

    }

    @Test
    fun `debounce search should only execute after delay`() = runTest {
        val keyword = "Tom"
        val updateKeyword = "Tom hanks"
        coEvery { getMoviesByActorUseCase(keyword) } returns emptyList()

        viewModel.onUserSearchChange(keyword)
        advanceTimeBy(200)
        viewModel.onUserSearchChange(updateKeyword)
        viewModel.state.test {
            assertThat(awaitItem().keyword).isEqualTo(keyword)
        }

        advanceTimeBy(100)
        viewModel.state.test {
            assertThat(awaitItem().keyword).isEqualTo(updateKeyword)
        }
    }

    @Test
    fun `search should handle use case exception gracefully`() = runTest {
        val keyword = "Tom Hanks"
        coEvery { getMoviesByActorUseCase(keyword) } throws AflamiException()

        viewModel.onUserSearchChange(keyword)
        advanceUntilIdle()

        assertThat(viewModel.state.value.keyword).isEqualTo(keyword)
    }

    private fun createCombinedLoadStates(refreshState: LoadState): CombinedLoadStates {
        val sourceLoadStates = mockk<LoadStates>(relaxed = true)
        val mediatorLoadStates = mockk<LoadStates>(relaxed = true)

        every { sourceLoadStates.isIdle } returns false
        every { mediatorLoadStates.isIdle } returns false

        return CombinedLoadStates(
            refresh = refreshState,
            prepend = LoadState.NotLoading(false),
            append = LoadState.NotLoading(false),
            source = sourceLoadStates,
            mediator = mediatorLoadStates
        )
    }
}