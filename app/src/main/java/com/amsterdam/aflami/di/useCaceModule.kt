package com.amsterdam.aflami.di

import com.example.domain.useCase.GetMovieCastUseCase
import com.example.domain.useCase.GetMovieCategoriesUseCase
import com.example.domain.useCase.GetMovieDetailsUseCase
import com.example.domain.useCase.GetMoviesByActorUseCase
import com.example.domain.useCase.GetMoviesByCountryUseCase
import com.example.domain.useCase.GetAndFilterMoviesByKeywordUseCase
import com.example.domain.useCase.GetSuggestedCountriesUseCase
import com.example.domain.useCase.GetAndFilterTvShowsByKeywordUseCase
import com.example.domain.useCase.GetTvShowCategoriesUseCase
import com.example.domain.useCase.search.AddRecentSearchUseCase
import com.example.domain.useCase.search.ClearAllRecentSearchesUseCase
import com.example.domain.useCase.search.ClearRecentSearchUseCase
import com.example.domain.useCase.search.GetRecentSearchesUseCase
import com.example.domain.validation.CountryValidator
import com.example.domain.validation.CountryValidatorImp
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val useCaseModule = module{
    singleOf(::GetAndFilterMoviesByKeywordUseCase)
    singleOf(::GetRecentSearchesUseCase)
    singleOf(::GetAndFilterTvShowsByKeywordUseCase)
    singleOf(::AddRecentSearchUseCase)
    singleOf(::ClearRecentSearchUseCase)
    singleOf(::ClearAllRecentSearchesUseCase)
    singleOf(::GetMovieCastUseCase)
    singleOf(::GetMovieDetailsUseCase)
    single<CountryValidator> { CountryValidatorImp() }
    single { GetSuggestedCountriesUseCase(get(), get()) }
    single { GetMoviesByCountryUseCase(get()) }
    single { GetMoviesByActorUseCase(get()) }
    single { GetMovieCategoriesUseCase(get()) }
    single { GetTvShowCategoriesUseCase(get()) }
}