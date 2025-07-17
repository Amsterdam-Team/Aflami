package com.amsterdam.aflami.di

import com.example.viewmodel.search.countrySearch.CountrySearchViewModel
import com.example.viewmodel.search.searchByKeyword.SearchViewModel
import com.example.viewmodel.search.actorSearch.ActorSearchViewModel
import com.example.viewmodel.utils.dispatcher.DefaultDispatcherProvider
import com.example.viewmodel.utils.dispatcher.DispatcherProvider
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val viewModelModule = module {
    singleOf(::DefaultDispatcherProvider) { bind<DispatcherProvider>() }

    viewModelOf(::SearchViewModel)
    viewModelOf(::CountrySearchViewModel)
    viewModelOf(::ActorSearchViewModel)
}