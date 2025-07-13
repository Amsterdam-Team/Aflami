package com.amsterdam.aflami.di

import com.amsterdam.localdatasource.roomDataBase.AflamiDatabase
import com.amsterdam.localdatasource.roomDataBase.daos.CategoryDao
import com.amsterdam.localdatasource.roomDataBase.daos.CountryDao
import com.amsterdam.localdatasource.roomDataBase.daos.MovieDao
import com.amsterdam.localdatasource.roomDataBase.daos.RecentSearchDao
import com.amsterdam.localdatasource.roomDataBase.daos.TvShowDao
import com.amsterdam.localdatasource.roomDataBase.datasource.LocalCategoryDataSourceImpl
import com.amsterdam.localdatasource.roomDataBase.datasource.LocalCountryDataSourceImpl
import com.amsterdam.localdatasource.roomDataBase.datasource.LocalMovieDataSourceImpl
import com.amsterdam.localdatasource.roomDataBase.datasource.LocalTvShowDataSourceImpl
import com.amsterdam.localdatasource.roomDataBase.datasource.RecentSearchDataSourceImpl
import com.amsterdam.repository.datasource.local.LocalCategoryDataSource
import com.amsterdam.repository.datasource.local.LocalCountryDataSource
import com.amsterdam.repository.datasource.local.LocalMovieDataSource
import com.amsterdam.repository.datasource.local.LocalRecentSearchDataSource
import com.amsterdam.repository.datasource.local.LocalTvShowDataSource
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
    single<LocalTvShowDataSource> { LocalTvShowDataSourceImpl(get()) }
}