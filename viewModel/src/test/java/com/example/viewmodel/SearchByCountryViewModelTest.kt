package com.example.viewmodel

import com.example.domain.exceptions.AflamiException
import com.example.domain.exceptions.NoInternetException
import com.example.domain.useCase.GetMoviesByCountryUseCase
import com.example.domain.useCase.GetSuggestedCountriesUseCase
import com.example.domain.useCase.RecentSearchesUseCase
import com.example.entity.Country
import com.example.viewmodel.search.countrySearch.CountryItemUiState
import com.example.viewmodel.search.countrySearch.SearchByCountryContentUIState
import com.example.viewmodel.search.countrySearch.CountrySearchEffect
import com.example.viewmodel.search.countrySearch.CountrySearchViewModel
import com.example.viewmodel.search.mapper.toMoveUiStates
import com.example.viewmodel.search.mapper.toUiState
import com.example.viewmodel.utils.TestDispatcherProvider
import com.example.viewmodel.utils.entityHelper.createMovie
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
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
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource

@OptIn(ExperimentalCoroutinesApi::class)
class SearchByCountryViewModelTest {
    private lateinit var viewModel: CountrySearchViewModel
    private val testDispatcherProvider = TestDispatcherProvider()
    private val getSuggestedCountriesUseCase: GetSuggestedCountriesUseCase = mockk(relaxed = true)
    private val getMoviesByCountryUseCase: GetMoviesByCountryUseCase = mockk(relaxed = true)
    private val recentSearchesUseCase: RecentSearchesUseCase = mockk(relaxed = true)
    private var testScope = TestScope(
        testDispatcherProvider.testDispatcher
    )

    @BeforeEach
    fun setUp() {
        Dispatchers.setMain(testDispatcherProvider.testDispatcher)
        viewModel = CountrySearchViewModel(
            getSuggestedCountriesUseCase = getSuggestedCountriesUseCase,
            getMoviesByCountryUseCase = getMoviesByCountryUseCase,
            recentSearchUseCase = recentSearchesUseCase,
            dispatcherProvider = testDispatcherProvider
        )
    }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `should nav back when onNavigateBackClicked`() =
        testScope.runTest {
            var effects = mutableListOf<CountrySearchEffect?>()
            val collectJob = testScope.launch {
                viewModel.effect.collect {
                    effects.add(it)
                }
            }

            viewModel.onClickNavigateBack()
            testScope.advanceUntilIdle()
            collectJob.cancel()

            assertThat(effects).containsExactly(CountrySearchEffect.NavigateBack)
        }

    @Test
    fun `should update the selected country name when onKeywordValueChanged`() =
        testScope.runTest {
            val keyword = "egypt"

            viewModel.onChangeSearchKeyword(keyword)
            testScope.advanceTimeBy(5000)
            testScope.advanceUntilIdle()

            assertThat(viewModel.state.value.keyword).isEqualTo(keyword)
        }

    @Test
    fun `should add recent search when onCountrySelected called`() =
        testScope.runTest {
            val keyword = "eg"
            val countryUiState = CountryItemUiState("Egypt", "eg")
            coEvery { recentSearchesUseCase.addRecentSearch(any()) } returns

            viewModel.onSelectCountry(countryUiState)
            testScope.advanceUntilIdle()

            coVerify(exactly = 1) { recentSearchesUseCase.addRecentSearch(keyword) }
        }

    @Test
    fun `should fail to add recent search when onCountrySelected called`() =
        testScope.runTest {
            val keyword = "eg"
            val countryUiState = CountryItemUiState("Egypt", "eg")
            coEvery { recentSearchesUseCase.addRecentSearch(any()) } throws AflamiException()

            viewModel.onSelectCountry(countryUiState)
            testScope.advanceUntilIdle()

            coVerify(exactly = 1) { recentSearchesUseCase.addRecentSearch(keyword) }
        }

