package com.amsterdam.aflami.di

import com.example.domain.repository.CategoryRepository
import com.example.domain.repository.CountryRepository
import com.example.domain.repository.MovieRepository
import com.example.domain.repository.RecentSearchRepository
import com.example.domain.repository.TvShowRepository
import com.example.repository.repository.CategoryRepositoryImpl
import com.example.repository.repository.CountryRepositoryImpl
import com.example.repository.repository.MovieRepositoryImpl
import com.example.repository.repository.RecentSearchRepositoryImpl
import com.example.repository.repository.TvShowRepositoryImpl
import com.example.repository.utils.RecentSearchHandler
import com.example.repository.utils.RecentSearchHandlerImpl
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val repositoryModule = module {
    singleOf(::RecentSearchHandlerImpl) bind RecentSearchHandler::class

    singleOf(::CountryRepositoryImpl) bind CountryRepository::class
    singleOf(::CategoryRepositoryImpl) bind CategoryRepository::class
    singleOf(::MovieRepositoryImpl) bind MovieRepository::class
    singleOf(::RecentSearchRepositoryImpl) bind RecentSearchRepository::class
    singleOf(::TvShowRepositoryImpl) bind TvShowRepository::class
}