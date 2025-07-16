package com.example.viewmodel

import com.example.domain.exceptions.NetworkException
import com.example.domain.useCase.GetMoviesByActorUseCase
import com.example.viewmodel.search.actorSearch.SearchByActorEffect
import com.example.viewmodel.search.actorSearch.SearchByActorViewModel
import com.example.viewmodel.search.mapper.toListOfUiState
import com.example.viewmodel.utils.TestDispatcherProvider
import com.example.viewmodel.utils.entityHelper.createMovie
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
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

class SearchByActorViewModelTest {
    private lateinit var viewModel: SearchByActorViewModel

    private val testDispatcherProvider = TestDispatcherProvider()
    private val getMoviesByActorUseCase: GetMoviesByActorUseCase = mockk(relaxed = true)
    private var testScope = TestScope(testDispatcherProvider.testDispatcher)

    @BeforeEach
    fun setUp() {
        Dispatchers.setMain(testDispatcherProvider.testDispatcher)
        viewModel = SearchByActorViewModel(
            getMoviesByActorUseCase = getMoviesByActorUseCase,
            dispatcherProvider = testDispatcherProvider
        )
    }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `should update keyword and set loading to false when onKeywordValueChanged is called with blank string`() =
        testScope.runTest {
            val keyword = ""

            viewModel.onKeywordValueChanged(keyword)
            testScope.advanceUntilIdle()

            val state = viewModel.state.value
            assertThat(state.keyword).isEqualTo(keyword)
            assertThat(state.isLoading).isFalse()
        }

    @Test
    fun `should not call use case when keyword is entered within debounce duration`() =
        testScope.runTest {
            val keyword = "a"
            viewModel.onKeywordValueChanged(keyword)
            advanceTimeBy(200L)

            coVerify(exactly = 0) { getMoviesByActorUseCase(any()) }
        }

    @Test
    fun `should call use case when keyword is entered after debounce duration`() =
        testScope.runTest {
            val keyword = "Tom"
            coEvery { getMoviesByActorUseCase(keyword) } returns emptyList()

            viewModel.onKeywordValueChanged(keyword)
            advanceTimeBy(500L)
            advanceUntilIdle()

            coVerify(exactly = 1) { getMoviesByActorUseCase(keyword) }
        }

    @Test
    fun `should update movies state when getMoviesByActorUseCase returns a list of movies`() =
        testScope.runTest {
            val keyword = "Tom Hanks"
            val movies = listOf(createMovie(id = 1, name = "Forrest Gump"))
            coEvery { getMoviesByActorUseCase(keyword) } returns movies

            viewModel.onKeywordValueChanged(keyword)
            advanceUntilIdle()

            val state = viewModel.state.value
            assertThat(state.movies).isEqualTo(movies.toListOfUiState())
            assertThat(state.isLoading).isFalse()
        }

    @Test
    fun `should update state to empty movies when getMoviesByActorUseCase returns an empty list`() =
        testScope.runTest {
            val keyword = "Unknown Actor"
            coEvery { getMoviesByActorUseCase(keyword) } returns emptyList()

            viewModel.onKeywordValueChanged(keyword)
            advanceUntilIdle()

            val state = viewModel.state.value
            assertThat(state.movies).isEmpty()
            assertThat(state.isLoading).isFalse()
        }

    @Test
    fun `should update noInternetException state when use case throws NetworkException`() =
        testScope.runTest {
            val keyword = "Tom Cruise"
            coEvery { getMoviesByActorUseCase(keyword) } throws NetworkException()

            viewModel.onKeywordValueChanged(keyword)
            advanceUntilIdle()

            val state = viewModel.state.value
            assertThat(state.noInternetException).isTrue()
            assertThat(state.isLoading).isFalse()
            assertThat(state.movies).isEmpty()
        }

    @Test
    fun `should send NavigateBack effect when onNavigateBackClicked is called`() =
        testScope.runTest {
            val effects = mutableListOf<SearchByActorEffect>()
            val collectJob = launch {
                viewModel.effect.collect { effects.add(it!!) }
            }

            viewModel.onNavigateBackClicked()
            advanceUntilIdle()

            assertThat(effects).contains(SearchByActorEffect.NavigateBack)
            collectJob.cancel()
        }

    @Test
    fun `should retry search when onRetryQuestClicked is called`() = testScope.runTest {
        val keyword = "Will Smith"
        coEvery { getMoviesByActorUseCase(keyword) } throws NetworkException()
        viewModel.onKeywordValueChanged(keyword)
        advanceUntilIdle()

        coVerify(exactly = 1) { getMoviesByActorUseCase(keyword) }
        assertThat(viewModel.state.value.noInternetException).isTrue()

        val movies = listOf(createMovie(id = 2, name = "I Am Legend"))
        coEvery { getMoviesByActorUseCase(keyword) } returns movies

        viewModel.onRetryQuestClicked()
        advanceTimeBy(500L)
        advanceUntilIdle()

        coVerify(exactly = 2) { getMoviesByActorUseCase(keyword) }
        val state = viewModel.state.value
        assertThat(state.movies).isEqualTo(movies.toListOfUiState())
        assertThat(state.isLoading).isFalse()
    }
}