    @Test
    fun `should hide countries dropDown when call it with empty string`() =
        testScope.runTest {
            val keyword = ""

            viewModel.onChangeSearchKeyword(keyword)
            testScope.advanceUntilIdle()

            assertThat(viewModel.state.value.isCountriesDropDownVisible).isFalse()
        }

    @Test
    fun `should take no action when debounce is running`() =
        testScope.runTest {
            val keyword = "a"
            coEvery { getSuggestedCountriesUseCase(any()) } returns emptyList()

            viewModel.onChangeSearchKeyword(keyword)

            coVerify(exactly = 0) { getSuggestedCountriesUseCase(keyword) }
            assertThat(viewModel.state.value.suggestedCountries).isEqualTo(emptyList<CountryItemUiState>())
        }

    @Test
    fun `should update suggestedCountries when getSuggestedCountriesUseCase return non empty list`() =
        testScope.runTest {
            val countries = listOf(
                Country("Australia", ""),
                Country("Austria", "")
            )
            coEvery { getSuggestedCountriesUseCase("a") } coAnswers { delay(500L); emptyList() }
            coEvery { getSuggestedCountriesUseCase("au") } coAnswers { delay(500L); emptyList() }
            coEvery { getSuggestedCountriesUseCase("aus") } coAnswers { countries }

            viewModel.onChangeSearchKeyword("a")
            assertThat(viewModel.state.value.suggestedCountries).isEqualTo(emptyList<CountryItemUiState>())
            viewModel.onChangeSearchKeyword("au")
            assertThat(viewModel.state.value.suggestedCountries).isEqualTo(emptyList<CountryItemUiState>())
            viewModel.onChangeSearchKeyword("aus")
            testScope.advanceTimeBy(3000L)
            testScope.advanceUntilIdle()
            coVerify(exactly = 0) { getSuggestedCountriesUseCase("a") }
            coVerify(exactly = 0) { getSuggestedCountriesUseCase("au") }
            coVerify(exactly = 1) { getSuggestedCountriesUseCase("aus") }
            assertThat(viewModel.state.value.suggestedCountries).isEqualTo(countries.toUiState())
        }

    @Test
    fun `should show countries dropDown when getSuggestedCountriesUseCase return non empty list`() =
        testScope.runTest {
            val keyword = "a"
            val countries = listOf(
                Country("America", ""),
                Country("Austria", "")
            )
            coEvery { getSuggestedCountriesUseCase(keyword) } returns countries
            viewModel.onChangeSearchKeyword(keyword)
            testScope.advanceUntilIdle()

            assertThat(viewModel.state.value.isCountriesDropDownVisible).isTrue()
        }

    @Test
    fun `should not show countries dropDown when getSuggestedCountriesUseCase return empty list`() =
        testScope.runTest {
            val keyword = "abc"
            val countries = emptyList<Country>()
            coEvery { getSuggestedCountriesUseCase(keyword) } returns countries

            viewModel.onChangeSearchKeyword(keyword)
            testScope.advanceUntilIdle()

            assertThat(viewModel.state.value.isCountriesDropDownVisible).isFalse()
        }

    @Test
    fun `should show countries dropDown when onKeywordValueChanged called`() = testScope.runTest {
        val countryUiState = CountryItemUiState("Egypt", "eg")
        val keyword = "eg"
        val countries = listOf(Country("Egypt", "eg"), Country("Senegal", "sg"))
        coEvery { getSuggestedCountriesUseCase(keyword) } returns countries

        viewModel.onChangeSearchKeyword(keyword)
        testScope.advanceTimeBy(500)
        viewModel.onSelectCountry(countryUiState)
        testScope.advanceUntilIdle()

        assertThat(viewModel.state.value.isCountriesDropDownVisible).isFalse()
    }

    @Test
    fun `should set state to LOADING_MOVIES when call getMoviesByCountry`() = testScope.runTest {
        val countryUiState = CountryItemUiState("Egypt", "eg")

        viewModel.onSelectCountry(countryUiState)
        testScope.advanceUntilIdle()

        assertThat(viewModel.state.value.searchByCountryContentUIState).isEqualTo(
            SearchByCountryContentUIState.NO_DATA_FOUND
        )
    }

