package com.amsterdam.aflami.di

import com.example.viewmodel.movieDetails.MovieDetailsArgs
import com.example.viewmodel.movieDetails.MovieDetailsViewModel
import com.example.viewmodel.search.GlobalSearchViewModel
import com.example.viewmodel.search.countrySearch.SearchByCountryViewModel
import com.example.viewmodel.searchByActor.SearchByActorViewModel
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
    singleOf(::MovieDetailsArgs)
    viewModelOf(::MovieDetailsViewModel)
}
