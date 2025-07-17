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
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val localDataSourceModule = module {
    single { AflamiDatabase.getInstance(androidApplication()) }
    single { get<AflamiDatabase>().categoryDao() }
    single { get<AflamiDatabase>().countryDao() }
    single { get<AflamiDatabase>().movieDao() }
    single { get<AflamiDatabase>().tvShowDao() }
    single { get<AflamiDatabase>().recentSearchDao() }
    singleOf(::CategoryLocalDataSourceImpl) { bind<CategoryLocalSource>() }
    singleOf(::CountryLocalDataSourceImpl) { bind<CountryLocalSource>() }
    singleOf(::MovieLocalDataSourceImpl) { bind<MovieLocalSource>() }
    singleOf(::TvShowLocalDataSourceImpl) { bind<TvShowLocalSource>() }
    singleOf(::RecentSearchLocalDataSourceImpl) { bind<RecentSearchLocalSource>() }
}
