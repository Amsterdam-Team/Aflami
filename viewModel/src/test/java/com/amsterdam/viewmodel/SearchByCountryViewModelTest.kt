package com.amsterdam.viewmodel

import com.amsterdam.domain.exceptions.AflamiException
import com.amsterdam.domain.exceptions.CountryTooShortException
import com.amsterdam.domain.exceptions.InternetConnectionException
import com.amsterdam.domain.exceptions.NoMoviesForCountryException
import com.amsterdam.domain.exceptions.NoSuggestedCountriesException
import com.amsterdam.domain.useCase.GetMoviesByCountryUseCase
import com.amsterdam.domain.useCase.GetSuggestedCountriesUseCase
import com.amsterdam.entity.Country
import com.amsterdam.viewmodel.search.countrySearch.CountryUiState
import com.amsterdam.viewmodel.search.countrySearch.SearchByCountryEffect
import com.amsterdam.viewmodel.search.countrySearch.SearchByCountryViewModel
import com.amsterdam.viewmodel.search.mapper.toListOfUiState
import com.amsterdam.viewmodel.search.mapper.toUiState
import com.amsterdam.viewmodel.utils.TestDispatcherProvider
import com.amsterdam.viewmodel.utils.entityHelper.createMovie
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource

@OptIn(ExperimentalCoroutinesApi::class)
class SearchByCountryViewModelTest {

    private lateinit var viewModel: SearchByCountryViewModel

    private val testDispatcherProvider = TestDispatcherProvider()
    private val getSuggestedCountriesUseCase: GetSuggestedCountriesUseCase = mockk(relaxed = true)
    private val getMoviesByCountryUseCase: GetMoviesByCountryUseCase = mockk(relaxed = true)
    private var testScope = TestScope(
        testDispatcherProvider.testDispatcher
    )

    @BeforeEach
    fun setUp() {
        viewModel = SearchByCountryViewModel(
            getSuggestedCountriesUseCase = getSuggestedCountriesUseCase,
            getMoviesByCountryUseCase = getMoviesByCountryUseCase,
            dispatcherProvider =  testDispatcherProvider
        )
    }

    @Test
    fun `onCountryNameUpdated should update the selected country name when its call`() {
        // arrange
        val countryName = "egypt"
        // act
        viewModel.onCountryNameUpdated(countryName)
        testScope.advanceUntilIdle()
        //assert
        assertThat(viewModel.state.value.selectedCountry).isEqualTo(countryName)
    }

    @Test
    fun `onCountryNameUpdated should send HideCountriesDropDown when call it with empty string`() =
        testScope.runTest {
            // arrange
            val countryName = ""

            var effects = mutableListOf<SearchByCountryEffect?>()
            // act
            val collectJob = launch {
                viewModel.effect.collect {
                    effects.add(it)
                }
            }
            viewModel.onCountryNameUpdated(countryName)
            testScope.advanceUntilIdle()
            collectJob.cancel()
            //assert
            assertThat(effects.first()).isEqualTo(SearchByCountryEffect.HideCountriesDropDown)
        }

    @Test
    fun `onCountryNameUpdated should update suggestedCountries when getSuggestedCountriesUseCase return non empty list`() =
        testScope.runTest {
            // arrange
            val countryName = "a"
            val countries = listOf(
                Country("America", ""),
                Country("Austria", "")
            )
            // act
            viewModel.onCountryNameUpdated(countryName)
            coEvery { getSuggestedCountriesUseCase.invoke(countryName) } returns countries
            testScope.advanceUntilIdle()
            //assert
            assertThat(viewModel.state.value.suggestedCountries).isEqualTo(countries.toUiState())
        }

    @Test
    fun `onCountryNameUpdated should send ShowCountriesDropDown when getSuggestedCountriesUseCase return non empty list`() =
        testScope.runTest {
            // arrange
            val countryName = "a"
            val countries = listOf(
                Country("America", ""),
                Country("Austria", "")
            )
            var effects = mutableListOf<SearchByCountryEffect?>()
            // act
            coEvery { getSuggestedCountriesUseCase.invoke(countryName) } returns countries
            val collectJob = launch {
                viewModel.effect.collect {
                    effects.add(it)
                }
            }
            viewModel.onCountryNameUpdated(countryName)
            testScope.advanceUntilIdle()
            collectJob.cancel()
            //assert
            assertThat(effects.last()).isEqualTo(SearchByCountryEffect.ShowCountriesDropDown)
        }

    @Test
    fun `onCountryNameUpdated should not send ShowCountriesDropDown when getSuggestedCountriesUseCase return empty list`() =
        testScope.runTest {
            // arrange
            val countryName = "abc"
            val countries = emptyList<Country>()
            var effects = mutableListOf<SearchByCountryEffect?>()
            // act
            coEvery { getSuggestedCountriesUseCase.invoke(countryName) } returns countries
            val collectJob = launch {
                viewModel.effect.collect {
                    effects.add(it)
                }
            }
            viewModel.onCountryNameUpdated(countryName)
            testScope.advanceUntilIdle()
            collectJob.cancel()
            //assert
            assertThat(effects).doesNotContain(SearchByCountryEffect.ShowCountriesDropDown)
        }
    @Test
    fun `onCountryNameUpdated should send LoadingSuggestedCountriesEffect when call getSuggestedCountriesUseCase `() =
        testScope.runTest {
            // arrange
            val countryName = "a"
            val countries = listOf(
                Country("America", ""),
                Country("Austria", "")
            )
            var effects = mutableListOf<SearchByCountryEffect?>()
            // act
            coEvery { getSuggestedCountriesUseCase.invoke(countryName) } returns countries
            val collectJob = launch {
                viewModel.effect.collect {
                    effects.add(it)
                }
            }
            viewModel.onCountryNameUpdated(countryName)
            testScope.advanceUntilIdle()
            collectJob.cancel()
            //assert
            assertThat(effects.first()).isEqualTo(SearchByCountryEffect.LoadingSuggestedCountriesEffect)
        }

