package com.example.viewmodel

import com.example.domain.exceptions.AflamiException
import com.example.domain.exceptions.CountryTooShortException
import com.example.domain.exceptions.InternetConnectionException
import com.example.domain.exceptions.NoMoviesForCountryException
import com.example.domain.exceptions.NoSuggestedCountriesException
import com.example.domain.useCase.GetMoviesByCountryUseCase
import com.example.domain.useCase.GetSuggestedCountriesUseCase
import com.example.entity.Country
import com.example.viewmodel.search.countrySearch.CountryUiState
import com.example.viewmodel.search.countrySearch.SearchByCountryEffect
import com.example.viewmodel.search.countrySearch.SearchByCountryViewModel
import com.example.viewmodel.search.mapper.toListOfUiState
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
        Dispatchers.resetMain()
        Dispatchers.setMain(testDispatcherProvider.testDispatcher)
        viewModel = SearchByCountryViewModel(
            getSuggestedCountriesUseCase = getSuggestedCountriesUseCase,
            getMoviesByCountryUseCase = getMoviesByCountryUseCase,
            dispatcherProvider =  testDispatcherProvider
        )
    }

    @Test
    fun `onCountryNameUpdated should update the selected country name when its call`() =
        testScope.runTest {
        // arrange
        val countryName = "egypt"
        // act
        viewModel.onCountryNameUpdated(countryName)
        testScope.advanceTimeBy(5000)
        testScope.advanceUntilIdle()
        // assert
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
            // assert
            assertThat(effects.first()).isEqualTo(SearchByCountryEffect.HideCountriesDropDown)
        }

    @Test
    fun `onCountryNameUpdated should take no action when debounce is running`() =
        testScope.runTest {
            // arrange
            val countryName = "a"
            coEvery { getSuggestedCountriesUseCase(any()) } returns emptyList()
            // act
            viewModel.onCountryNameUpdated(countryName)
            // assert
            coVerify(exactly = 0) { getSuggestedCountriesUseCase(countryName) }
            assertThat(viewModel.state.value.suggestedCountries).isEqualTo(emptyList<CountryUiState>())
        }

    @Test
    fun `onCountryNameUpdated should update suggestedCountries when getSuggestedCountriesUseCase return non empty list`() =
        testScope.runTest {
            // arrange
            val countries = listOf(
                Country("Australia", ""),
                Country("Austria", "")
            )
            coEvery { getSuggestedCountriesUseCase("a") } coAnswers { delay(500L); emptyList() }
            coEvery { getSuggestedCountriesUseCase("au") } coAnswers { delay(500L); emptyList() }
            coEvery { getSuggestedCountriesUseCase("aus") } coAnswers { countries }
            // act & assert
            viewModel.onCountryNameUpdated("a")
            assertThat(viewModel.state.value.suggestedCountries).isEqualTo(emptyList<CountryUiState>())
            viewModel.onCountryNameUpdated("au")
            assertThat(viewModel.state.value.suggestedCountries).isEqualTo(emptyList<CountryUiState>())
            viewModel.onCountryNameUpdated("aus")
            testScope.advanceTimeBy(3000L)
            testScope.advanceUntilIdle()
            coVerify(exactly = 0) { getSuggestedCountriesUseCase("a") }
            coVerify(exactly = 0) { getSuggestedCountriesUseCase("au") }
            coVerify(exactly = 1) { getSuggestedCountriesUseCase("aus") }
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
            coEvery { getSuggestedCountriesUseCase.invoke(countryName) } returns countries
            // act
            val collectJob = launch {
                viewModel.effect.collect {
                    effects.add(it)
                }
            }
            viewModel.onCountryNameUpdated(countryName)
            testScope.advanceUntilIdle()
            collectJob.cancel()
            // assert
            assertThat(effects.last()).isEqualTo(SearchByCountryEffect.ShowCountriesDropDown)
        }

    @Test
    fun `onCountryNameUpdated should not send ShowCountriesDropDown when getSuggestedCountriesUseCase return empty list`() =
        testScope.runTest {
            // arrange
            val countryName = "abc"
            val countries = emptyList<Country>()
            var effects = mutableListOf<SearchByCountryEffect?>()
            coEvery { getSuggestedCountriesUseCase.invoke(countryName) } returns countries
            // act
            val collectJob = launch {
                viewModel.effect.collect {
                    effects.add(it)
                }
            }
            viewModel.onCountryNameUpdated(countryName)
            testScope.advanceUntilIdle()
            collectJob.cancel()
            // assert
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
            coEvery { getSuggestedCountriesUseCase.invoke(countryName) } returns countries
            // act
            val collectJob = launch {
                viewModel.effect.collect {
                    effects.add(it)
                }
            }
            viewModel.onCountryNameUpdated(countryName)
            delay(500L)
            testScope.advanceUntilIdle()
            collectJob.cancel()
            // assert
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
        coEvery { getSuggestedCountriesUseCase.invoke(anyCountryName) } throws exception
        // act
        val collectJob = launch {
            viewModel.effect.collect {
                effects.add(it)
            }
        }
        viewModel.onCountryNameUpdated(anyCountryName)
        testScope.advanceUntilIdle()
        collectJob.cancel()
        // assert
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
        // assert
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
        // assert
        assertThat(effects[1]).isEqualTo(SearchByCountryEffect.LoadingMoviesEffect)
    }

    @Test
    fun `onSelectCountry should update movies state when getMoviesByCountryUseCase return list of movie `() = testScope.runTest {
        // arrange
        val countryUiState = CountryUiState("eg","+20")
        val movies = listOf(createMovie())
        coEvery { getMoviesByCountryUseCase(any()) } returns movies
        // act
        viewModel.onSelectCountry(countryUiState)
        testScope.advanceUntilIdle()
        // assert
        assertThat(viewModel.state.value.movies).isEqualTo(movies.toListOfUiState())
    }

    @Test
    fun `onSelectCountry should send MoviesLoadedEffect when getMoviesByCountryUseCase return list of movie`() = testScope.runTest {
        // arrange
        val countryUiState = CountryUiState("eg","+20")
        var effects = mutableListOf<SearchByCountryEffect?>()
        val movies = listOf(createMovie())
        coEvery { getMoviesByCountryUseCase(any()) } returns movies
        // act
        val collectJob = launch {
            viewModel.effect.collect {
                effects.add(it)
            }
        }
        viewModel.onSelectCountry(countryUiState)
        testScope.advanceUntilIdle()
        collectJob.cancel()
        // assert
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
        coEvery { getMoviesByCountryUseCase(any()) } throws exception
        // act
        val collectJob = launch {
            viewModel.effect.collect {
                effects.add(it)
            }
        }
        viewModel.onSelectCountry(countryUiState)
        testScope.advanceUntilIdle()
        collectJob.cancel()
        // assert
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