package com.amsterdam.aflami.di

import com.example.localdatasource.roomDataBase.AflamiDatabase
import com.example.localdatasource.roomDataBase.daos.CategoryDao
import com.example.localdatasource.roomDataBase.daos.CountryDao
import com.example.localdatasource.roomDataBase.daos.MovieDao
import com.example.localdatasource.roomDataBase.daos.RecentSearchDao
import com.example.localdatasource.roomDataBase.daos.TvShowDao
import com.example.localdatasource.roomDataBase.datasource.CategoryLocalSourceImpl
import com.example.localdatasource.roomDataBase.datasource.CountryLocalSourceImpl
import com.example.localdatasource.roomDataBase.datasource.MovieLocalSourceImpl
import com.example.localdatasource.roomDataBase.datasource.TvShowLocalSourceImpl
import com.example.localdatasource.roomDataBase.datasource.RecentSearchLocalSourceImpl
import com.example.repository.datasource.local.CategoryLocalSource
import com.example.repository.datasource.local.CountryLocalSource
import com.example.repository.datasource.local.MovieLocalSource
import com.example.repository.datasource.local.RecentSearchLocalSource
import com.example.repository.datasource.local.TvShowLocalSource
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

    single<CountryLocalSource> { CountryLocalSourceImpl(get()) }
    single<MovieLocalSource> { MovieLocalSourceImpl(get()) }
    single<RecentSearchLocalSource> { RecentSearchLocalSourceImpl(get()) }
    single<CategoryLocalSource> { CategoryLocalSourceImpl(get()) }
    single<TvShowLocalSource> { TvShowLocalSourceImpl(get()) }
}