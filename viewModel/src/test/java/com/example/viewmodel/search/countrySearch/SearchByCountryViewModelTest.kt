package com.example.viewmodel.search.countrySearch

import com.example.domain.usecase.GetMoviesByCountryUseCase
import com.example.domain.usecase.GetSuggestedCountriesUseCase
import com.example.entity.Country
import com.example.viewmodel.search.mapper.toUiState
import com.example.viewmodel.utils.TestDispatcherProvider
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
            testDispatcherProvider
        )
    }

    @Test
    fun `onCountryNameUpdated should update the selected country name when its call`() {
        // arrange
        val countryName = "egypt"
        // act
        viewModel.onCountryNameUpdated(countryName)
        //assert
        assertThat(viewModel.state.value.selectedCountry).isEqualTo(countryName)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `onCountryNameUpdated should update suggestedCountries when getSuggestedCountriesUseCase return non empty list`() = testScope.runTest{
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
    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `onCountryNameUpdated should update SearchByCountryEffect with ShowCountriesDropDown when getSuggestedCountriesUseCase return non empty list`() =  testScope.runTest {
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
        assertThat(effects[1]).isEqualTo(SearchByCountryEffect.ShowCountriesDropDown)
    }
}
