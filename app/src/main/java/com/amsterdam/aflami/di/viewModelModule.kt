package com.amsterdam.aflami.di

import com.example.viewmodel.search.searchByKeyword.SearchViewModel
import com.example.viewmodel.movieDetails.MovieDetailsArgs
import com.example.viewmodel.movieDetails.MovieDetailsViewModel
import com.example.viewmodel.cast.CastViewModel
import com.example.viewmodel.movieDetails.MovieDetailsUiStateMapper
import com.example.viewmodel.search.globalSearch.GlobalSearchViewModel
import com.example.viewmodel.search.countrySearch.SearchByCountryViewModel
import com.example.viewmodel.searchByActor.SearchByActorViewModel
import com.example.viewmodel.utils.dispatcher.DefaultDispatcherProvider
import com.example.viewmodel.utils.dispatcher.DispatcherProvider
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val viewModelModule = module {
    singleOf<DispatcherProvider>(::DefaultDispatcherProvider)
    viewModelOf(::SearchViewModel)
    viewModelOf(::SearchViewModel)
    viewModelOf(::SearchByCountryViewModel)
    viewModelOf(::SearchByActorViewModel)
    factoryOf(::MovieDetailsArgs)
    factoryOf(::MovieDetailsUiStateMapper)
    viewModelOf(::MovieDetailsViewModel)
    viewModelOf(::CastViewModel)
}
