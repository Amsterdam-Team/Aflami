package com.amsterdam.aflami.di

import com.amsterdam.viewmodel.application.ApplicationViewModel
import com.amsterdam.viewmodel.cast.CastScreenArgs
import com.amsterdam.viewmodel.cast.CastViewModel
import com.amsterdam.viewmodel.continueWatching.ContinueWatchingUiStateMapper
import com.amsterdam.viewmodel.continueWatching.ContinueWatchingViewModel
import com.amsterdam.viewmodel.home.HomeUiStateMapper
import com.amsterdam.viewmodel.home.HomeViewModel
import com.amsterdam.viewmodel.login.LoginViewModel
import com.amsterdam.viewmodel.movieDetails.MovieDetailsArgs
import com.amsterdam.viewmodel.movieDetails.MovieDetailsUiStateMapper
import com.amsterdam.viewmodel.movieDetails.MovieDetailsViewModel
import com.amsterdam.viewmodel.register.RegisterViewModel
import com.amsterdam.viewmodel.resetPassword.ResetPasswordViewModel
import com.amsterdam.viewmodel.search.actorSearch.ActorSearchViewModel
import com.amsterdam.viewmodel.search.countrySearch.CountrySearchViewModel
import com.amsterdam.viewmodel.search.keywordSearch.SearchViewModel
import com.amsterdam.viewmodel.seriesDetails.SeriesDetailsArgs
import com.amsterdam.viewmodel.seriesDetails.SeriesDetailsStateMapper
import com.amsterdam.viewmodel.seriesDetails.SeriesDetailsViewModel
import com.amsterdam.viewmodel.topRated.TopRatedUiStateMapper
import com.amsterdam.viewmodel.topRated.TopRatedViewModel
import com.amsterdam.viewmodel.utils.dispatcher.DefaultDispatcherProvider
import com.amsterdam.viewmodel.utils.dispatcher.DispatcherProvider
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val viewModelModule = module {
    singleOf(::DefaultDispatcherProvider) { bind<DispatcherProvider>() }
    viewModelOf(::ApplicationViewModel)
    viewModelOf(::LoginViewModel)
    viewModelOf(::RegisterViewModel)
    viewModelOf(::SearchViewModel)
    viewModelOf(::CountrySearchViewModel)
    viewModelOf(::ActorSearchViewModel)
    viewModelOf(::MovieDetailsViewModel)
    viewModelOf(::CastViewModel)
    viewModelOf(::CountrySearchViewModel)
    viewModelOf(::ActorSearchViewModel)
    viewModelOf(::HomeViewModel)
    viewModelOf(::SeriesDetailsViewModel)
    viewModelOf(::TopRatedViewModel)
    viewModelOf(::RegisterViewModel)
    viewModelOf(::ResetPasswordViewModel)
    viewModelOf(::ContinueWatchingViewModel)
    factoryOf(::MovieDetailsUiStateMapper)
    // mappers
    factoryOf(::MovieDetailsUiStateMapper)
    factoryOf(::HomeUiStateMapper)
    factoryOf(::SeriesDetailsStateMapper)
    factoryOf(::TopRatedUiStateMapper)
    factoryOf(::ContinueWatchingUiStateMapper)
    // args
    factoryOf(::MovieDetailsArgs)
    factoryOf(::SeriesDetailsArgs)
    factoryOf(::CastScreenArgs)
}
