package com.amsterdam.viewmodel.search.countrySearch

import androidx.lifecycle.viewModelScope
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.amsterdam.domain.exceptions.AflamiException
import com.amsterdam.domain.useCase.search.GetSuggestedCountriesUseCase
import com.amsterdam.entity.Country
import com.amsterdam.viewmodel.search.mapper.toSearchMediaItemUiState
import com.amsterdam.viewmodel.search.uiState.SearchMediaItemUiState
import com.amsterdam.viewmodel.shared.BaseViewModel
import com.amsterdam.viewmodel.utils.debounceSearch
import com.amsterdam.viewmodel.utils.dispatcher.DispatcherProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class CountrySearchViewModel @Inject constructor(
    private val getSuggestedCountriesUseCase: GetSuggestedCountriesUseCase,
    private val countrySearchPagingSource: CountrySearchPagingSource,
    private val dispatcherProvider: DispatcherProvider,
) : BaseViewModel<CountrySearchUiState, CountrySearchEffect>(
    CountrySearchUiState(),
    dispatcherProvider,
), CountrySearchInteractionListener {
    init {
        observeKeywordFlow()
    }

    @OptIn(FlowPreview::class)
    private fun observeKeywordFlow() {
        tryToExecute(
            action = {
                state.map { it -> it.keywordDebounceValue.trim() }
                    .debounceSearch(::fetchCountriesByKeyword)
            },
            dispatcher = dispatcherProvider.IO
        )
    }

    private fun fetchCountriesByKeyword(keyword: String) {
        tryToExecute(
            action = { getSuggestedCountriesUseCase(keyword) },
            onSuccess = ::onFetchCountriesSuccess,
            onError = ::onFetchError
        )
    }

    private fun onFetchCountriesSuccess(countries: List<Country>) {
        updateState {
            it.copy(
                suggestedCountries = countries.toUiState(),
                showSuggestedCountries = true,
                errorUiState = null
            )
        }
    }

    private fun onFetchError(exception: AflamiException) {
        updateState {
            it.copy(
                isLoading = false,
                showSuggestedCountries = false,
                movies = emptyFlow(),
                errorUiState = CountrySearchErrorState.toCountrySearchErrorState(exception),
            )
        }
    }

    override fun onChangeSearchKeyword(keyword: String) {
        updateState {
            it.copy(
                keyword = keyword,
                keywordDebounceValue = keyword,
                isLoading = false,
                selectedCountryIsoCode = "",
                showSuggestedCountries = false,
                errorUiState = null,
                movies = emptyFlow(),
            )
        }
    }

    override fun onSelectCountry(country: CountryItemUiState) {
        updateState {
            it.copy(
                keyword = country.countryName,
                selectedCountryIsoCode = country.countryIsoCode,
            )
        }
        fetchMoviesByCountry(getSelectedCountry())
    }

    override fun onClickRetry() {
        val hasSelectedCountry = state.value.selectedCountryIsoCode.isNotBlank()

        when {
            !hasSelectedCountry -> fetchCountriesByKeyword(state.value.keyword)
            hasSelectedCountry -> fetchMoviesByCountry(getSelectedCountry())
        }
    }

    private fun fetchMoviesByCountry(selectedCountry: Country) {
        updateState { it.copy(isLoading = true, showSuggestedCountries = false) }
        tryToExecute(
            action = {
                countrySearchPagingSource.getMovies(selectedCountry)
                    .map { pagingData -> pagingData.map { it.toSearchMediaItemUiState() } }
                    .cachedIn(viewModelScope)
            },
            onSuccess = ::onFetchMoviesSuccess,
        )
    }

    override fun onClickMovieCard(movieId: Long) {
        updateState { it.copy(selectedMovieId = movieId) }
        sendNewNavigationEffect(CountrySearchEffect.NavigateToMovieDetails)
    }

    override fun onPagingLoadStateChanged(loadStates: CombinedLoadStates) {
        when (val refreshState = loadStates.refresh) {
            is LoadState.Loading -> {
                if (state.value.selectedCountryIsoCode.isNotBlank()) {
                    updateState { it.copy(isLoading = true, errorUiState = null) }
                }
            }

            is LoadState.NotLoading -> {
                updateState { it.copy(isLoading = false) }
            }

            is LoadState.Error -> {
                updateState {
                    it.copy(
                        isLoading = false,
                        errorUiState = CountrySearchErrorState.toCountrySearchErrorState(
                            refreshState.error
                        ),
                    )
                }
            }
        }
    }

    private fun getSelectedCountry(): Country {
        return state.value.suggestedCountries.find {
            state.value.selectedCountryIsoCode == it.countryIsoCode
        }.toCountry()
    }

    private fun onFetchMoviesSuccess(movies: Flow<PagingData<SearchMediaItemUiState>>) {
        updateState {
            it.copy(
                movies = movies,
                showSuggestedCountries = false,
            )
        }
    }

    override fun onClickNavigateBack() {
        sendNewNavigationEffect(CountrySearchEffect.NavigateBack)
    }
}