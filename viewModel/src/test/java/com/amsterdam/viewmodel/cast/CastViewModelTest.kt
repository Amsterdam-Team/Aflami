package com.amsterdam.viewmodel.cast

import com.amsterdam.domain.exceptions.AflamiException
import com.amsterdam.domain.exceptions.NoInternetException
import com.amsterdam.domain.useCase.details.GetMovieCastUseCase
import com.amsterdam.domain.useCase.details.GetTvShowCastUseCase
import com.amsterdam.domain.useCase.preferences.ManageLocaleLanguageUseCase
import com.amsterdam.domain.useCase.preferences.ManageLocaleLanguageUseCase.Language
import com.amsterdam.entity.Actor
import com.amsterdam.entity.Gender
import com.amsterdam.viewmodel.utils.TestDispatcherProvider
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@OptIn(ExperimentalCoroutinesApi::class)
class CastViewModelTest {
    private lateinit var viewModel: CastViewModel
    private lateinit var getMovieCastUseCase: GetMovieCastUseCase
    private lateinit var getTvShowCastUseCase: GetTvShowCastUseCase
    private lateinit var manageLocaleLanguageUseCase: ManageLocaleLanguageUseCase
    private lateinit var args: CastScreenArgs

    private val testDispatcherProvider = TestDispatcherProvider()

    @BeforeEach
    fun setUp() {
        Dispatchers.setMain(testDispatcherProvider.testDispatcher)
        getMovieCastUseCase = mockk(relaxed = true)
        getTvShowCastUseCase = mockk(relaxed = true)
        manageLocaleLanguageUseCase = mockk()
        every { manageLocaleLanguageUseCase.getAppLanguage() } returns flowOf(Language.ENGLISH)
        args = mockk {
            every { mediaId } returns 100
            every { mediaType } returns "MOVIE"
        }
    }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `should nav back when onNavigateBackClicked`() = runTest {
        viewModel = CastViewModel(
            getMovieCastUseCase = getMovieCastUseCase,
            getTvShowCastUseCase = getTvShowCastUseCase,
            args = args,
            manageLocaleLanguageUseCase = manageLocaleLanguageUseCase,
            dispatcherProvider = testDispatcherProvider
        )

        val effects = mutableListOf<CastUiEffect?>()
        val collectJob = launch {
            viewModel.effect.collect { effects.add(it) }
        }

        viewModel.onClickNavigateBack()
        testDispatcherProvider.testDispatcher.scheduler.advanceUntilIdle()

        assertThat(effects).containsExactly(CastUiEffect.NavigateBack)
        collectJob.cancel()
    }

    @Test
    fun `fetchMovieCast should update state with cast list on success`() = runTest {
        // Given
        val fakeActors = listOf(
            Actor(1, "Actor 1", "https://image1.jpg", 8.5, Gender.Male),
            Actor(2, "Actor 2", "https://image2.jpg", 9.0, Gender.Female)
        )
        coEvery { getMovieCastUseCase(100) } returns fakeActors

        // When
        viewModel = CastViewModel(
            getMovieCastUseCase,
            getTvShowCastUseCase,
            args,
            manageLocaleLanguageUseCase,
            testDispatcherProvider
        )

        testDispatcherProvider.testDispatcher.scheduler.advanceUntilIdle()

        // Then
        val state = viewModel.state.value
        assertThat(state.cast).hasSize(2)
        assertThat(state.cast[0].actorName).isEqualTo("Actor 1")
    }

    @Test
    fun `fetchTvShowCast should update state with cast list on success`() = runTest {
        // Given
        val tvShowArgs = mockk<CastScreenArgs> {
            every { mediaId } returns 456
            every { mediaType } returns "TV_SHOW" // Corrected to uppercase
        }
        val fakeActors = listOf(
            Actor(3, "Actor 3", "https://image3.jpg", 7.5, Gender.Male),
            Actor(4, "Actor 4", "https://image4.jpg", 8.0, Gender.Female)
        )
        coEvery { getTvShowCastUseCase(456) } returns fakeActors

        // When
        viewModel = CastViewModel(
            getMovieCastUseCase,
            getTvShowCastUseCase,
            tvShowArgs,
            manageLocaleLanguageUseCase,
            testDispatcherProvider
        )

        testDispatcherProvider.testDispatcher.scheduler.advanceUntilIdle()

        // Then
        val state = viewModel.state.value
        assertThat(state.cast).hasSize(2)
        assertThat(state.cast[0].actorName).isEqualTo("Actor 3")
    }

    @Test
    fun `getMovieCastUseCase throws unknown error, errorUiState should be null`() = runTest {
        // Given
        val exception = mockk<AflamiException>(relaxed = true)
        coEvery { getMovieCastUseCase(100) } throws exception

        // When
        viewModel = CastViewModel(
            getMovieCastUseCase,
            getTvShowCastUseCase,
            args,
            manageLocaleLanguageUseCase,
            testDispatcherProvider
        )
        testDispatcherProvider.testDispatcher.scheduler.advanceUntilIdle()

        // Then
        val actual = viewModel.state.value
        assertThat(actual.isLoading).isFalse()
        assertThat(actual.errorUiState).isNull()
    }

    @Test
    fun `initial state should set isLoading true then false after completion`() = runTest {
        // Given
        coEvery { getMovieCastUseCase(100) } returns emptyList()

        // When
        viewModel = CastViewModel(
            getMovieCastUseCase,
            getTvShowCastUseCase,
            args,
            manageLocaleLanguageUseCase,
            testDispatcherProvider
        )
        testDispatcherProvider.testDispatcher.scheduler.advanceUntilIdle()

        // Then
        val actual = viewModel.state.value
        assertThat(actual.isLoading).isFalse()
        assertThat(actual.cast).isEmpty()
    }

    @Test
    fun `onClickRetrySearch should re-fetch movie cast`() = runTest {
        // Given
        val initialActors = emptyList<Actor>()
        val retryActors = listOf(
            Actor(1, "Retry Actor", "img.jpg", 7.0, Gender.Male)
        )

        coEvery { getMovieCastUseCase(100) } returns initialActors

        viewModel = CastViewModel(
            getMovieCastUseCase,
            getTvShowCastUseCase,
            args,
            manageLocaleLanguageUseCase,
            testDispatcherProvider
        )
        testDispatcherProvider.testDispatcher.scheduler.advanceUntilIdle()

        assertThat(viewModel.state.value.cast).isEmpty()

        coEvery { getMovieCastUseCase(100) } returns retryActors

        // When
        viewModel.onClickRetrySearch()
        testDispatcherProvider.testDispatcher.scheduler.advanceUntilIdle()

        // Then
        val state = viewModel.state.value
        assertThat(state.cast).hasSize(1)
        assertThat(state.cast[0].actorName).isEqualTo("Retry Actor")
        assertThat(state.isLoading).isFalse()
    }

    @Test
    fun `fetchMovieCast should update errorUiState when NoInternetException is thrown`() = runTest {
        // Given
        val exception = NoInternetException()
        coEvery { getMovieCastUseCase(100) } throws exception

        // When
        viewModel = CastViewModel(
            getMovieCastUseCase,
            getTvShowCastUseCase,
            args,
            manageLocaleLanguageUseCase,
            testDispatcherProvider
        )
        testDispatcherProvider.testDispatcher.scheduler.advanceUntilIdle()

        // Then
        val state = viewModel.state.value
        assertThat(state.isLoading).isFalse()
        assertThat(state.cast).isEmpty()
        assertThat(state.errorUiState).isEqualTo(CastUiState.CastErrorUiState.NoNetworkConnection)
    }
}