    @ParameterizedTest
    @MethodSource("exceptionToEffectProvider")
    fun `onCountryNameUpdated should send different Errors Effect when call getSuggestedCountriesUseCase`(
        exception: AflamiException,
        expectedEffect: SearchByCountryEffect
    ) = testScope.runTest {
        // arrange
        val anyCountryName = "abc"
        var effects = mutableListOf<SearchByCountryEffect?>()
        // act
        coEvery { getSuggestedCountriesUseCase.invoke(anyCountryName) } throws exception
        val collectJob = launch {
            viewModel.effect.collect {
                effects.add(it)
            }
        }
        viewModel.onCountryNameUpdated(anyCountryName)
        testScope.advanceUntilIdle()
        collectJob.cancel()
        //assert
        assertThat(effects.last()).isEqualTo(expectedEffect)
    }

    @Test
    fun `onSelectCountry should send HideCountriesDropDown when its call`() = testScope.runTest {
        // arrange
        val countryUiState = CountryUiState("a","+20")
        val countryName = "a"
        val countries = listOf(
            Country("America", ""),
            Country("Austria", "")
        )
        var effects = mutableListOf<SearchByCountryEffect?>()
        viewModel.onCountryNameUpdated(countryName)
        coEvery { getSuggestedCountriesUseCase.invoke(countryName) } returns countries
        // act
        val collectJob = launch {
            viewModel.effect.collect {
                effects.add(it)
            }
        }
        viewModel.onSelectCountry(countryUiState)
        testScope.advanceUntilIdle()
        collectJob.cancel()
        //assert
        assertThat(effects.first()).isEqualTo(SearchByCountryEffect.HideCountriesDropDown)
    }

    @Test
    fun `onSelectCountry should send LoadingMoviesEffect when its call`() = testScope.runTest {
        // arrange
        val countryUiState = CountryUiState("eg","+20")
        var effects = mutableListOf<SearchByCountryEffect?>()
        // act
        val collectJob = launch {
            viewModel.effect.collect {
                effects.add(it)
            }
        }
        viewModel.onSelectCountry(countryUiState)
        testScope.advanceUntilIdle()
        collectJob.cancel()
        //assert
        assertThat(effects[1]).isEqualTo(SearchByCountryEffect.LoadingMoviesEffect)
    }

    @Test
    fun `onSelectCountry should update movies state when getMoviesByCountryUseCase return list of movie `() = testScope.runTest {
        // arrange
        val countryUiState = CountryUiState("eg","+20")
        val movies = listOf(createMovie())
        // act
        coEvery { getMoviesByCountryUseCase(any()) } returns movies
        viewModel.onSelectCountry(countryUiState)
        testScope.advanceUntilIdle()
        //assert
        assertThat(viewModel.state.value.movies).isEqualTo(movies.toListOfUiState())
    }

    @Test
    fun `onSelectCountry should send MoviesLoadedEffect when getMoviesByCountryUseCase return list of movie`() = testScope.runTest {
        // arrange
        val countryUiState = CountryUiState("eg","+20")
        var effects = mutableListOf<SearchByCountryEffect?>()
        val movies = listOf(createMovie())
        // act
        coEvery { getMoviesByCountryUseCase(any()) } returns movies
        val collectJob = launch {
            viewModel.effect.collect {
                effects.add(it)
            }
        }
        viewModel.onSelectCountry(countryUiState)
        testScope.advanceUntilIdle()
        collectJob.cancel()
        //assert
        assertThat(effects.last()).isEqualTo(SearchByCountryEffect.MoviesLoadedEffect)
    }

    @ParameterizedTest
    @MethodSource("exceptionToEffectProvider")
    fun `onSelectCountry should send different Errors Effect when call getSuggestedCountriesUseCase`(
        exception: AflamiException,
        expectedEffect: SearchByCountryEffect
    ) = testScope.runTest {
        // arrange
        val countryUiState = CountryUiState("eg","+20")
        var effects = mutableListOf<SearchByCountryEffect?>()
        // act
        coEvery { getMoviesByCountryUseCase(any()) } throws exception
        val collectJob = launch {
            viewModel.effect.collect {
                effects.add(it)
            }
        }
        viewModel.onSelectCountry(countryUiState)
        testScope.advanceUntilIdle()
        collectJob.cancel()
        //assert
        assertThat(effects.last()).isEqualTo(expectedEffect)
    }

    companion object {
        @JvmStatic
        fun exceptionToEffectProvider() = listOf(
            Arguments.of(
                InternetConnectionException(),
                SearchByCountryEffect.NoInternetConnectionEffect
            ),
            Arguments.of(
                NoSuggestedCountriesException(),
                SearchByCountryEffect.NoSuggestedCountriesEffect
            ),
            Arguments.of(NoMoviesForCountryException(), SearchByCountryEffect.NoMoviesEffect),
            Arguments.of(CountryTooShortException(), SearchByCountryEffect.CountryTooShortEffect),
            Arguments.of(AflamiException(), SearchByCountryEffect.UnknownErrorEffect)
        )
    }

}