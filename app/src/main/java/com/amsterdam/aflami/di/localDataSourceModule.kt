package com.amsterdam.aflami.di

import androidx.datastore.core.DataStore
import androidx.datastore.dataStoreFile
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import com.example.localdatasource.dataStore.datasource.AppPreferences
import com.example.localdatasource.dataStore.appPreferences.AppDataStorePreferences
import com.example.localdatasource.dataStore.datasource.AuthenticationLocalDataSourceImpl
import com.example.localdatasource.roomDataBase.AflamiDatabase
import com.example.localdatasource.roomDataBase.datasource.CategoryLocalDataSourceImpl
import com.example.localdatasource.roomDataBase.datasource.CountryLocalDataSourceImpl
import com.example.localdatasource.roomDataBase.datasource.MovieLocalDataSourceImpl
import com.example.localdatasource.roomDataBase.datasource.RecentSearchLocalDataSourceImpl
import com.example.localdatasource.roomDataBase.datasource.TvShowLocalDataSourceImpl
import com.example.localdatasource.roomDataBase.datasource.WatchHistoryLocalDataSourceImpl
import com.example.repository.datasource.local.AuthenticationLocalSource
import com.example.repository.datasource.local.CategoryLocalSource
import com.example.repository.datasource.local.CountryLocalSource
import com.example.repository.datasource.local.MovieLocalSource
import com.example.repository.datasource.local.RecentSearchLocalSource
import com.example.repository.datasource.local.TvShowLocalSource
import com.example.repository.datasource.local.WatchHistoryLocalDataSource
import org.koin.android.ext.koin.androidApplication
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val localDataSourceModule = module {
    // App Preferences
    single<DataStore<Preferences>> {
        PreferenceDataStoreFactory.create(
            produceFile = { androidApplication().dataStoreFile("app.preferences_pb") }
        )
    }

    singleOf(::AppDataStorePreferences) bind AppPreferences::class

    // Database instance
    single { AflamiDatabase.getInstance(androidApplication()) }

    // DAOs
    single { get<AflamiDatabase>().categoryDao() }
    single { get<AflamiDatabase>().countryDao() }
    single { get<AflamiDatabase>().movieDao() }
    single { get<AflamiDatabase>().tvShowDao() }
    single { get<AflamiDatabase>().watchHistoryDao()}
    single { get<AflamiDatabase>().recentSearchDao() }
    single { get<AflamiDatabase>().movieCategoryInterestDao() }
    single { get<AflamiDatabase>().tvShowCategoryInterestDao()}

// Local sources using singleOf with interface binding
    singleOf(::AuthenticationLocalDataSourceImpl) bind AuthenticationLocalSource::class
    singleOf(::CategoryLocalDataSourceImpl) bind CategoryLocalSource::class
    singleOf(::CountryLocalDataSourceImpl) bind CountryLocalSource::class
    singleOf(::MovieLocalDataSourceImpl) bind MovieLocalSource::class
    singleOf(::TvShowLocalDataSourceImpl) bind TvShowLocalSource::class
    singleOf(::RecentSearchLocalDataSourceImpl) bind RecentSearchLocalSource::class
    singleOf(::WatchHistoryLocalDataSourceImpl) bind WatchHistoryLocalDataSource::class
}