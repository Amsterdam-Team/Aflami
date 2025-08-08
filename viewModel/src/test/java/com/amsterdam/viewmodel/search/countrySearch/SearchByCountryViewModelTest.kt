package com.amsterdam.viewmodel.search.countrySearch

import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.paging.LoadStates
import com.amsterdam.domain.exceptions.NetworkException
import com.amsterdam.domain.exceptions.NoInternetException
import com.amsterdam.domain.useCase.search.GetMoviesByCountryUseCase
import com.amsterdam.domain.useCase.search.GetSuggestedCountriesUseCase
import com.amsterdam.entity.Country
import com.amsterdam.viewmodel.utils.TestDispatcherProvider
import com.google.common.truth.Truth
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource

@OptIn(ExperimentalCoroutinesApi::class)
class SearchByCountryViewModelTest {

    private lateinit var viewModel: CountrySearchViewModel
    private val getSuggestedCountriesUseCase: GetSuggestedCountriesUseCase = mockk(relaxed = true)
    private val getMoviesByCountryUseCase: GetMoviesByCountryUseCase = mockk(relaxed = true)
    private val testDispatcherProvider = TestDispatcherProvider()
    private var testScope = TestScope(
        testDispatcherProvider.testDispatcher
    )

    @BeforeEach
    fun setUp() {
        viewModel = CountrySearchViewModel(
            getSuggestedCountriesUseCase = getSuggestedCountriesUseCase,
            getMoviesByCountryUseCase = getMoviesByCountryUseCase,
            dispatcherProvider = testDispatcherProvider
        )
    }

    @Test
    fun `should nav back when onNavigateBackClicked`() = testScope.runTest {
        // Given
        var effects = mutableListOf<CountrySearchEffect?>()
        val collectJob = testScope.launch {
            viewModel.effect.collect {
                effects.add(it)
            }
        }

        // When
        viewModel.onClickNavigateBack()
        testScope.advanceUntilIdle()
        collectJob.cancel()

        // Then
        Truth.assertThat(effects).containsExactly(CountrySearchEffect.NavigateBack)
    }

    @Test
    fun `should nav to movie details when onClickMovieCard`() = testScope.runTest {
        // Given
        var effects = mutableListOf<CountrySearchEffect?>()
        val collectJob = testScope.launch {
            viewModel.effect.collect {
                effects.add(it)
            }
        }

        // When
        viewModel.onClickMovieCard(1L)
        testScope.advanceUntilIdle()
        collectJob.cancel()

        // Then
        Truth.assertThat(effects).containsExactly(CountrySearchEffect.NavigateToMovieDetails)
    }

    @Test
    fun `should update the selected country name when onKeywordValueChanged`() = testScope.runTest {
        // Given
        val keyword = "egypt"

        // When
        viewModel.onChangeSearchKeyword(keyword)
        testScope.advanceTimeBy(5000)
        testScope.advanceUntilIdle()

        // Then
        Truth.assertThat(viewModel.state.value.keyword).isEqualTo(keyword)
    }

    @Test
    fun `should hide countries dropDown when call it with empty string`() = testScope.runTest {
        // Given
        val keyword = ""

        // When
        viewModel.onChangeSearchKeyword(keyword)
        testScope.advanceUntilIdle()

        // Then
        Truth.assertThat(viewModel.state.value.showSuggestedCountries).isFalse()
    }

    @Test
    fun `should take no action when debounce is running`() = testScope.runTest {
        // Given
        val keyword = "a"
        coEvery { getSuggestedCountriesUseCase(any()) } returns emptyList()

        // When
        viewModel.onChangeSearchKeyword(keyword)

        // Then
        coVerify(exactly = 0) { getSuggestedCountriesUseCase(keyword) }
        Truth.assertThat(viewModel.state.value.suggestedCountries).isEqualTo(emptyList<CountryItemUiState>())
    }

    @Test
    fun `should not show countries dropDown when getSuggestedCountriesUseCase return empty list`() =
        testScope.runTest {
            // Given
            val keyword = "abc"
            val countries = emptyList<Country>()
            coEvery { getSuggestedCountriesUseCase(keyword) } returns countries

            // When
            viewModel.onChangeSearchKeyword(keyword)
            testScope.advanceUntilIdle()

            // Then
            Truth.assertThat(viewModel.state.value.showSuggestedCountries && viewModel.state.value.suggestedCountries.isNotEmpty())
                .isFalse()
        }

    @Test
    fun `should reload countries when onClickRetry called without selecting country`() =
        testScope.runTest {
            // Given
            val keyword = "eg"
            val countriesUiState = listOf(CountryItemUiState("Egypt", "eg"))
            val countries = listOf(Country("Egypt", "eg"))

            // When
            coEvery { getSuggestedCountriesUseCase(any()) } throws NetworkException()
            viewModel.onChangeSearchKeyword(keyword)
            testScope.advanceUntilIdle()
            coEvery { getSuggestedCountriesUseCase(any()) } returns countries
            viewModel.onClickRetry()
            testScope.advanceUntilIdle()

            // Then
            Truth.assertThat(viewModel.state.value.suggestedCountries).isEqualTo(countriesUiState)
        }

