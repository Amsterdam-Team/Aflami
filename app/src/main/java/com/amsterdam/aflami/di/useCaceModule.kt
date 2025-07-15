package com.amsterdam.aflami.di

import com.example.domain.useCase.GetMovieCategoriesUseCase
import com.example.domain.useCase.GetMoviesByActorUseCase
import com.example.domain.useCase.GetMoviesByCountryUseCase
import com.example.domain.useCase.GetMoviesByKeywordUseCase
import com.example.domain.useCase.GetMoviesPagingByKeywordUseCase
import com.example.domain.useCase.GetSuggestedCountriesUseCase
import com.example.domain.useCase.GetTvShowByKeywordUseCase
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
    singleOf(::GetMoviesByKeywordUseCase)
    singleOf(::GetTvShowByKeywordUseCase)
    singleOf(::GetRecentSearchesUseCase)
    singleOf(::ClearRecentSearchUseCase)
    singleOf(::ClearAllRecentSearchesUseCase)
    singleOf(::GetMoviesByKeywordUseCase)
    singleOf(::GetTvShowByKeywordUseCase)
    singleOf(::GetRecentSearchesUseCase)
    singleOf(::AddRecentSearchUseCase)
    singleOf(::ClearRecentSearchUseCase)
    singleOf(::ClearAllRecentSearchesUseCase)
    single<CountryValidator> { CountryValidatorImp() }
    single { GetSuggestedCountriesUseCase(get(), get()) }
    single { GetMoviesByCountryUseCase(get()) }
    single { GetMoviesByActorUseCase(get()) }
    single { GetMovieCategoriesUseCase(get()) }
    single { GetTvShowCategoriesUseCase(get()) }
    single { GetMoviesPagingByKeywordUseCase(get()) }
}