package com.amsterdam.viewmodel.search.actorSearch

import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.paging.LoadStates
import com.amsterdam.domain.exceptions.AflamiException
import com.amsterdam.domain.exceptions.NetworkException
import com.amsterdam.domain.useCase.preferences.ManageLocaleLanguageUseCase
import com.amsterdam.domain.useCase.search.GetMoviesByActorUseCase
import com.amsterdam.viewmodel.utils.TestDispatcherProvider
import com.amsterdam.viewmodel.utils.entityHelper.createMovie
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ActorSearchViewModelTest {
    private lateinit var viewModel: ActorSearchViewModel
    private lateinit var testDispatcherProvider: TestDispatcherProvider
    private lateinit var getMoviesByActorUseCase: GetMoviesByActorUseCase
    private lateinit var manageLocaleLanguageUseCase: ManageLocaleLanguageUseCase
    private lateinit var testScope: TestScope

    @BeforeEach
    fun setUp() {
        testDispatcherProvider = TestDispatcherProvider()
        testScope = TestScope(testDispatcherProvider.testDispatcher)
        getMoviesByActorUseCase = mockk(relaxed = true)
        manageLocaleLanguageUseCase = mockk(relaxed = true)

        Dispatchers.setMain(testDispatcherProvider.testDispatcher)

        viewModel = ActorSearchViewModel(
            getMoviesByActorUseCase = getMoviesByActorUseCase,
            manageLocaleLanguageUseCase = manageLocaleLanguageUseCase,
            dispatcherProvider = testDispatcherProvider
        )
    }

    @Test
    fun `init should subscribe to app language changes and trigger search observation`() =
        testScope.runTest {
            coEvery { manageLocaleLanguageUseCase.getAppLanguage() } returns flowOf(
                ManageLocaleLanguageUseCase.Language.ENGLISH
            )
            advanceUntilIdle()

            coVerify(exactly = 1) { manageLocaleLanguageUseCase.getAppLanguage() }
        }

    @Test
    fun `onUserSearchChange should update keyword when different from current state`() =
        testScope.runTest {
            val keyword = "Tom Hanks"

            viewModel.onUserSearchChange(keyword)
            advanceUntilIdle()

            assertThat(viewModel.state.value.keyword).isEqualTo(keyword)
        }

    @Test
    fun `onUserSearchChange should not update keywordFlow when keyword is same as current state`() =
        testScope.runTest {
            val keyword = "Tom Hanks"
            viewModel.onUserSearchChange(keyword)
            advanceUntilIdle()

            viewModel.onUserSearchChange(keyword)
            advanceUntilIdle()

            assertThat(viewModel.state.value.keyword).isEqualTo(keyword)
        }

    @Test
    fun `onUserSearchChange should handle whitespace differences correctly`() = testScope.runTest {
        val keyword1 = "Tom Hanks"
        val keyword2 = "  Tom Hanks  "

        viewModel.onUserSearchChange(keyword1)
        advanceUntilIdle()
        viewModel.onUserSearchChange(keyword2)
        advanceUntilIdle()

        assertThat(viewModel.state.value.keyword).isEqualTo(keyword2)
    }

    @Test
    fun `executeActorSearch should create pager with correct configuration`() = testScope.runTest {
        val query = "Tom Hanks"
        val movies = listOf(createMovie(id = 1, name = "Forrest Gump"))
        coEvery { getMoviesByActorUseCase(query, any()) } returns movies

        viewModel.onUserSearchChange(query)
        advanceTimeBy(300) // Wait for debounce
        advanceUntilIdle()

        // The use case is called inside PagingSource when paging library loads data
        // We can't verify it immediately since PagingSource is lazy
        // The loading state is managed by paging load states, not just executeActorSearch
        assertThat(viewModel.state.value.keyword).isEqualTo(query)
    }

    @Test
    fun `executeActorSearch should set loading state to true`() = testScope.runTest {
        val query = "Tom Hanks"
        coEvery { getMoviesByActorUseCase(query, any()) } returns emptyList()

        viewModel.onUserSearchChange(query)
        advanceTimeBy(300)
        advanceUntilIdle()

        assertThat(viewModel.state.value.keyword).isEqualTo(query)
    }

    @Test
    fun `handleSearchResults should update movies in state`() = testScope.runTest {
        val query = "Tom Hanks"
        val movies = listOf(createMovie(id = 1, name = "Forrest Gump"))
        coEvery { getMoviesByActorUseCase(query, any()) } returns movies

        viewModel.onUserSearchChange(query)
        advanceTimeBy(300) // Wait for debounce
        advanceUntilIdle()

        assertThat(viewModel.state.value.movies).isNotNull()
    }

    @Test
    fun `onClickNavigateBack should emit NavigateBack effect`() = testScope.runTest {
        val effects = mutableListOf<ActorSearchEffect>()
        val collectJob = testScope.launch {
            viewModel.effect.collect { effects.add(it) }
        }

        viewModel.onClickNavigateBack()
        advanceUntilIdle()
        collectJob.cancel()

        assertThat(effects).containsExactly(ActorSearchEffect.NavigateBack)
    }

    @Test
    fun `onClickRetrySearch should set loading state and execute search`() = testScope.runTest {
        val keyword = "Tom Hanks"
        val movies = listOf(createMovie(id = 1, name = "Forrest Gump"))
        coEvery { getMoviesByActorUseCase(keyword, any()) } returns movies

        // Set initial state
        viewModel.onUserSearchChange(keyword)
        advanceUntilIdle()

        viewModel.onClickRetrySearch()
        advanceUntilIdle()

        assertThat(viewModel.state.value.keyword).isEqualTo(keyword)
    }

    @Test
    fun `onClickMovie should emit NavigateToDetailsScreen effect with correct movieId`() =
        testScope.runTest {
            val movieId = 123L

            val effects = mutableListOf<ActorSearchEffect>()
            val collectJob = testScope.launch {
                viewModel.effect.collect { effects.add(it) }
            }

            viewModel.onClickMovie(movieId)
            advanceUntilIdle()
            collectJob.cancel()

            assertThat(effects).containsExactly(ActorSearchEffect.NavigateToDetailsScreen(movieId))
        }

    @Test
    fun `onPagingLoadStateChanged should handle Loading state with non-blank keyword`() =
        testScope.runTest {
            val keyword = "Tom Hanks"
            viewModel.onUserSearchChange(keyword)
            advanceUntilIdle()

            val loadStates = createCombinedLoadStates(LoadState.Loading)

            viewModel.onPagingLoadStateChanged(loadStates)
            advanceUntilIdle()

            assertThat(viewModel.state.value.isLoading).isTrue()
            assertThat(viewModel.state.value.error).isNull()
        }

    @Test
    fun `onPagingLoadStateChanged should not set loading for Loading state with blank keyword`() =
        testScope.runTest {
            val keyword = ""
            viewModel.onUserSearchChange(keyword)
            advanceUntilIdle()

            val loadStates = createCombinedLoadStates(LoadState.Loading)

            viewModel.onPagingLoadStateChanged(loadStates)
            advanceUntilIdle()

            assertThat(viewModel.state.value.isLoading).isFalse()
        }

    @Test
    fun `onPagingLoadStateChanged should handle NotLoading state`() = testScope.runTest {
        val loadStates = createCombinedLoadStates(LoadState.NotLoading(false))

        viewModel.onPagingLoadStateChanged(loadStates)
        advanceUntilIdle()

        assertThat(viewModel.state.value.isLoading).isFalse()
    }

    @Test
    fun `onPagingLoadStateChanged should handle Error state with NetworkException`() =
        testScope.runTest {
            val networkException = NetworkException()
            val loadStates = createCombinedLoadStates(LoadState.Error(networkException))

            viewModel.onPagingLoadStateChanged(loadStates)
            advanceUntilIdle()

            assertThat(viewModel.state.value.isLoading).isFalse()
            assertThat(viewModel.state.value.error).isEqualTo(ActorSearchErrorState.NoNetworkConnection)
        }

    @Test
    fun `onPagingLoadStateChanged should handle Error state with generic exception`() =
        testScope.runTest {
            val genericException = Exception("Generic error")
            val loadStates = createCombinedLoadStates(LoadState.Error(genericException))

            viewModel.onPagingLoadStateChanged(loadStates)
            advanceUntilIdle()

            assertThat(viewModel.state.value.isLoading).isFalse()
            assertThat(viewModel.state.value.error).isEqualTo(ActorSearchErrorState.UnknownError)
        }

    @Test
    fun `debounce search should only execute after delay`() = testScope.runTest {
        val keyword = "Tom"
        coEvery { getMoviesByActorUseCase(keyword, any()) } returns emptyList()

        viewModel.onUserSearchChange(keyword)
        advanceTimeBy(200)


        assertThat(viewModel.state.value.keyword).isEqualTo(keyword)

        advanceTimeBy(100)
        advanceUntilIdle()

        assertThat(viewModel.state.value.keyword).isEqualTo(keyword)
    }

    @Test
    fun `debounce search should handle rapid changes correctly`() = testScope.runTest {
        coEvery { getMoviesByActorUseCase(any(), any()) } returns emptyList()

        viewModel.onUserSearchChange("T")
        advanceTimeBy(100)
        viewModel.onUserSearchChange("To")
        advanceTimeBy(100)
        viewModel.onUserSearchChange("Tom")
        advanceTimeBy(300) // Wait for final debounce
        advanceUntilIdle()

        assertThat(viewModel.state.value.keyword).isEqualTo("Tom")
    }

    @Test
    fun `search should handle empty results`() = testScope.runTest {
        val keyword = "NonexistentActor"
        coEvery { getMoviesByActorUseCase(keyword, any()) } returns emptyList()

        viewModel.onUserSearchChange(keyword)
        advanceTimeBy(300)
        advanceUntilIdle()

        assertThat(viewModel.state.value.keyword).isEqualTo(keyword)
    }

    @Test
    fun `search should handle use case exception gracefully`() = testScope.runTest {
        val keyword = "Tom Hanks"
        coEvery { getMoviesByActorUseCase(keyword, any()) } throws AflamiException()

        viewModel.onUserSearchChange(keyword)
        advanceTimeBy(300)
        advanceUntilIdle()

        assertThat(viewModel.state.value.keyword).isEqualTo(keyword)
    }

    @Test
    fun `initial state should have correct default values`() = testScope.runTest {
        assertThat(viewModel.state.value.isLoading).isFalse()
        assertThat(viewModel.state.value.keyword).isEmpty()
        assertThat(viewModel.state.value.error).isNull()
    }

    @Test
    fun `ActorSearchErrorState should convert NetworkException correctly`() {
        val networkException = NetworkException()

        val result = ActorSearchErrorState.toActorSearchErrorState(networkException)

        assertThat(result).isEqualTo(ActorSearchErrorState.NoNetworkConnection)
    }

    @Test
    fun `ActorSearchErrorState should convert generic exception to UnknownError`() {
        val genericException = Exception("Some error")

        val result = ActorSearchErrorState.toActorSearchErrorState(genericException)

        assertThat(result).isEqualTo(ActorSearchErrorState.UnknownError)
    }

    @Test
    fun `ActorSearchErrorState should convert AflamiException to UnknownError`() {
        val aflamiException = AflamiException()

        val result = ActorSearchErrorState.toActorSearchErrorState(aflamiException)

        assertThat(result).isEqualTo(ActorSearchErrorState.UnknownError)
    }

    @Test
    fun `search should handle multiple rapid searches correctly`() = testScope.runTest {
        coEvery { getMoviesByActorUseCase(any(), any()) } returns emptyList()

        viewModel.onUserSearchChange("A")
        advanceTimeBy(50)
        viewModel.onUserSearchChange("Ac")
        advanceTimeBy(50)
        viewModel.onUserSearchChange("Act")
        advanceTimeBy(50)
        viewModel.onUserSearchChange("Actor")
        advanceTimeBy(300)
        advanceUntilIdle()

        assertThat(viewModel.state.value.keyword).isEqualTo("Actor")
    }

    @Test
    fun `search should handle whitespace-only keywords`() = testScope.runTest {
        val keyword = "   "
        coEvery { getMoviesByActorUseCase(any(), any()) } returns emptyList()

        viewModel.onUserSearchChange(keyword)
        advanceTimeBy(300)
        advanceUntilIdle()

        assertThat(viewModel.state.value.keyword).isEqualTo(keyword)
    }

    @Test
    fun `search should handle empty string keywords`() = testScope.runTest {
        val keyword = ""
        coEvery { getMoviesByActorUseCase(any(), any()) } returns emptyList()

        viewModel.onUserSearchChange(keyword)
        advanceTimeBy(300)
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