package com.amsterdam.aflami.di

import com.example.localdatasource.roomDataBase.AflamiDatabase
import com.example.localdatasource.roomDataBase.datasource.CategoryLocalSourceImpl
import com.example.localdatasource.roomDataBase.datasource.CountryLocalSourceImpl
import com.example.localdatasource.roomDataBase.datasource.MovieLocalSourceImpl
import com.example.localdatasource.roomDataBase.datasource.RecentSearchLocalSourceImpl
import com.example.localdatasource.roomDataBase.datasource.TvShowLocalSourceImpl
import com.example.repository.datasource.local.CategoryLocalSource
import com.example.repository.datasource.local.CountryLocalSource
import com.example.repository.datasource.local.MovieLocalSource
import com.example.repository.datasource.local.RecentSearchLocalSource
import com.example.repository.datasource.local.TvShowLocalSource
import org.koin.android.ext.koin.androidApplication
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import org.koin.dsl.bind

val localDataSourceModule = module {
    // Database instance
    single { AflamiDatabase.getInstance(androidApplication()) }

    // DAOs
    single { get<AflamiDatabase>().categoryDao() }
    single { get<AflamiDatabase>().countryDao() }
    single { get<AflamiDatabase>().movieDao() }
    single { get<AflamiDatabase>().tvShowDao() }
    single { get<AflamiDatabase>().recentSearchDao() }

    // Local sources using singleOf with interface binding
    singleOf(::CategoryLocalSourceImpl) bind CategoryLocalSource::class
    singleOf(::CountryLocalSourceImpl) bind CountryLocalSource::class
    singleOf(::MovieLocalSourceImpl) bind MovieLocalSource::class
    singleOf(::TvShowLocalSourceImpl) bind TvShowLocalSource::class
    singleOf(::RecentSearchLocalSourceImpl) bind RecentSearchLocalSource::class
}