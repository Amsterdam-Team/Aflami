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
import org.junit.jupiter.api.AfterEach
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
        Dispatchers.setMain(testDispatcherProvider.testDispatcher)
        viewModel = SearchByCountryViewModel(
            getSuggestedCountriesUseCase = getSuggestedCountriesUseCase,
            getMoviesByCountryUseCase = getMoviesByCountryUseCase,
            dispatcherProvider = testDispatcherProvider
        )
    }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `should update the selected country name when its call`() =
        testScope.runTest {
            val countryName = "egypt"

            viewModel.onCountryNameUpdated(countryName)
            testScope.advanceTimeBy(5000)
            testScope.advanceUntilIdle()

            assertThat(viewModel.state.value.selectedCountry).isEqualTo(countryName)
        }

    @Test
    fun `should send HideCountriesDropDown when call it with empty string`() =
        testScope.runTest {
            val countryName = ""
            var effects = mutableListOf<SearchByCountryEffect?>()
            val collectJob = launch {
                viewModel.effect.collect {
                    effects.add(it)
                }
            }

            viewModel.onCountryNameUpdated(countryName)
            testScope.advanceUntilIdle()
            collectJob.cancel()

            assertThat(effects.first()).isEqualTo(SearchByCountryEffect.HideCountriesDropDown)
        }

    @Test
    fun `should take no action when debounce is running`() =
        testScope.runTest {
            val countryName = "a"
            coEvery { getSuggestedCountriesUseCase(any()) } returns emptyList()

            viewModel.onCountryNameUpdated(countryName)

            coVerify(exactly = 0) { getSuggestedCountriesUseCase(countryName) }
            assertThat(viewModel.state.value.suggestedCountries).isEqualTo(emptyList<CountryUiState>())
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
    fun `should send ShowCountriesDropDown when getSuggestedCountriesUseCase return non empty list`() =
        testScope.runTest {
            val countryName = "a"
            val countries = listOf(
                Country("America", ""),
                Country("Austria", "")
            )
            var effects = mutableListOf<SearchByCountryEffect?>()
            coEvery { getSuggestedCountriesUseCase(countryName) } returns countries
            val collectJob = launch {
                viewModel.effect.collect {
                    effects.add(it)
                }
            }

            viewModel.onCountryNameUpdated(countryName)
            testScope.advanceUntilIdle()
            collectJob.cancel()

            assertThat(effects.last()).isEqualTo(SearchByCountryEffect.ShowCountriesDropDown)
        }

    @Test
    fun `should not send ShowCountriesDropDown when getSuggestedCountriesUseCase return empty list`() =
        testScope.runTest {
            val countryName = "abc"
            val countries = emptyList<Country>()
            var effects = mutableListOf<SearchByCountryEffect?>()
            coEvery { getSuggestedCountriesUseCase(countryName) } returns countries
            val collectJob = launch {
                viewModel.effect.collect {
                    effects.add(it)
                }
            }

            viewModel.onCountryNameUpdated(countryName)
            testScope.advanceUntilIdle()
            collectJob.cancel()

            assertThat(effects).doesNotContain(SearchByCountryEffect.ShowCountriesDropDown)
        }

    @Test
    fun `should send LoadingSuggestedCountriesEffect when call getSuggestedCountriesUseCase `() =
        testScope.runTest {
            val countryName = "a"
            val countries = listOf(
                Country("America", ""),
                Country("Austria", "")
            )
            var effects = mutableListOf<SearchByCountryEffect?>()
            coEvery { getSuggestedCountriesUseCase(countryName) } returns countries
            val collectJob = launch {
                viewModel.effect.collect {
                    effects.add(it)
                }
            }

            viewModel.onCountryNameUpdated(countryName)
            delay(500L)
            testScope.advanceUntilIdle()
            collectJob.cancel()

            assertThat(effects.first()).isEqualTo(SearchByCountryEffect.LoadingSuggestedCountriesEffect)
        }

    @ParameterizedTest
    @MethodSource("exceptionToEffectProvider")
    fun `should send different Errors Effect when call getSuggestedCountriesUseCase after updating country name`(
        exception: AflamiException,
        expectedEffect: SearchByCountryEffect
    ) = testScope.runTest {
        val anyCountryName = "abc"
        var effects = mutableListOf<SearchByCountryEffect?>()
        coEvery { getSuggestedCountriesUseCase(anyCountryName) } throws exception
        val collectJob = launch {
            viewModel.effect.collect {
                effects.add(it)
            }
        }

        viewModel.onCountryNameUpdated(anyCountryName)
        testScope.advanceUntilIdle()
        collectJob.cancel()

        assertThat(effects.last()).isEqualTo(expectedEffect)
    }

    @Test
    fun `should send HideCountriesDropDown when its call`() = testScope.runTest {
        val countryUiState = CountryUiState("a", "+20")
        val countryName = "a"
        val countries = listOf(Country("America", ""), Country("Austria", ""))
        var effects = mutableListOf<SearchByCountryEffect?>()
        coEvery { getSuggestedCountriesUseCase(countryName) } returns countries
        val collectJob = launch {
            viewModel.effect.collect {
                effects.add(it)
            }
        }

        viewModel.onCountryNameUpdated(countryName)
        viewModel.onSelectCountry(countryUiState)
        testScope.advanceUntilIdle()
        collectJob.cancel()

        assertThat(effects.first()).isEqualTo(SearchByCountryEffect.HideCountriesDropDown)
    }

    @Test
    fun `should send LoadingMoviesEffect when its call`() = testScope.runTest {
        val countryUiState = CountryUiState("eg", "+20")
        var effects = mutableListOf<SearchByCountryEffect?>()
        val collectJob = launch {
            viewModel.effect.collect {
                effects.add(it)
            }
        }

        viewModel.onSelectCountry(countryUiState)
        testScope.advanceUntilIdle()
        collectJob.cancel()

        assertThat(effects[1]).isEqualTo(SearchByCountryEffect.LoadingMoviesEffect)
    }

    @Test
    fun `should update movies state when getMoviesByCountryUseCase return list of movie `() =
        testScope.runTest {
            val countryUiState = CountryUiState("eg", "+20")
            val movies = listOf(createMovie())
            coEvery { getMoviesByCountryUseCase(any()) } returns movies

            viewModel.onSelectCountry(countryUiState)
            testScope.advanceUntilIdle()

            assertThat(viewModel.state.value.movies).isEqualTo(movies.toListOfUiState())
        }

    @Test
    fun `should send MoviesLoadedEffect when getMoviesByCountryUseCase return list of movie`() =
        testScope.runTest {
            val countryUiState = CountryUiState("eg", "+20")
            var effects = mutableListOf<SearchByCountryEffect?>()
            val movies = listOf(createMovie())
            coEvery { getMoviesByCountryUseCase(any()) } returns movies
            val collectJob = launch {
                viewModel.effect.collect {
                    effects.add(it)
                }
            }

            viewModel.onSelectCountry(countryUiState)
            testScope.advanceUntilIdle()
            collectJob.cancel()

            assertThat(effects.last()).isEqualTo(SearchByCountryEffect.MoviesLoadedEffect)
        }

    @ParameterizedTest
    @MethodSource("exceptionToEffectProvider")
    fun `should send different Errors Effect when call getSuggestedCountriesUseCase after selecting country`(
        exception: AflamiException,
        expectedEffect: SearchByCountryEffect
    ) = testScope.runTest {
        val countryUiState = CountryUiState("eg", "+20")
        var effects = mutableListOf<SearchByCountryEffect?>()
        coEvery { getMoviesByCountryUseCase(any()) } throws exception
        val collectJob = launch {
            viewModel.effect.collect {
                effects.add(it)
            }
        }

        viewModel.onSelectCountry(countryUiState)
        testScope.advanceUntilIdle()
        collectJob.cancel()

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