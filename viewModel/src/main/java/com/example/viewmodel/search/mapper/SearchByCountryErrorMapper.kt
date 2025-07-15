package com.example.viewmodel.search.mapper

import com.example.domain.exceptions.AflamiException
import com.example.domain.exceptions.NoInternetException
import com.example.viewmodel.search.countrySearch.SearchByCountryContentUIState

internal fun AflamiException.toSearchByCountryState(): SearchByCountryContentUIState {
    return when (this) {
        is NoInternetException -> SearchByCountryContentUIState.NO_INTERNET_CONNECTION
        else -> SearchByCountryContentUIState.NO_DATA_FOUND
    }
}