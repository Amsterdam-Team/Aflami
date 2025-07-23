package com.amsterdam.aflami.di

import com.example.domain.useCase.GetAndFilterMoviesByKeywordUseCase
import com.example.domain.useCase.GetAndFilterTvShowsByKeywordUseCase
import com.example.domain.useCase.GetHomeScreenDataUseCase
import com.example.domain.useCase.GetMovieCastUseCase
import com.example.domain.useCase.GetMovieDetailsUseCase
import com.example.domain.useCase.GetMoviesByActorUseCase
import com.example.domain.useCase.GetMoviesByCountryUseCase
import com.example.domain.useCase.GetPopularMoviesUseCase
import com.example.domain.useCase.GetSuggestedCountriesUseCase
import com.example.domain.useCase.GetUpcomingMoviesUseCase
import com.example.domain.useCase.GetTopRatedMoviesUseCase
import com.example.domain.useCase.RecentSearchesUseCase
import com.example.domain.useCase.GetTvShowDetailsUseCase
import com.example.domain.useCase.GetEpisodesBySeasonNumberUseCase
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val useCaseModule = module {
    singleOf(::GetAndFilterMoviesByKeywordUseCase)
    singleOf(::GetMoviesByCountryUseCase)
    singleOf(::GetMovieCastUseCase)
    singleOf(::GetMovieDetailsUseCase)
    singleOf(::GetMoviesByActorUseCase)
    singleOf(::GetSuggestedCountriesUseCase)
    singleOf(::RecentSearchesUseCase)
    singleOf(::GetAndFilterTvShowsByKeywordUseCase)
    singleOf(::GetPopularMoviesUseCase)
    singleOf(::GetEpisodesBySeasonNumberUseCase)
    singleOf(::GetTvShowDetailsUseCase)
    singleOf(::GetUpcomingMoviesUseCase)
    singleOf(::GetTopRatedMoviesUseCase)
    singleOf(::GetHomeScreenDataUseCase)
}