package com.amsterdam.viewmodel.search.countrySearch

import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.paging.LoadStates
import app.cash.turbine.test
import com.amsterdam.domain.exceptions.NetworkException
import com.amsterdam.domain.exceptions.NoInternetException
import com.amsterdam.domain.useCase.search.GetMoviesByCountryUseCase
import com.amsterdam.domain.useCase.search.GetSuggestedCountriesUseCase
import com.amsterdam.entity.Country
import com.amsterdam.viewmodel.shared.errorUiState.ErrorUiState
import com.amsterdam.viewmodel.utils.TestDispatcherProvider
import com.amsterdam.viewmodel.utils.TestExtension
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource

@OptIn(ExperimentalCoroutinesApi::class)
@ExtendWith(TestExtension::class)
class SearchByCountryViewModelTest {

    private val getSuggestedCountriesUseCase: GetSuggestedCountriesUseCase = mockk(relaxed = true)
    private val getMoviesByCountryUseCase: GetMoviesByCountryUseCase = mockk(relaxed = true)

    private val viewModel by lazy {
        CountrySearchViewModel(
            getSuggestedCountriesUseCase,
            getMoviesByCountryUseCase,
            TestDispatcherProvider()
        )
    }

    @Test
    fun `should nav back when onNavigateBackClicked`() = runTest {
        viewModel.onClickNavigateBack()

        viewModel.effect.test {
            assertThat(awaitItem()).isEqualTo(CountrySearchEffect.NavigateBack)
        }
    }

    @Test
    fun `should nav to movie details when onClickMovieCard`() = runTest {
        viewModel.onClickMovieCard(1L)

        viewModel.effect.test {
            assertThat(awaitItem()).isEqualTo(CountrySearchEffect.NavigateToMovieDetails)
        }
    }

    @Test
    fun `should update the selected country name when onKeywordValueChanged`() = runTest {
        val keyword = "egypt"

        viewModel.onChangeSearchKeyword(keyword)
        advanceUntilIdle()

        viewModel.state.test {
            assertThat(awaitItem().keyword).isEqualTo(keyword)
        }
    }

    @Test
    fun `should hide countries dropDown when call it with empty string`() = runTest {
        val keyword = ""

        viewModel.onChangeSearchKeyword(keyword)

        viewModel.state.test {
            assertThat(awaitItem().showSuggestedCountries).isFalse()
        }
    }

    @Test
    fun `should take no action when debounce is running`() = runTest {
        val keyword = "a"
        coEvery { getSuggestedCountriesUseCase(any()) } returns emptyList()

        viewModel.onChangeSearchKeyword(keyword)

        coVerify(exactly = 0) { getSuggestedCountriesUseCase(keyword) }
    }

    @Test
    fun `should not show countries dropDown when getSuggestedCountriesUseCase return empty list`() =
        runTest {
            coEvery { getSuggestedCountriesUseCase("abc") } returns emptyList()

            viewModel.onChangeSearchKeyword("abc")

            viewModel.state.test {
                assertThat(awaitItem().showSuggestedCountries).isFalse()
            }
        }

    @Test
    fun `should reload countries when onClickRetry called without selecting country`() = runTest {
        val fakeCountries = listOf(countryUiState.toCountry())
        coEvery { getSuggestedCountriesUseCase(any()) } returns fakeCountries

        viewModel.onClickRetry()
        advanceUntilIdle()

        viewModel.state.test {
            assertThat(awaitItem().suggestedCountries).isEqualTo(fakeCountries.toUiState())
        }
    }

    @Test
    fun `should call load countries use case only once debounce end`() = runTest {
        coEvery { getSuggestedCountriesUseCase(any()) } returns emptyList()

        viewModel.onChangeSearchKeyword("N")
        viewModel.onChangeSearchKeyword(countryUiState.countryName)
        advanceUntilIdle()

        coVerify(exactly = 1) { getSuggestedCountriesUseCase(any()) }
    }

    @Test
    @Disabled
    fun `should update countries list with new value when debounce ends`() = runTest {
        val fakeCountries = listOf(countryUiState.toCountry())
        coEvery { getSuggestedCountriesUseCase(any()) } returns fakeCountries

        viewModel.onChangeSearchKeyword("N")
        viewModel.onChangeSearchKeyword(countryUiState.countryName)
        advanceUntilIdle()

        viewModel.state.test {
            assertThat(awaitItem().suggestedCountries).isEqualTo(fakeCountries.toUiState())
        }
    }

    @Test
    fun `should set loading to true when pagination load changed to loading when selectedCountryIsoCode has value`() =
        runTest {
            viewModel.onSelectCountry(countryUiState)
            advanceUntilIdle()

            viewModel.onPagingLoadStateChanged(createCombinedLoadState(LoadState.Loading))

            viewModel.state.test {
                assertThat(awaitItem().isLoading).isTrue()
            }
        }

    @Test
    fun `should set loading to false when pagination load changed to not loading`() = runTest {
        viewModel.onPagingLoadStateChanged(createCombinedLoadState(LoadState.NotLoading(true)))

        viewModel.state.test {
            assertThat(awaitItem().isLoading).isFalse()
        }
    }

    @Test
    fun `should set error ui state when pagination load changed to error`() = runTest {
        viewModel.onPagingLoadStateChanged(createCombinedLoadState(LoadState.Error(NetworkException())))
        advanceUntilIdle()

        viewModel.errorState.test { assertThat(awaitItem()).isEqualTo(ErrorUiState.NoInternetError) }
    }

    @Test
    @Disabled
    fun `when use case fails, error state is updated`() = runTest {
        coEvery { getSuggestedCountriesUseCase("eg") } throws NetworkException()

        viewModel.onChangeSearchKeyword("Egypt")
        advanceUntilIdle()

        assertThat(viewModel.errorState).isEqualTo(ErrorUiState.NoInternetError)
    }

    @Test
    fun `when user types blank text, use case is not called`() = runTest {
        viewModel.onChangeSearchKeyword("   ")
        advanceUntilIdle()

        coVerify(exactly = 0) { getSuggestedCountriesUseCase(any()) }
    }

    @ParameterizedTest
    @MethodSource("exceptionToStateProvider")
    fun `should set different error state when throw exception from getSuggestedCountriesUseCase after selecting country`(
        exception: Exception,
        expectedState: ErrorUiState,
    ) = runTest {
        coEvery { getSuggestedCountriesUseCase(any()) } throws exception

        viewModel.onChangeSearchKeyword("eg")
        advanceTimeBy(500L)
        advanceUntilIdle()

        viewModel.errorState.test {
            advanceTimeBy(500)
            assertThat(awaitItem()).isEqualTo(expectedState)
        }
    }

    private companion object {

        val countryUiState = CountryItemUiState("Netherlands", "NL")

        @JvmStatic
        fun exceptionToStateProvider() = listOf(
            Arguments.of(NoInternetException(), ErrorUiState.NoInternetError),
            Arguments.of(Exception(), ErrorUiState.UnknownError)
        )

        fun createCombinedLoadState(loadState: LoadState): CombinedLoadStates =
            CombinedLoadStates(
                refresh = loadState,
                prepend = loadState,
                append = loadState,
                source = LoadStates(loadState, loadState, loadState),
                mediator = LoadStates(loadState, loadState, loadState),
            )
    }
}