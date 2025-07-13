package com.amsterdam.aflami.di

import com.amsterdam.domain.repository.CategoryRepository
import com.amsterdam.domain.repository.CountryRepository
import com.amsterdam.domain.repository.MovieRepository
import com.amsterdam.domain.repository.RecentSearchRepository
import com.amsterdam.domain.repository.TvShowRepository
import com.amsterdam.repository.mapper.local.CategoryLocalMapper
import com.amsterdam.repository.mapper.local.CountryLocalMapper
import com.amsterdam.repository.mapper.local.MovieLocalMapper
import com.amsterdam.repository.mapper.local.RecentSearchMapper
import com.amsterdam.repository.mapper.local.TvShowLocalMapper
import com.amsterdam.repository.mapper.remote.RemoteCountryMapper
import com.amsterdam.repository.mapper.remote.RemoteMovieMapper
import com.amsterdam.repository.mapper.remote.RemoteTvShowMapper
import com.amsterdam.repository.repository.CategoryRepositoryImpl
import com.amsterdam.repository.repository.CountryRepositoryImpl
import com.amsterdam.repository.repository.MovieRepositoryImpl
import com.amsterdam.repository.repository.RecentSearchRepositoryImpl
import com.amsterdam.repository.repository.TvShowRepositoryImpl
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