package com.amsterdam.aflami.di

import com.example.domain.repository.CategoryRepository
import com.example.domain.repository.CountryRepository
import com.example.domain.repository.MovieRepository
import com.example.domain.repository.RecentSearchRepository
import com.example.domain.repository.TvShowRepository
import com.example.repository.mapper.local.CategoryLocalMapper
import com.example.repository.mapper.local.CountryLocalMapper
import com.example.repository.mapper.local.MovieLocalMapper
import com.example.repository.mapper.local.RecentSearchMapper
import com.example.repository.mapper.local.TvShowLocalMapper
import com.example.repository.mapper.remote.RemoteCountryMapper
import com.example.repository.mapper.remote.RemoteMovieMapper
import com.example.repository.mapper.remote.RemoteTvShowMapper
import com.example.repository.repository.CategoryRepositoryImpl
import com.example.repository.repository.CountryRepositoryImpl
import com.example.repository.repository.MovieRepositoryImpl
import com.example.repository.repository.RecentSearchRepositoryImpl
import com.example.repository.repository.TvShowRepositoryImpl
import org.koin.dsl.module

val repositoryModule = module {
    single { CountryLocalMapper() }
    single { RemoteCountryMapper() }
    single { CategoryLocalMapper() }
    single { MovieLocalMapper(get()) }
    single { TvShowLocalMapper(get()) }
    single { RemoteMovieMapper() }
    single { RemoteTvShowMapper() }
    single { RecentSearchMapper() }
    single<CountryRepository> { CountryRepositoryImpl(get(), get(), get(), get()) }
    single<MovieRepository> { MovieRepositoryImpl(get(), get(), get(), get(), get()) }
    single<CategoryRepository> { CategoryRepositoryImpl(get(), get(), get()) }
    single<RecentSearchRepository> { RecentSearchRepositoryImpl(get(), get()) }
    single<TvShowRepository> { TvShowRepositoryImpl(get(), get(), get(), get(), get()) }
}