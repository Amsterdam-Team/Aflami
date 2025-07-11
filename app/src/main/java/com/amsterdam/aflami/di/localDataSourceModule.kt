package com.amsterdam.aflami.di

import com.example.localdatasource.roomDataBase.AflamiDatabase
import com.example.localdatasource.roomDataBase.daos.CategoryDao
import com.example.localdatasource.roomDataBase.daos.CountryDao
import com.example.localdatasource.roomDataBase.daos.MovieDao
import com.example.localdatasource.roomDataBase.daos.RecentSearchDao
import com.example.localdatasource.roomDataBase.daos.TvShowDao
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val localDataSourceModule = module {
    single { AflamiDatabase.getInstance(androidApplication()) }
    single<RecentSearchDao> { get<AflamiDatabase>().recentSearchDao() }
    single<CountryDao> { get<AflamiDatabase>().countryDao() }
    single<CategoryDao> { get<AflamiDatabase>().categoryDao() }
    single<MovieDao> { get<AflamiDatabase>().movieDao() }
    single<TvShowDao> { get<AflamiDatabase>().tvShowDao() }
}