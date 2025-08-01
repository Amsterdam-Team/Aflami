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
    fun provideDataStore(app: Application): DataStore<Preferences> {
        return PreferenceDataStoreFactory.create {
            app.dataStoreFile("app.preferences_pb")
        }
    }

    @Provides
    @Singleton
    fun provideDatabase(app: Application): AflamiDatabase {
        return AflamiDatabase.getInstance(app)
    }

    @Provides
    @Singleton
    fun provideCategoryDao(db: AflamiDatabase) = db.categoryDao()

    @Provides
    @Singleton
    fun provideCountryDao(db: AflamiDatabase) = db.countryDao()

    @Provides
    @Singleton
    fun provideMovieDao(db: AflamiDatabase) = db.movieDao()

    @Provides
    @Singleton
    fun provideTvShowDao(db: AflamiDatabase) = db.tvShowDao()

    @Provides
    @Singleton
    fun provideWatchHistoryDao(db: AflamiDatabase) = db.watchHistoryDao()

    @Provides
    @Singleton
    fun provideRecentSearchDao(db: AflamiDatabase) = db.recentSearchDao()

    @Provides
    @Singleton
    fun provideMovieCategoryInterestDao(db: AflamiDatabase) = db.movieCategoryInterestDao()

    @Provides
    @Singleton
    fun provideTvShowCategoryInterestDao(db: AflamiDatabase) = db.tvShowCategoryInterestDao()
}

@Module
@InstallIn(SingletonComponent::class)
abstract class LocalDataSourceBindsModule {

    @Binds
    @Singleton
    abstract fun bindAuthenticationLocalDataSource(
        impl: AuthenticationLocalDataSourceImpl
    ): AuthenticationLocalSource


    @Binds
    @Singleton
    abstract fun bindAppPreferences(
        impl: AppPreferencesImpl
    ): AppPreferences

    @Binds
    @Singleton
    abstract fun bindCategoryLocalDataSource(
        impl: CategoryLocalDataSourceImpl
    ): CategoryLocalSource

    @Binds
    @Singleton
    abstract fun bindCountryLocalDataSource(
        impl: CountryLocalDataSourceImpl
    ): CountryLocalSource

    @Binds
    @Singleton
    abstract fun bindMovieLocalDataSource(
        impl: MovieLocalDataSourceImpl
    ): MovieLocalSource

    @Binds
    @Singleton
    abstract fun bindTvShowLocalDataSource(
        impl: TvShowLocalDataSourceImpl
    ): TvShowLocalSource

    @Binds
    @Singleton
    abstract fun bindRecentSearchLocalDataSource(
        impl: RecentSearchLocalDataSourceImpl
    ): RecentSearchLocalSource

    @Binds
    @Singleton
    abstract fun bindWatchHistoryLocalDataSource(
        impl: WatchHistoryLocalDataSourceImpl
    ): WatchHistoryLocalDataSource

}
