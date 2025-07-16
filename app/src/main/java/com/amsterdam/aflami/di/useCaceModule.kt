package com.amsterdam.aflami.di

import com.example.domain.useCase.GetAndFilterMoviesByKeywordUseCase
import com.example.domain.useCase.GetAndFilterTvShowsByKeywordUseCase
import com.example.domain.useCase.GetMovieCategoriesUseCase
import com.example.domain.useCase.GetMoviesByActorUseCase
import com.example.domain.useCase.GetMoviesByCountryUseCase
import com.example.domain.useCase.GetSuggestedCountriesUseCase
import com.example.domain.useCase.GetTvShowCategoriesUseCase
import com.example.domain.useCase.search.AddRecentSearchUseCase
import com.example.domain.useCase.search.ClearAllRecentSearchesUseCase
import com.example.domain.useCase.search.ClearRecentSearchUseCase
import com.example.domain.useCase.search.GetRecentSearchesUseCase
import com.example.domain.validation.CountryValidator
import com.example.domain.validation.CountryValidatorImp
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val useCaseModule = module {
    singleOf(::CountryValidatorImp) { bind<CountryValidator>() }
    singleOf(::GetMoviesByKeywordUseCase)
    singleOf(::CountryValidatorImp)
    singleOf(::GetAndFilterMoviesByKeywordUseCase)
    singleOf(::GetMoviesByCountryUseCase)
    singleOf(::GetMoviesByActorUseCase)
    singleOf(::GetMovieCategoriesUseCase)
    singleOf(::GetTvShowCategoriesUseCase)
    singleOf(::GetSuggestedCountriesUseCase)
    singleOf(::GetRecentSearchesUseCase)
    singleOf(::GetAndFilterTvShowsByKeywordUseCase)
    singleOf(::AddRecentSearchUseCase)
    singleOf(::ClearRecentSearchUseCase)
    singleOf(::ClearAllRecentSearchesUseCase)
}