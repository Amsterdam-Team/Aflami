package com.amsterdam.viewmodel.cast

import app.cash.turbine.test
import com.amsterdam.domain.exceptions.NoInternetException
import com.amsterdam.domain.useCase.details.GetMovieCastUseCase
import com.amsterdam.domain.useCase.details.GetTvShowCastUseCase
import com.amsterdam.domain.useCase.preferences.ManageLocaleLanguageUseCase
import com.amsterdam.domain.useCase.preferences.ManageLocaleLanguageUseCase.Language
import com.amsterdam.entity.Actor
import com.amsterdam.entity.Gender
import com.amsterdam.viewmodel.cast.CastUiState.CastErrorUiState
import com.amsterdam.viewmodel.shared.uiStates.MediaType
import com.amsterdam.viewmodel.utils.TestDispatcherProvider
import com.amsterdam.viewmodel.utils.TestExtension
import com.google.common.truth.Truth.assertThat
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@OptIn(ExperimentalCoroutinesApi::class)
@ExtendWith(TestExtension::class)
class CastViewModelTest {

    private val getMovieCastUseCase: GetMovieCastUseCase = mockk()
    private val getTvShowCastUseCase: GetTvShowCastUseCase = mockk()
    private val manageLocaleLanguageUseCase: ManageLocaleLanguageUseCase = mockk()
    private val args: CastScreenArgs = mockk {
        every { mediaId } returns 100
        every { mediaType } returns MediaType.MOVIE.name
    }
    private val viewModel by lazy {
        CastViewModel(
            getMovieCastUseCase,
            getTvShowCastUseCase,
            args,
            manageLocaleLanguageUseCase,
            TestDispatcherProvider()
        )
    }

    @BeforeEach
    fun setUp() {
        every { manageLocaleLanguageUseCase.getAppLanguage() } returns flowOf(Language.ENGLISH)
    }

    @AfterEach
    fun tearDown() {
        clearAllMocks()
    }

    @Test
    fun `should nav back when onNavigateBackClicked`() = runTest {
        viewModel.onClickNavigateBack()

        viewModel.effect.test {
            assertThat(awaitItem()).isEqualTo(CastUiEffect.NavigateBack)
        }
    }

    @Test
    fun `fetchMovieCast should update state with cast list on success`() = runTest {
        coEvery { getMovieCastUseCase(any()) } returns fakeActors

        viewModel
        advanceUntilIdle()

        viewModel.state.test {
            assertThat(awaitItem().cast).isEqualTo(fakeActors.toActorsUiState())
        }
    }

    @Test
    fun `fetchTvShowCast should update state with cast list on success`() = runTest {
        coEvery { getTvShowCastUseCase(any()) } returns fakeActors

        val viewModel = CastViewModel(
            getMovieCastUseCase,
            getTvShowCastUseCase,
            args = mockk<CastScreenArgs> {
                every { mediaId } returns 456
                every { mediaType } returns MediaType.TV_SHOW.name
            },
            manageLocaleLanguageUseCase,
            TestDispatcherProvider()
        )
        viewModel
        advanceUntilIdle()

        viewModel.state.test {
            assertThat(awaitItem().cast).isEqualTo(fakeActors.toActorsUiState())
        }
    }

    @Test
    fun `should update state with NoNetworkError when use case throws NoInternetException`() =
        runTest {
            coEvery { getMovieCastUseCase(any()) } throws NoInternetException()

            viewModel
            advanceUntilIdle()

            viewModel.state.test {
                assertThat(awaitItem().errorUiState).isEqualTo(CastErrorUiState.NoNetworkConnection)
            }
        }

    @Test
    fun `initial state should set isLoading true then false after completion`() = runTest {
        viewModel

        viewModel.state.test {
            assertThat(awaitItem().isLoading).isFalse()
        }
    }

    @Test
    fun `onClickRetrySearch should re-fetch movie cast`() = runTest {
        coEvery { getMovieCastUseCase(any()) } returns fakeActors
        viewModel.onClickRetrySearch()
        advanceUntilIdle()

        viewModel.state.test {
            assertThat(awaitItem().cast).isEqualTo(fakeActors.toActorsUiState())
        }
    }

    companion object {
        private val fakeActors = listOf(
            Actor(1, "Actor 1", "https://image1.jpg", 8.5, Gender.Male),
            Actor(2, "Actor 2", "https://image2.jpg", 9.0, Gender.Female)
        )
    }
}