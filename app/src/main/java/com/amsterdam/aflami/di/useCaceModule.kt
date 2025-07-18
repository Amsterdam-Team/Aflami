package com.amsterdam.aflami.di

import com.example.domain.useCase.*
import com.example.domain.useCase.search.*
import com.example.domain.validation.CountryValidator
import com.example.domain.validation.CountryValidatorImp
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val useCaseModule = module {
    // Validators
    singleOf(::CountryValidatorImp) { bind<CountryValidator>() }

    // Use Cases
    singleOf(::GetAndFilterMoviesByKeywordUseCase)
    singleOf(::GetAndFilterTvShowsByKeywordUseCase)
    singleOf(::GetMovieCastUseCase)
    singleOf(::GetMovieDetailsUseCase)
    singleOf(::GetMoviesByActorUseCase)
    singleOf(::GetMoviesByCountryUseCase)
    singleOf(::GetMovieCategoriesUseCase)
    singleOf(::GetTvShowCategoriesUseCase)
    singleOf(::GetSuggestedCountriesUseCase)
    singleOf(::GetEpisodesBySeasonNumberUseCase)
    singleOf(::GetTvShowDetailsUseCase)

    // Recent Search Use Cases
    singleOf(::GetRecentSearchesUseCase)
    singleOf(::AddRecentSearchUseCase)
    singleOf(::ClearRecentSearchUseCase)
    singleOf(::ClearAllRecentSearchesUseCase)
}
