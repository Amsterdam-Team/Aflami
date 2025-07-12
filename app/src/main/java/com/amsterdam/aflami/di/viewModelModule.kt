package com.amsterdam.aflami.di

import com.example.viewmodel.search.GlobalSearchViewModel
import com.example.viewmodel.search.countrySearch.SearchByCountryViewModel
import com.example.viewmodel.searchByActor.SearchByActorViewModel
import com.example.viewmodel.utils.dispatcher.DefaultDispatcherProvider
import com.example.viewmodel.utils.dispatcher.DispatcherProvider
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val viewModelModule = module {
    viewModelOf(::GlobalSearchViewModel)
    singleOf<DispatcherProvider>(::DefaultDispatcherProvider)
    viewModelOf(::SearchByCountryViewModel)
    viewModelOf(::SearchByActorViewModel)
}
