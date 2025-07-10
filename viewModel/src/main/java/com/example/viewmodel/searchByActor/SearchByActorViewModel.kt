package com.example.viewmodel.searchByActor

import androidx.lifecycle.viewModelScope
import com.example.viewmodel.BaseViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch

@OptIn(FlowPreview::class)
class SearchByActorViewModel(
   // private val getSearchByActorUseCase: GetSearchByActorUseCase
) : BaseViewModel<SearchByActorUiState, SearchByActorEffect>(SearchByActorUiState()) {

    private val searchQuery = MutableStateFlow("")

    init {
        viewModelScope.launch {
            searchQuery
                .debounce(300)
                .collect { query ->
                    if (query.isNotBlank()) {
                        getResults(query)
                    }
                }
        }
    }

    fun onQueryChange(query: String) {
        updateState { it.copy(query = query) }
        searchQuery.value = query
    }

    private fun getResults(query: String) {
        updateState { it.copy(isLoading = true, results = emptyList()) }

        tryToExecute(
            function = {
               // getSearchByActorUseCase(query)
            },
            onSuccess = { result ->
                updateState {
                    it.copy(
                       // results = result,
                        isLoading = false,
                       // isEmpty = result.isEmpty()
                    )
                }

                if (result.isEmpty()) {
                    sendNewEffect(SearchByActorEffect.NoResultsFound)
                }
            },
            onError = {
                updateState { it.copy(isLoading = false) }
                sendNewEffect(SearchByActorEffect.ShowError("Something went wrong"))
            }
        )
    }

    fun onBackClicked() {
        sendNewEffect(SearchByActorEffect.NavigateBack)
    }

    fun onClearQuery() {
        updateState { it.copy(query = "", results = emptyList()) }
        searchQuery.value = ""
    }

    fun onNavigateToDetails() {
        sendNewEffect(SearchByActorEffect.NavigateToDetails)
    }
}



