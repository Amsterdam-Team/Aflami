package com.mohamed.repositorfake.di

import com.example.domain.repository.CategoryRepository
import com.example.domain.repository.CountryRepository
import com.example.domain.repository.MovieRepository
import com.example.domain.repository.RecentSearchRepository
import com.example.domain.repository.TvShowRepository
import com.mohamed.repositorfake.repository.CategoryRepositoryImpl
import com.mohamed.repositorfake.repository.CountryRepositoryImpl
import com.mohamed.repositorfake.repository.MovieRepositoryImpl
import com.mohamed.repositorfake.repository.RecentSearchRepositoryImpl
import com.mohamed.repositorfake.repository.TvShowRepositoryImpl
import org.koin.dsl.module

val repositoryModule = module {
    single<CountryRepository> { CountryRepositoryImpl() }
    single<MovieRepository> { MovieRepositoryImpl() }
    single<CategoryRepository> { CategoryRepositoryImpl() }
    single<RecentSearchRepository> { RecentSearchRepositoryImpl() }
    single<TvShowRepository> { TvShowRepositoryImpl() }
}