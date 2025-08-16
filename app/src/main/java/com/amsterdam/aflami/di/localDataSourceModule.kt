package com.amsterdam.aflami.di

import android.app.Application
import androidx.datastore.core.DataStore
import androidx.datastore.dataStoreFile
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import com.amsterdam.localdatasource.dataStore.AppLocalPreferencesDataStore
import com.amsterdam.localdatasource.dataStore.AuthenticationLocalDataSourceImpl
import com.amsterdam.localdatasource.inMemory.GameInMemoryDataSource
import com.amsterdam.localdatasource.roomDataBase.AflamiDatabase
import com.amsterdam.localdatasource.roomDataBase.datasource.CategoryLocalDataSourceImpl
import com.amsterdam.localdatasource.roomDataBase.datasource.CountryLocalDataSourceImpl
import com.amsterdam.localdatasource.roomDataBase.datasource.GameLocalDataSourceImpl
import com.amsterdam.localdatasource.roomDataBase.datasource.MovieLocalDataSourceImpl
import com.amsterdam.localdatasource.roomDataBase.datasource.ProfileLocalDataSourceImpl
import com.amsterdam.localdatasource.roomDataBase.datasource.RecentSearchLocalDataSourceImpl
import com.amsterdam.localdatasource.roomDataBase.datasource.TvShowLocalDataSourceImpl
import com.amsterdam.localdatasource.roomDataBase.datasource.WatchHistoryLocalDataSourceImpl
import com.amsterdam.repository.datasource.local.AppLocalPreferences
import com.amsterdam.repository.datasource.local.AuthenticationLocalDataSource
import com.amsterdam.repository.datasource.local.CategoryLocalDataSource
import com.amsterdam.repository.datasource.local.CountryLocalDataSource
import com.amsterdam.repository.datasource.local.GameLocalDataSource
import com.amsterdam.repository.datasource.local.GameSessionLocalDataSource
import com.amsterdam.repository.datasource.local.MovieLocalDataSource
import com.amsterdam.repository.datasource.local.ProfileLocalDataSource
import com.amsterdam.repository.datasource.local.RecentSearchLocalDataSource
import com.amsterdam.repository.datasource.local.TvShowLocalDataSource
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

    @Provides
    @Singleton
    fun provideAccountDetailsDao(aflamiDatabase: AflamiDatabase) = aflamiDatabase.accountDetailsDao()

    @Provides
    @Singleton
    fun provideGamePointsDao(aflamiDatabase: AflamiDatabase) = aflamiDatabase.gamePointsDao()
}

@Module
@InstallIn(SingletonComponent::class)
abstract class LocalDataSourceBindsModule {
    @Binds
    @Singleton
    abstract fun bindAuthenticationLocalDataSource(
        authenticationLocalDataSourceImpl: AuthenticationLocalDataSourceImpl
    ): AuthenticationLocalDataSource

    @Binds
    @Singleton
    abstract fun bindAppPreferences(
        appPreferencesDataStore: AppLocalPreferencesDataStore
    ): AppLocalPreferences

    @Binds
    @Singleton
    abstract fun bindCountryLocalDataSource(
        countryLocalDataSourceImpl: CountryLocalDataSourceImpl
    ): CountryLocalDataSource

    @Binds
    @Singleton
    abstract fun bindCategoryLocalSource(
        impl: CategoryLocalDataSourceImpl
    ): CategoryLocalDataSource

    @Binds
    @Singleton
    abstract fun bindMovieLocalDataSource(
        movieLocalDataSourceImpl: MovieLocalDataSourceImpl
    ): MovieLocalDataSource

    @Binds
    @Singleton
    abstract fun bindTvShowLocalDataSource(
        tvShowLocalDataSourceImpl: TvShowLocalDataSourceImpl
    ): TvShowLocalDataSource

    @Binds
    @Singleton
    abstract fun bindRecentSearchLocalDataSource(
        recentSearchLocalDataSourceImpl: RecentSearchLocalDataSourceImpl
    ): RecentSearchLocalDataSource

    @Binds
    @Singleton
    abstract fun bindWatchHistoryLocalDataSource(
        watchHistoryLocalDataSourceImpl: WatchHistoryLocalDataSourceImpl
    ): WatchHistoryLocalDataSource

    @Binds
    @Singleton
    abstract fun bindProfileLocalDataSource(
        profileLocalDataSourceImpl: ProfileLocalDataSourceImpl
    ): ProfileLocalDataSource

    @Binds
    @Singleton
    abstract fun bindGameLocalDataSource(
        gameLocalDataSource: GameLocalDataSourceImpl
    ): GameLocalDataSource

    @Binds
    @Singleton
    abstract fun bindGameInMemoryDataSource(
        gameInMemoryDataSource: GameInMemoryDataSource
    ): GameSessionLocalDataSource

}
