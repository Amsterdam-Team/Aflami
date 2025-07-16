package com.amsterdam.aflami.di

import com.example.localdatasource.roomDataBase.AflamiDatabase
import com.example.localdatasource.roomDataBase.datasource.CategoryLocalSourceImpl
import com.example.localdatasource.roomDataBase.datasource.CountryLocalSourceImpl
import com.example.localdatasource.roomDataBase.datasource.MovieLocalSourceImpl
import com.example.localdatasource.roomDataBase.datasource.RecentSearchLocalSourceImpl
import com.example.localdatasource.roomDataBase.datasource.TvShowLocalSourceImpl
import org.koin.android.ext.koin.androidApplication
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val localDataSourceModule = module {
    single { AflamiDatabase.getInstance(androidApplication()) }
    single { get<AflamiDatabase>().categoryDao() }
    single { get<AflamiDatabase>().countryDao() }
    single { get<AflamiDatabase>().movieDao() }
    single { get<AflamiDatabase>().tvShowDao() }
    single { get<AflamiDatabase>().recentSearchDao() }

    singleOf(::CategoryLocalSourceImpl)
    singleOf(::CountryLocalSourceImpl)
    singleOf(::MovieLocalSourceImpl)
    singleOf(::TvShowLocalSourceImpl)
    singleOf(::RecentSearchLocalSourceImpl)
}
