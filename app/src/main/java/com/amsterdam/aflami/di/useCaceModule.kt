package com.amsterdam.aflami.di

import com.amsterdam.domain.useCase.GetMovieCategoriesUseCase
import com.amsterdam.domain.useCase.GetMoviesByActorUseCase
import com.amsterdam.domain.useCase.GetMoviesByCountryUseCase
import com.amsterdam.domain.useCase.GetMoviesByKeywordUseCase
import com.amsterdam.domain.useCase.GetSuggestedCountriesUseCase
import com.amsterdam.domain.useCase.GetTvShowByKeywordUseCase
import com.amsterdam.domain.useCase.GetTvShowCategoriesUseCase
import com.amsterdam.domain.useCase.search.AddRecentSearchUseCase
import com.amsterdam.domain.useCase.search.ClearAllRecentSearchesUseCase
import com.amsterdam.domain.useCase.search.ClearRecentSearchUseCase
import com.amsterdam.domain.useCase.search.GetRecentSearchesUseCase
import com.amsterdam.domain.validation.CountryValidator
import com.amsterdam.domain.validation.CountryValidatorImp
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
}