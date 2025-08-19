package com.amsterdam.viewmodel.search.countrySearch

import androidx.lifecycle.viewModelScope
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.amsterdam.domain.exceptions.AflamiException
import com.amsterdam.domain.useCase.search.GetMoviesByCountryUseCase
import com.amsterdam.domain.useCase.search.GetSuggestedCountriesUseCase
import com.amsterdam.entity.Country
import com.amsterdam.paging.PagingSource
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
    private val getMoviesByCountryUseCase: GetMoviesByCountryUseCase,
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
                    .debounceSearch(::getCountriesByKeyword)
            },
            dispatcher = dispatcherProvider.IO
        )
    }

    private fun getCountriesByKeyword(keyword: String) {
        tryToExecute(
            action = { getSuggestedCountriesUseCase(keyword) },
            onSuccess = ::onGetCountriesSuccess,
            onError = ::onGetCountriesError
        )
    }

    private fun onGetCountriesSuccess(countries: List<Country>) {
        resetErrorStateToNull()
        updateState {
            it.copy(
                suggestedCountries = countries.toUiState(),
                showSuggestedCountries = true,
            )
        }
    }

    private fun onGetCountriesError(exception: AflamiException) {
        updateState {
            it.copy(
                isLoading = false,
                showSuggestedCountries = false,
                movies = emptyFlow(),
            )
        }
    }

    override fun onChangeSearchKeyword(keyword: String) {
        resetErrorStateToNull()
        updateState {
            it.copy(
                keyword = keyword,
                keywordDebounceValue = keyword,
                isLoading = false,
                selectedCountryIsoCode = "",
                showSuggestedCountries = false,
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
        getMoviesByCountry(getSelectedCountry())
    }

    override fun onClickRetry() {
        val hasSelectedCountry = state.value.selectedCountryIsoCode.isNotBlank()

        when {
            !hasSelectedCountry -> getCountriesByKeyword(state.value.keyword)
            hasSelectedCountry -> getMoviesByCountry(getSelectedCountry())
        }
    }

    private fun getMoviesByCountry(selectedCountry: Country) {
        updateState { it.copy(isLoading = true, showSuggestedCountries = false) }
        tryToExecute(
            action = {
                Pager(
                    config = PagingConfig(pageSize = 20),
                    pagingSourceFactory = {
                        PagingSource { page ->
                            getMoviesByCountryUseCase(selectedCountry, page)
                                .map { it.toSearchMediaItemUiState() }
                        }
                    },
                ).flow.cachedIn(viewModelScope)
            },
            onSuccess = ::onGetMoviesSuccess,
        )
    }

    override fun onClickMovieCard(movieId: Long) {
        updateState { it.copy(selectedMovieId = movieId) }
        sendNewNavigationEffect(CountrySearchEffect.NavigateToMovieDetails)
    }

    override fun onPagingLoadStateChanged(loadStates: CombinedLoadStates) {
        when (val refreshState = loadStates.refresh) {
            is LoadState.Loading -> {
                resetErrorStateToNull()
                if (state.value.selectedCountryIsoCode.isNotBlank()) {
                    updateState { it.copy(isLoading = true) }
                }
            }

            is LoadState.NotLoading -> {
                updateState { it.copy(isLoading = false) }
            }

            is LoadState.Error -> {
                updateState { it.copy(isLoading = false) }
                updateErrorStateByException(refreshState.error as AflamiException?)
            }
        }
    }

    private fun getSelectedCountry(): Country {
        return state.value.suggestedCountries.find {
            state.value.selectedCountryIsoCode == it.countryIsoCode
        }.toCountry()
    }

    private fun onGetMoviesSuccess(movies: Flow<PagingData<SearchMediaItemUiState>>) {
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