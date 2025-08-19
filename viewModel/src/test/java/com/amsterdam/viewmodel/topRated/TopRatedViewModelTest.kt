package com.amsterdam.viewmodel.topRated

import androidx.paging.LoadState
import androidx.paging.testing.asSnapshot
import app.cash.turbine.test
import com.amsterdam.domain.exceptions.AflamiException
import com.amsterdam.domain.exceptions.NetworkException
import com.amsterdam.domain.useCase.preferences.ManageLocaleLanguageUseCase
import com.amsterdam.domain.useCase.topRated.GetTopRatedDataUseCase
import com.amsterdam.viewmodel.shared.errorUiState.ErrorUiState
import com.amsterdam.viewmodel.shared.uiStates.MediaType
import com.amsterdam.viewmodel.utils.TestDispatcherProvider
import com.amsterdam.viewmodel.utils.TestExtension
import com.amsterdam.viewmodel.utils.helper.createMovie
import com.amsterdam.viewmodel.utils.helper.createPagingLoadStates
import com.amsterdam.viewmodel.utils.helper.createTvShow
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExperimentalCoroutinesApi
@ExtendWith(TestExtension::class)
class TopRatedViewModelTest {

    private val getTopRatedDataUseCase: GetTopRatedDataUseCase = mockk(relaxed = true)
    private val manageLocaleLanguageUseCase: ManageLocaleLanguageUseCase = mockk(relaxed = true)

    private val viewModel by lazy {
        TopRatedViewModel(
            getTopRatedScreenDataUseCase = getTopRatedDataUseCase,
            manageLocaleLanguageUseCase = manageLocaleLanguageUseCase,
            dispatcherProvider = TestDispatcherProvider()
        )
    }

    @BeforeEach
    fun setUp() {
        every { manageLocaleLanguageUseCase.getAppLanguage() } returns languageFlow
        viewModel
    }

    @Test
    fun `init should set loading to true, when its called`() = runTest {
        assertThat(viewModel.state.value.isLoading).isTrue()
    }

    @Test
    fun `init should update state of media items when data loaded successfully`() = runTest {
        coEvery { getTopRatedDataUseCase(1) } returns topRatedData

        advanceUntilIdle()

        viewModel.state.test {
            val items = awaitItem().mediaItems.asSnapshot()
            assertThat(items).containsExactlyElementsIn(expectedTopRatedUiState).inOrder()
        }
        coVerify(exactly = 1) { getTopRatedDataUseCase(1) }
    }

    @Test
    fun `init should update state loading state false, when NetworkException is thrown`() =
        runTest {
            coEvery { getTopRatedDataUseCase(any()) } throws NetworkException()

            advanceUntilIdle()

            assertThat(viewModel.state.value.isLoading).isFalse()
        }

    @Test
    fun `onClickRetryLoading triggers data fetch, when called`() = runTest {
        coEvery { getTopRatedDataUseCase(1) } returns topRatedData

        viewModel.onClickRetryLoading()
        advanceUntilIdle()

        viewModel.state.test {
            val items = awaitItem().mediaItems.asSnapshot()
            assertThat(items).containsExactlyElementsIn(expectedTopRatedUiState).inOrder()
        }
        coVerify(exactly = 1) { getTopRatedDataUseCase(1) }
    }


    @Test
    fun `onPagingLoadStateChanged should set loading to true, when called with loading state`() =
        runTest {
            viewModel.onPagingLoadStateChanged(expectedPagingLoadingState)
            advanceUntilIdle()

            assertThat(viewModel.state.value.isLoading).isTrue()
        }

    @Test
    fun `onPagingLoadStateChanged should set error to null, when called with loading state`() =
        runTest {
            viewModel.onPagingLoadStateChanged(expectedPagingLoadingState)
            advanceUntilIdle()

            viewModel.errorState.test {
                assertThat(awaitItem()).isNull()
            }
        }

    @Test
    fun `onPagingLoadStateChanged should set loading false, when called with not loading state`() =
        runTest {
            viewModel.onPagingLoadStateChanged(expectedPagingNotLoadingState)
            advanceUntilIdle()

            assertThat(viewModel.state.value.isLoading).isFalse()
        }

    @Test
    fun `onPagingLoadStateChanged should set loading false, when called with any exception`() =
        runTest {
            viewModel.onPagingLoadStateChanged(expectedPagingGeneralErrorState)
            advanceUntilIdle()

            assertThat(viewModel.state.value.isLoading).isFalse()
        }


    @Test
    fun `onPagingLoadStateChanged should update error state, when called with NetworkException`() =
        runTest {
            viewModel.onPagingLoadStateChanged(expectedPagingNetworkErrorState)
            advanceUntilIdle()

            viewModel.errorState.test {
                assertThat(awaitItem()).isEqualTo(ErrorUiState.NoInternetError)
            }
        }

    @Test
    fun `onClickMediaItem with movie type should navigate to movie details, when called`() =
        runTest {
            viewModel.onClickMediaItem(1, MediaType.MOVIE)

            viewModel.effect.test {
                assertThat(awaitItem()).isEqualTo(TopRatedEffect.NavigateToMovieDetailsScreen(1))
            }
        }

    @Test
    fun `onClickMediaItem should navigate to tv details, when called`() = runTest {
        viewModel.onClickMediaItem(1, MediaType.TV_SHOW)

        viewModel.effect.test {
            assertThat(awaitItem()).isEqualTo(TopRatedEffect.NavigateToTvShowDetailsEffect(1))
        }
    }

    @Test
    fun `onClickBack should trigger navigate back effect, when called`() = runTest {
        viewModel.onClickBack()

        viewModel.effect.test {
            assertThat(awaitItem()).isEqualTo(TopRatedEffect.NavigateBack)
        }
    }

    private val topRatedData = GetTopRatedDataUseCase.TopRatedData(
        topRatedMovies = listOf(createMovie(1, "interstellar")),
        topRatedTvShows = listOf(createTvShow(2, "family"), createTvShow(3, "family v2"))
    )

    private val expectedTopRatedUiState = getTopRatedMediaItems(
        topRatedMovies = topRatedData.topRatedMovies,
        topRatedTvShows = topRatedData.topRatedTvShows
    )

    private val expectedPagingLoadingState = createPagingLoadStates(
        state = LoadState.Loading
    )

    private val expectedPagingNotLoadingState = createPagingLoadStates(
        state = LoadState.NotLoading(endOfPaginationReached = false)
    )

    private val expectedPagingNetworkErrorState = createPagingLoadStates(
        state = LoadState.Error(error = NetworkException())
    )

    private val expectedPagingGeneralErrorState = createPagingLoadStates(
        state = LoadState.Error(error = AflamiException())
    )

    val languageFlow = flowOf(ManageLocaleLanguageUseCase.Language.ENGLISH)
}