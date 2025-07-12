package com.amsterdam.aflami.di

import com.example.domain.repository.CountryRepository
import com.example.domain.repository.MovieRepository
import com.example.repository.mapper.local.CategoryLocalMapper
import com.example.repository.mapper.local.CountryLocalMapper
import com.example.repository.mapper.local.MovieLocalMapper
import com.example.repository.mapper.remote.RemoteCountryMapper
import com.example.repository.mapper.remote.RemoteMovieMapper
import com.example.repository.repository.CountryRepositoryImpl
import com.example.repository.repository.MovieRepositoryImpl
import org.koin.dsl.module

val repositoryModule = module {
    single { CountryLocalMapper() }
    single { RemoteCountryMapper() }
    single { CategoryLocalMapper() }
    single { ::MovieLocalMapper }
    single { RemoteMovieMapper() }
    single<CountryRepository> { CountryRepositoryImpl(get(), get(), get(), get()) }
    single<MovieRepository> { MovieRepositoryImpl(get(), get(), get(), get(), get()) }
}