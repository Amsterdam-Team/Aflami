package com.amsterdam.viewmodel

import com.amsterdam.domain.exceptions.AflamiException
import com.amsterdam.domain.exceptions.NoInternetException
import com.amsterdam.domain.useCase.details.GetMovieCastUseCase
import com.amsterdam.domain.useCase.details.GetTvShowCastUseCase
import com.amsterdam.entity.Actor
import com.amsterdam.entity.Gender
import com.amsterdam.viewmodel.cast.CastScreenArgs
import com.amsterdam.viewmodel.cast.CastUiEffect
import com.amsterdam.viewmodel.cast.CastUiState
import com.amsterdam.viewmodel.cast.CastViewModel
import com.amsterdam.viewmodel.movieDetails.MovieDetailsArgs
import com.amsterdam.viewmodel.utils.TestDispatcherProvider
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class CastViewModelTest {
    private lateinit var viewModel: CastViewModel
    private lateinit var getMovieCastUseCase: GetMovieCastUseCase
    private lateinit var args: CastScreenArgs
    private lateinit var tvShowCastUseCase: GetTvShowCastUseCase
    private val testDispatcherProvider = TestDispatcherProvider()
    private var testScope = TestScope(
        testDispatcherProvider.testDispatcher
    )

    @BeforeEach
    fun setUp() {
        Dispatchers.setMain(testDispatcherProvider.testDispatcher)
        getMovieCastUseCase = mockk(relaxed = true)
        tvShowCastUseCase = mockk(relaxed = true)
        args = mockk {
            every { mediaId } returns 100
            every { mediaType } returns "movie"
        }
        viewModel = CastViewModel(
            getMovieCastUseCase = getMovieCastUseCase,
            args = args,
            dispatcherProvider = testDispatcherProvider,
            getTvShowCastUseCase = tvShowCastUseCase
        )

    }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `should nav back when onNavigateBackClicked`() =
        testScope.runTest {
            var effects = mutableListOf<CastUiEffect?>()
            val collectJob = testScope.launch {
                viewModel.effect.collect {
                    effects.add(it)
                }
            }

            viewModel.onClickNavigateBack()
            testScope.advanceUntilIdle()
            collectJob.cancel()
            assertThat(effects).containsExactly(CastUiEffect.NavigateBack)
        }

    @Test
    fun `fetchMovieCast should update state with cast list on success`() = testScope.runTest {
        // Given
        val fakeActors = listOf(
            Actor(
                id = 1,
                name = "Actor 1",
                imageUrl = "https://image1.jpg",
                popularity = 8.5,
                gender = Gender.Male
            ),
            Actor(
                id = 2,
                name = "Actor 2",
                imageUrl = "https://image2.jpg",
                popularity = 9.0,
                gender = Gender.Female
            )
        )
        coEvery { getMovieCastUseCase(100) } returns fakeActors
        viewModel = CastViewModel(
            getMovieCastUseCase = getMovieCastUseCase,
            args = args,
            dispatcherProvider = testDispatcherProvider,
            getTvShowCastUseCase = tvShowCastUseCase
        )

        val states = mutableListOf<CastUiState>()
        val job = launch {
            viewModel.state.collect { states.add(it) }
        }

        // When
        advanceUntilIdle()
        job.cancel()

        // Then
        val finalState = states.last()
        assertThat(finalState.cast).hasSize(2)
        assertThat(finalState.cast[0].actorName).isEqualTo("Actor 1")

    }

    @Test
    fun `getMovieCastUseCase throws unknown error, errorUiState should be null`() = runTest {
        // Given
        val exception = mockk<AflamiException>(relaxed = true)
        coEvery {
            getMovieCastUseCase(
                movieId = args.mediaId!!,
            )
        } throws exception

        // When
        viewModel = CastViewModel(getMovieCastUseCase, tvShowCastUseCase,args, testDispatcherProvider)

        // Then
        val actual = viewModel.state.value
        assertThat(actual.isLoading).isFalse()
    }

    @Test
    fun `initial state should set isLoading true then false after completion`() = runTest {
        // Given
        coEvery {
            getMovieCastUseCase(
                movieId = args.mediaId!!,
            )
        } returns emptyList()

        // When
        viewModel = CastViewModel(getMovieCastUseCase, tvShowCastUseCase,args, testDispatcherProvider)

        // Then
        val actual = viewModel.state.value
        assertThat(actual.isLoading).isFalse()
        assertThat(actual.cast).isEmpty()
    }
    @Test
    fun `onClickRetrySearch should re-fetch movie cast`() = runTest {
        // Given
        val fakeActors = listOf(
            Actor(id = 1, name = "Retry Actor", imageUrl = "img.jpg", popularity = 7.0, gender = Gender.Male)
        )
        coEvery { getMovieCastUseCase(100) } returns fakeActors

        // When
        viewModel.onClickRetrySearch()
        advanceUntilIdle()

        // Then
        val state = viewModel.state.value
        assertThat(state.cast).hasSize(1)
        assertThat(state.cast[0].actorName).isEqualTo("Retry Actor")
    }

   @Test
   fun`fetchMovieCast should update errorUiState when NoInternetException is thrown`() = runTest {
       // Given & When
       val exception = mockk<NoInternetException>(relaxed = true)
       coEvery { getMovieCastUseCase(100) } throws exception
       viewModel = CastViewModel(getMovieCastUseCase, tvShowCastUseCase,args, testDispatcherProvider)
       val states = mutableListOf<CastUiState>()
       val job = launch {
           viewModel.state.collect { states.add(it) }
       }

       // Then
       advanceUntilIdle()
       job.cancel()

       val lastState = states.last()
       assertThat(lastState.errorUiState).isInstanceOf(CastUiState.CastErrorUiState.NoNetworkConnection::class.java)
   }
}