package com.amsterdam.aflami.di

import com.example.viewmodel.search.GlobalSearchViewModel
import com.example.viewmodel.search.countrySearch.SearchByCountryViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val viewModelModule = module {
    viewModelOf(::GlobalSearchViewModel)
    viewModelOf(::SearchByCountryViewModel)
}