    @Test
    fun `should update movies state when getMoviesByCountryUseCase return list of movie `() =
        testScope.runTest {
            val countryUiState = CountryItemUiState("Egypt", "eg")
            val movies = listOf(createMovie())
            coEvery { getMoviesByCountryUseCase(any()) } returns movies

            viewModel.onSelectCountry(countryUiState)
            testScope.advanceUntilIdle()

            assertThat(viewModel.state.value.movies).isEqualTo(movies.toMoveUiStates())
        }

    @Test
    fun `should set state to MOVIES_LOADED when getMoviesByCountryUseCase return list of movie`() =
        testScope.runTest {
            val countryUiState = CountryItemUiState("Egypt", "eg")
            val movies = listOf(createMovie())
            coEvery { getMoviesByCountryUseCase(any()) } returns movies

            viewModel.onSelectCountry(countryUiState)
            testScope.advanceUntilIdle()

            assertThat(viewModel.state.value.searchByCountryContentUIState).isEqualTo(
                SearchByCountryContentUIState.MOVIES_LOADED
            )
        }

    @Test
    fun `should reload countries when onRetryRequestClicked calls getCountriesByKeyword`() =
        testScope.runTest {
            val keyword = "eg"
            val countriesUiState =
                listOf(CountryItemUiState("Egypt", "eg"), CountryItemUiState("Senegal", "sg"))
            val countries = listOf(Country("Egypt", "eg"), Country("Senegal", "sg"))
            coEvery { getSuggestedCountriesUseCase(any()) } returns countries

            viewModel.onChangeSearchKeyword(keyword)
            testScope.advanceUntilIdle()
            viewModel.onClickRetry()
            testScope.advanceUntilIdle()

            assertThat(viewModel.state.value.suggestedCountries).isEqualTo(countriesUiState)
        }

    @Test
    fun `should reload movies when onRetryRequestClicked calls getMoviesByCountry`() =
        testScope.runTest {
            val countryUiState = CountryItemUiState("Egypt", "eg")
            val movies = listOf(createMovie())
            coEvery { getMoviesByCountryUseCase(any()) } returns movies

            viewModel.onSelectCountry(countryUiState)
            testScope.advanceUntilIdle()
            viewModel.onClickRetry()
            testScope.advanceUntilIdle()

            assertThat(viewModel.state.value.movies).isEqualTo(movies.toMoveUiStates())
        }

    @Test
    fun `should set content ui state to COUNTRY_TOUR when onRetryRequestClicked cannot reload`() =
        testScope.runTest {
            viewModel.onClickRetry()
            testScope.advanceUntilIdle()

            assertThat(viewModel.state.value.searchByCountryContentUIState).isEqualTo(
                SearchByCountryContentUIState.COUNTRY_TOUR
            )
        }

    @ParameterizedTest
    @MethodSource("exceptionToStateProvider")
    fun `should set different error state when throw exception from getSuggestedCountriesUseCase after selecting country`(
        exception: Exception,
        expectedState: SearchByCountryContentUIState,
    ) = testScope.runTest {
        val countryUiState = CountryItemUiState("Egypt", "eg")
        coEvery { getMoviesByCountryUseCase(any()) } throws exception

        viewModel.onSelectCountry(countryUiState)
        testScope.advanceUntilIdle()

        val res = viewModel.state.value.searchByCountryContentUIState
        assertThat(res).isEqualTo(expectedState)
    }

    companion object {
        @JvmStatic
        fun exceptionToStateProvider() = listOf(
            Arguments.of(
                NoInternetException(),
                SearchByCountryContentUIState.NO_INTERNET_CONNECTION
            ),
            Arguments.of(AflamiException(), SearchByCountryContentUIState.NO_DATA_FOUND)
        )
    }

}