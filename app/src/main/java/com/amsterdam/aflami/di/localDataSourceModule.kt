package com.amsterdam.aflami.di

import com.example.localdatasource.roomDataBase.AflamiDatabase
import com.example.localdatasource.roomDataBase.datasource.CategoryLocalDataSourceImpl
import com.example.localdatasource.roomDataBase.datasource.CountryLocalDataSourceImpl
import com.example.localdatasource.roomDataBase.datasource.MovieLocalDataSourceImpl
import com.example.localdatasource.roomDataBase.datasource.RecentSearchLocalDataSourceImpl
import com.example.localdatasource.roomDataBase.datasource.TvShowLocalDataSourceImpl
import com.example.repository.datasource.local.CategoryLocalSource
import com.example.repository.datasource.local.CountryLocalSource
import com.example.repository.datasource.local.MovieLocalSource
import com.example.repository.datasource.local.RecentSearchLocalSource
import com.example.repository.datasource.local.TvShowLocalSource
import org.koin.android.ext.koin.androidApplication
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val localDataSourceModule = module {
    // Database instance
    single { AflamiDatabase.getInstance(androidApplication()) }

    // DAOs
    single { get<AflamiDatabase>().categoryDao() }
    single { get<AflamiDatabase>().countryDao() }
    single { get<AflamiDatabase>().movieDao() }
    single { get<AflamiDatabase>().tvShowDao() }
    single { get<AflamiDatabase>().recentSearchDao() }
    single { get<AflamiDatabase>().movieCategoryInterestDao() }
    single { get<AflamiDatabase>().tvShowCategoryInterestDao()}

// Local sources using singleOf with interface binding
    singleOf(::CategoryLocalDataSourceImpl) bind CategoryLocalSource::class
    singleOf(::CountryLocalDataSourceImpl) bind CountryLocalSource::class
    singleOf(::MovieLocalDataSourceImpl) bind MovieLocalSource::class
    singleOf(::TvShowLocalDataSourceImpl) bind TvShowLocalSource::class
    singleOf(::RecentSearchLocalDataSourceImpl) bind RecentSearchLocalSource::class
}