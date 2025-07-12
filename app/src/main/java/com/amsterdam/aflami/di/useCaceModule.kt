package com.amsterdam.aflami.di

import com.example.domain.usecase.GetMoviesByCountryUseCase
import com.example.domain.usecase.GetSuggestedCountriesUseCase
import com.example.domain.validation.CountryValidator
import com.example.domain.validation.CountryValidatorImp
import org.koin.dsl.module

val useCaseModule = module{
    single<CountryValidator> { CountryValidatorImp() }
    single { GetSuggestedCountriesUseCase(get(), get()) }
    single { GetMoviesByCountryUseCase(get()) }
}