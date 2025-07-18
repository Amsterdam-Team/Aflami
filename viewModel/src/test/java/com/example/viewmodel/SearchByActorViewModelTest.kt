package com.example.viewmodel

import com.example.domain.exceptions.AflamiException
import com.example.domain.exceptions.NetworkException
import com.example.domain.useCase.GetMoviesByActorUseCase
import com.example.viewmodel.search.actorSearch.SearchActorEffect
import com.example.viewmodel.search.actorSearch.ActorSearchUiState
import com.example.viewmodel.search.actorSearch.SearchActorViewModel
import com.example.viewmodel.search.mapper.toListOfUiState
import com.example.viewmodel.utils.TestDispatcherProvider
import com.example.viewmodel.utils.entityHelper.createMovie
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SearchByActorViewModelTest {
    private lateinit var viewModel: SearchActorViewModel

    private val testDispatcherProvider = TestDispatcherProvider()
    private val getMoviesByActorUseCase: GetMoviesByActorUseCase = mockk(relaxed = true)
    private var testScope = TestScope(testDispatcherProvider.testDispatcher)

    @BeforeEach
    fun setUp() {
        Dispatchers.setMain(testDispatcherProvider.testDispatcher)
        viewModel = SearchActorViewModel(
            getMoviesByActorUseCase = getMoviesByActorUseCase,
            dispatcherProvider = testDispatcherProvider
        )
    }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `onUserSearchChange should update keyword and set loading to false when onKeywordValueChanged is called with blank string`() =
        testScope.runTest {
            val keyword = ""

            viewModel.onUserSearchChange(keyword)
            testScope.advanceUntilIdle()

            val state = viewModel.state.value
            assertThat(state.keyword).isEqualTo(keyword)
            assertThat(state.isLoading).isFalse()
        }


    @Test
    fun `onUserSearchChange should call use case when keyword is entered after debounce duration`() =
        testScope.runTest {
            val keyword = "Tom"
            coEvery { getMoviesByActorUseCase(keyword) } returns emptyList()

            viewModel.onUserSearchChange(keyword)
            advanceTimeBy(500L)
            advanceUntilIdle()

            coVerify(exactly = 1) { getMoviesByActorUseCase(keyword) }
        }

    @Test
    fun `onUserSearchChange should not call use case when keyword is entered within debounce duration`() =
        testScope.runTest {
            var keyword = "a"
            viewModel.onUserSearchChange(keyword)
            advanceTimeBy(200L)
            keyword += "b"
            viewModel.onUserSearchChange(keyword)
            advanceTimeBy(200L)
            coVerify(exactly = 0) { getMoviesByActorUseCase(any()) }
        }


    @Test
    fun `onUserSearchChange should update movies state when getMoviesByActorUseCase returns a list of movies`() =
        testScope.runTest {
            val keyword = "Tom Hanks"
            val movies = listOf(createMovie(id = 1, name = "Forrest Gump"))
            coEvery { getMoviesByActorUseCase(keyword) } returns movies

            viewModel.onUserSearchChange(keyword)
            advanceUntilIdle()

            val state = viewModel.state.value
            assertThat(state.movies).isEqualTo(movies.toListOfUiState())
            assertThat(state.isLoading).isFalse()
        }

    @Test
    fun `onUserSearchChange should update state to empty movies when getMoviesByActorUseCase returns an empty list`() =
        testScope.runTest {
            val keyword = "Unknown Actor"
            coEvery { getMoviesByActorUseCase(keyword) } returns emptyList()

            viewModel.onUserSearchChange(keyword)
            advanceUntilIdle()

            val state = viewModel.state.value
            assertThat(state.movies).isEmpty()
            assertThat(state.isLoading).isFalse()
        }

    @Test
    fun `onUserSearchChange should update noInternetException state when use case throws NetworkException`() =
        testScope.runTest {
            val keyword = "Tom Cruise"
            coEvery { getMoviesByActorUseCase(keyword) } throws NetworkException()

            viewModel.onUserSearchChange(keyword)
            advanceUntilIdle()

            val state = viewModel.state.value
            assertThat(state.error).isEqualTo(ActorSearchUiState.SearchByActorError.NetworkError)
            assertThat(state.isLoading).isFalse()
            assertThat(state.movies).isEmpty()
        }

    @Test
    fun `onNavigateBackClicked should send NavigateBack effect when its called`() =
        testScope.runTest {
            val effects = mutableListOf<SearchActorEffect>()
            val collectJob = launch {
                viewModel.effect.collect { effects.add(it!!) }
            }

            viewModel.onClickNavigateBack()
            advanceUntilIdle()
            collectJob.cancel()
            assertThat(effects).contains(SearchActorEffect.NavigateBack)
        }

    @Test
    fun `onRetrySearchClick should retry search when its called`() = testScope.runTest {
        val keyword = "Will Smith"
        coEvery { getMoviesByActorUseCase(keyword) } throws NetworkException()
        viewModel.onUserSearchChange(keyword)
        advanceUntilIdle()

        coVerify(exactly = 1) { getMoviesByActorUseCase(keyword) }
        assertThat(viewModel.state.value.error).isEqualTo(ActorSearchUiState.SearchByActorError.NetworkError)

        val movies = listOf(createMovie(id = 2, name = "I Am Legend"))
        coEvery { getMoviesByActorUseCase(keyword) } returns movies

        viewModel.onClickRetrySearch()
        advanceTimeBy(500L)
        advanceUntilIdle()

        coVerify(exactly = 2) { getMoviesByActorUseCase(keyword) }
        val state = viewModel.state.value
        assertThat(state.movies).isEqualTo(movies.toListOfUiState())
        assertThat(state.isLoading).isFalse()
    }

    @Test
    fun `onMovieClicked should update the selected movie id state when its call`() =
        testScope.runTest {
            val movieId = 1L

            viewModel.onClickMovie(movieId)
            advanceUntilIdle()

            assertThat(viewModel.state.value.selectedMovieId).isEqualTo(movieId)
        }

    @Test
    fun `onMovieClicked should send NavigateToDetailsScreen effect when its call`() =
        testScope.runTest {
            val movieId = 1L
            var effect: SearchActorEffect? = null

            val job = launch {
                viewModel.effect.collect { searchByActorEffect ->
                    effect = searchByActorEffect
                }
            }
            viewModel.onClickMovie(movieId)
            advanceUntilIdle()
            job.cancel()

            assertThat(effect).isEqualTo(SearchActorEffect.NavigateToDetailsScreen)
        }

    @Test
    fun `onUserSearchChange should update the state with empty list and stop loading when get unknown error `()
    = testScope.runTest {
            val keyword = "#%$"
            coEvery { getMoviesByActorUseCase(keyword) } throws AflamiException()

            viewModel.onUserSearchChange(keyword)
            advanceUntilIdle()

            val state = viewModel.state.value
            assertThat(state.isLoading).isFalse()
            assertThat(state.movies).isEmpty()
    }
}