    @Test
    fun `should load countries when user types quickly use case is called only once with the last keyword`() =
        testScope.runTest {
            // Given
            val expectedKeyword = "Netherlands"
            val fakeCountries = listOf(Country("Netherlands", "NL"))
            coEvery { getSuggestedCountriesUseCase(expectedKeyword) } returns fakeCountries

            // When
            viewModel.onChangeSearchKeyword("Nether")
            testScope.advanceTimeBy(100L)
            viewModel.onChangeSearchKeyword("Netherland")
            testScope.advanceTimeBy(100L)
            viewModel.onChangeSearchKeyword("Netherlands")
            testScope.advanceTimeBy(500L)
            testScope.advanceUntilIdle()

            // Then
            val finalState = viewModel.state.value // Using viewModel.state as you specified
            coVerify(exactly = 1) { getSuggestedCountriesUseCase(expectedKeyword) }
            Truth.assertThat(finalState.suggestedCountries).isEqualTo(fakeCountries.toUiState())
        }

    @Test
    fun `should do nothing when pagination load changed to loading when selectedCountryIsoCode is empty`() =
        testScope.runTest {
            // Given
            val loadState = LoadState.Loading

            // When
            viewModel.onPagingLoadStateChanged(
                CombinedLoadStates(
                    refresh = loadState,
                    prepend = loadState,
                    append = loadState,
                    source = LoadStates(loadState, loadState, loadState),
                    mediator = LoadStates(loadState, loadState, loadState),
                )
            )

            // Then
            Truth.assertThat(viewModel.state.value.isLoading).isFalse()
        }

    @Test
    fun `should set loading to true when pagination load changed to loading when selectedCountryIsoCode has value`() =
        testScope.runTest {
            // Given
            val loadState = LoadState.Loading
            val countryUiState = CountryItemUiState("Egypt", "eg")

            // When
            viewModel.onSelectCountry(countryUiState)
            testScope.advanceUntilIdle()
            viewModel.onPagingLoadStateChanged(
                CombinedLoadStates(
                    refresh = loadState,
                    prepend = loadState,
                    append = loadState,
                    source = LoadStates(loadState, loadState, loadState),
                    mediator = LoadStates(loadState, loadState, loadState),
                )
            )

            // Then
            Truth.assertThat(viewModel.state.value.isLoading).isTrue()
        }

    @Test
    fun `should set loading to false when pagination load changed to not loading`() =
        testScope.runTest {
            // Given
            val loadState = LoadState.NotLoading(true)

            // When
            viewModel.onPagingLoadStateChanged(
                CombinedLoadStates(
                    refresh = loadState,
                    prepend = loadState,
                    append = loadState,
                    source = LoadStates(loadState, loadState, loadState),
                    mediator = LoadStates(loadState, loadState, loadState),
                )
            )

            // Then
            Truth.assertThat(viewModel.state.value.isLoading).isFalse()
        }

    @Test
    fun `should set error ui state when pagination load changed to error`() = testScope.runTest {
        // Given
        val loadState = LoadState.Error(NetworkException())
        loadState.error

        // When
        viewModel.onPagingLoadStateChanged(
            CombinedLoadStates(
                refresh = loadState,
                prepend = loadState,
                append = loadState,
                source = LoadStates(loadState, loadState, loadState),
                mediator = LoadStates(loadState, loadState, loadState),
            )
        )

        // Then
        Truth.assertThat(viewModel.state.value.errorUiState).isEqualTo(CountrySearchErrorState.NoNetworkConnection)
    }


    @Test
    fun `when use case fails, error state is updated`() = testScope.runTest {
        // Given
        val keyword = "error"
        val fakeException = NetworkException()
        coEvery { getSuggestedCountriesUseCase(keyword) } throws fakeException

        // When
        viewModel.onChangeSearchKeyword(keyword)
        testScope.advanceUntilIdle()

        // Then
        val finalState = viewModel.state.value
        Truth.assertThat(finalState.showSuggestedCountries).isFalse()
        Truth.assertThat(finalState.errorUiState is CountrySearchErrorState.NoNetworkConnection).isTrue()
    }

    @Test
    fun `when user types blank text, use case is not called`() = testScope.runTest {
        // When
        viewModel.onChangeSearchKeyword("   ")
        testScope.advanceUntilIdle()

        // Then
        coVerify(exactly = 0) { getSuggestedCountriesUseCase(any()) }
    }

    @ParameterizedTest
    @MethodSource("exceptionToStateProvider")
    fun `should set different error state when throw exception from getSuggestedCountriesUseCase after selecting country`(
        exception: Exception,
        expectedState: CountrySearchErrorState,
    ) = testScope.runTest {
        // Given
        val keyword = "eg"
        coEvery { getSuggestedCountriesUseCase(any()) } throws exception

        // When
        viewModel.onChangeSearchKeyword(keyword)
        testScope.advanceTimeBy(5000L)
        testScope.advanceUntilIdle()
        val res = viewModel.state.value.errorUiState

        // Then
        Truth.assertThat(res).isEqualTo(expectedState)
    }

    companion object {
        @JvmStatic
        fun exceptionToStateProvider() = listOf(
            Arguments.of(
                NoInternetException(), CountrySearchErrorState.NoNetworkConnection
            ), Arguments.of(Exception(), CountrySearchErrorState.UnknownError)
        )
    }
}