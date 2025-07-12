package com.example.localdatasource.di

import com.example.localdatasource.roomDataBase.AflamiDatabase
import com.example.localdatasource.roomDataBase.daos.CategoryDao
import com.example.localdatasource.roomDataBase.daos.CountryDao
import com.example.localdatasource.roomDataBase.daos.MovieDao
import com.example.localdatasource.roomDataBase.daos.RecentSearchDao
import com.example.localdatasource.roomDataBase.daos.TvShowDao
import com.example.localdatasource.roomDataBase.datasource.LocalCategoryDataSourceImpl
import com.example.localdatasource.roomDataBase.datasource.LocalCountryDataSourceImpl
import com.example.localdatasource.roomDataBase.datasource.LocalMovieDataSourceImpl
import com.example.localdatasource.roomDataBase.datasource.RecentSearchDataSourceImpl
import com.example.repository.datasource.local.LocalCategoryDataSource
import com.example.repository.datasource.local.LocalCountryDataSource
import com.example.repository.datasource.local.LocalMovieDataSource
import com.example.repository.datasource.local.LocalRecentSearchDataSource
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val localDataSourceModule = module {
    single { AflamiDatabase.getInstance(androidApplication()) }
    single<RecentSearchDao> { get<AflamiDatabase>().recentSearchDao() }
    single<CountryDao> { get<AflamiDatabase>().countryDao() }
    single<CategoryDao> { get<AflamiDatabase>().categoryDao() }
    single<MovieDao> { get<AflamiDatabase>().movieDao() }
    single<TvShowDao> { get<AflamiDatabase>().tvShowDao() }
    single<RecentSearchDao> { get<AflamiDatabase>().recentSearchDao() }

    single<LocalCountryDataSource> { LocalCountryDataSourceImpl(get()) }
    single<LocalMovieDataSource> { LocalMovieDataSourceImpl(get()) }
    single<LocalRecentSearchDataSource> { RecentSearchDataSourceImpl(get()) }
    single<LocalCategoryDataSource> { LocalCategoryDataSourceImpl(get()) }
}