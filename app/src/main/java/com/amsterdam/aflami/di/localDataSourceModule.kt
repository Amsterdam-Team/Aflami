package com.amsterdam.aflami.di

import android.app.Application
import androidx.datastore.core.DataStore
import androidx.datastore.dataStoreFile
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import com.amsterdam.localdatasource.dataStore.AppPreferencesImpl
import com.amsterdam.localdatasource.dataStore.AuthenticationLocalDataSourceImpl
import com.amsterdam.localdatasource.roomDataBase.AflamiDatabase
import com.amsterdam.localdatasource.roomDataBase.datasource.CategoryLocalDataSourceImpl
import com.amsterdam.localdatasource.roomDataBase.datasource.CountryLocalDataSourceImpl
import com.amsterdam.localdatasource.roomDataBase.datasource.MovieLocalDataSourceImpl
import com.amsterdam.localdatasource.roomDataBase.datasource.RecentSearchLocalDataSourceImpl
import com.amsterdam.localdatasource.roomDataBase.datasource.TvShowLocalDataSourceImpl
import com.amsterdam.localdatasource.roomDataBase.datasource.WatchHistoryLocalDataSourceImpl
import com.amsterdam.repository.datasource.local.AppPreferences
import com.amsterdam.repository.datasource.local.AuthenticationLocalSource
import com.amsterdam.repository.datasource.local.CategoryLocalSource
import com.amsterdam.repository.datasource.local.CountryLocalSource
import com.amsterdam.repository.datasource.local.MovieLocalSource
import com.amsterdam.repository.datasource.local.RecentSearchLocalSource
import com.amsterdam.repository.datasource.local.TvShowLocalSource
import com.amsterdam.repository.datasource.local.WatchHistoryLocalDataSource
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LocalDataSourceProviderModule {
    @Provides
    @Singleton
    fun provideDataStore(application: Application): DataStore<Preferences> {
        return PreferenceDataStoreFactory.create {
            application.dataStoreFile("application.preferences_pb")
        }
    }

    @Provides
    @Singleton
    fun provideDatabase(application: Application): AflamiDatabase {
        return AflamiDatabase.getInstance(application)
    }

    @Provides
    @Singleton
    fun provideCountryDao(aflamiDatabase: AflamiDatabase) = aflamiDatabase.countryDao()

    @Provides
    @Singleton
    fun provideCategoryDao(aflamiDatabase: AflamiDatabase) = aflamiDatabase.categoryDao()

    @Provides
    @Singleton
    fun provideMovieDao(aflamiDatabase: AflamiDatabase) = aflamiDatabase.movieDao()

    @Provides
    @Singleton
    fun provideTvShowDao(aflamiDatabase: AflamiDatabase) = aflamiDatabase.tvShowDao()

    @Provides
    @Singleton
    fun provideWatchHistoryDao(aflamiDatabase: AflamiDatabase) = aflamiDatabase.watchHistoryDao()

    @Provides
    @Singleton
    fun provideRecentSearchDao(aflamiDatabase: AflamiDatabase) = aflamiDatabase.recentSearchDao()

    @Provides
    @Singleton
    fun provideMovieCategoryInterestDao(aflamiDatabase: AflamiDatabase) = aflamiDatabase.movieCategoryInterestDao()

    @Provides
    @Singleton
    fun provideTvShowCategoryInterestDao(aflamiDatabase: AflamiDatabase) = aflamiDatabase.tvShowCategoryInterestDao()
}

@Module
@InstallIn(SingletonComponent::class)
abstract class LocalDataSourceBindsModule {
    @Binds
    @Singleton
    abstract fun bindAuthenticationLocalDataSource(
        authenticationLocalDataSourceImpl: AuthenticationLocalDataSourceImpl
    ): AuthenticationLocalSource

    @Binds
    @Singleton
    abstract fun bindAppPreferences(
        appPreferencesImpl: AppPreferencesImpl
    ): AppPreferences

    @Binds
    @Singleton
    abstract fun bindCountryLocalDataSource(
        countryLocalDataSourceImpl: CountryLocalDataSourceImpl
    ): CountryLocalSource

    @Binds
    @Singleton
    abstract fun bindCategoryLocalSource(
        impl: CategoryLocalDataSourceImpl
    ): CategoryLocalSource

    @Binds
    @Singleton
    abstract fun bindMovieLocalDataSource(
        movieLocalDataSourceImpl: MovieLocalDataSourceImpl
    ): MovieLocalSource

    @Binds
    @Singleton
    abstract fun bindTvShowLocalDataSource(
        tvShowLocalDataSourceImpl: TvShowLocalDataSourceImpl
    ): TvShowLocalSource

    @Binds
    @Singleton
    abstract fun bindRecentSearchLocalDataSource(
        recentSearchLocalDataSourceImpl: RecentSearchLocalDataSourceImpl
    ): RecentSearchLocalSource

    @Binds
    @Singleton
    abstract fun bindWatchHistoryLocalDataSource(
        watchHistoryLocalDataSourceImpl: WatchHistoryLocalDataSourceImpl
    ): WatchHistoryLocalDataSource
}
