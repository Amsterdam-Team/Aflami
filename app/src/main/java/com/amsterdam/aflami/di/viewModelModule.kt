package com.amsterdam.aflami.di

import com.amsterdam.viewmodel.search.GlobalSearchViewModel
import com.amsterdam.viewmodel.search.countrySearch.SearchByCountryViewModel
import com.amsterdam.viewmodel.searchByActor.SearchByActorViewModel
import com.amsterdam.viewmodel.utils.dispatcher.DefaultDispatcherProvider
import com.amsterdam.viewmodel.utils.dispatcher.DispatcherProvider
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
