package com.amsterdam.aflami.di

import com.example.viewmodel.search.globalSearch.GlobalSearchViewModel
import com.example.viewmodel.search.countrySearch.SearchByCountryViewModel
import com.example.viewmodel.search.actorSearch.SearchByActorViewModel
import com.example.viewmodel.utils.dispatcher.DefaultDispatcherProvider
import com.example.viewmodel.utils.dispatcher.DispatcherProvider
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val viewModelModule = module {
    singleOf<DispatcherProvider>(::DefaultDispatcherProvider)
    viewModelOf(::GlobalSearchViewModel)
    viewModelOf(::GlobalSearchViewModel)
    viewModelOf(::SearchByCountryViewModel)
    viewModelOf(::SearchByActorViewModel)
}
