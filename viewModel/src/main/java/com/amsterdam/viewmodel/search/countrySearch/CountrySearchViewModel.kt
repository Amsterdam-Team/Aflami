package com.amsterdam.viewmodel.search.countrySearch

import androidx.lifecycle.viewModelScope
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.amsterdam.domain.exceptions.AflamiException
import com.amsterdam.domain.useCase.preferences.ManageLocaleLanguageUseCase
import com.amsterdam.domain.useCase.search.GetMoviesByCountryUseCase
import com.amsterdam.domain.useCase.search.GetSuggestedCountriesUseCase
import com.amsterdam.domain.useCase.search.RecentSearchesUseCase
import com.amsterdam.entity.Country
import com.amsterdam.paging.PagingSource
import com.amsterdam.viewmodel.search.mapper.toCountry
import com.amsterdam.viewmodel.search.mapper.toMediaItemUiState
import com.amsterdam.viewmodel.search.mapper.toUiState
import com.amsterdam.viewmodel.shared.BaseViewModel
import com.amsterdam.viewmodel.shared.uiStates.MovieItemUiState
import com.amsterdam.viewmodel.utils.debounceSearch
import com.amsterdam.viewmodel.utils.dispatcher.DispatcherProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CountrySearchViewModel @Inject constructor(
    private val getSuggestedCountriesUseCase: GetSuggestedCountriesUseCase,
    private val getMoviesByCountryUseCase: GetMoviesByCountryUseCase,
    private val recentSearchesUseCase: RecentSearchesUseCase,
    manageLocaleLanguageUseCase: ManageLocaleLanguageUseCase,
    dispatcherProvider: DispatcherProvider,
) : BaseViewModel<CountrySearchUiState, CountrySearchEffect>(
    CountrySearchUiState(),
    dispatcherProvider,
), CountrySearchInteractionListener {
    private val _keyword = MutableStateFlow("")

    init {
        manageLocaleLanguageUseCase.getDeviceLanguage()
            .onEach {
                observeKeywordFlow()
            }.launchIn(viewModelScope)

        observeKeywordFlow()
    }

    private fun observeKeywordFlow() {
        viewModelScope.launch { _keyword.debounceSearch(::fetchCountriesByKeyword) }
    }

    private fun fetchCountriesByKeyword(keyword: String): Job {
        return tryToExecute(
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
        _keyword.update { keyword }

        updateState {
            it.copy(
                keyword = keyword,
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
        saveSearchHistory()
    }

    override fun onClickRetry() {
        val hasKeyword = state.value.keyword.isNotBlank()
        val hasSelectedCountry = state.value.selectedCountryIsoCode.isNotBlank()

        when {
            !hasSelectedCountry && hasKeyword -> fetchCountriesByKeyword(state.value.keyword)
            hasSelectedCountry -> fetchMoviesByCountry(getSelectedCountry())
        }
    }

    private fun saveSearchHistory() {
        tryToExecute(
            action = { recentSearchesUseCase.addRecentSearchForCountry(getSelectedCountry()) },
            onSuccess = {},
            onError = {}
        )
    }

    private fun fetchMoviesByCountry(selectedCountry: Country) {
        updateState { it.copy(isLoading = true, showSuggestedCountries = false) }
        tryToExecute(
            action = {
                Pager(
                    config = PagingConfig(pageSize = 20),
                    pagingSourceFactory = {
                        PagingSource { page ->
                            getMoviesByCountryUseCase(
                                selectedCountry,
                                page,
                            )
                        }
                    },
                )
                    .flow.map { pagingData -> pagingData.map { it.toMediaItemUiState() } }
                    .cachedIn(viewModelScope)
            },
            onSuccess = ::onFetchMoviesSuccess,
            onError = {}
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

    private fun onFetchMoviesSuccess(movies: Flow<PagingData<MovieItemUiState>>) {
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