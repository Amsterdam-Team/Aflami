package com.amsterdam.aflami.di

import com.amsterdam.domain.repository.*
import com.amsterdam.repository.repository.*
import com.amsterdam.repository.security.CryptoData
import com.amsterdam.repository.security.CryptoDataImpl
import com.amsterdam.repository.utils.RecentSearchHandler
import com.amsterdam.repository.utils.RecentSearchHandlerImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindCryptoData(
        impl: CryptoDataImpl
    ): CryptoData

    @Binds
    @Singleton
    abstract fun bindRecentSearchHandler(
        impl: RecentSearchHandlerImpl
    ): RecentSearchHandler

    @Binds
    @Singleton
    abstract fun bindAuthenticationRepository(
        impl: AuthenticationRepositoryImpl
    ): AuthenticationRepository


    @Binds
    @Singleton
    abstract fun bindAppPreferencesRepository(
        impl: AppPreferencesRepositoryImpl
    ): AppPreferencesRepository

    @Binds
    @Singleton
    abstract fun bindCountryRepository(
        impl: CountryRepositoryImpl
    ): CountryRepository

    @Binds
    @Singleton
    abstract fun bindCategoryRepository(
        impl: CategoryRepositoryImpl
    ): CategoryRepository

    @Binds
    @Singleton
    abstract fun bindMovieRepository(
        impl: MovieRepositoryImpl
    ): MovieRepository

    @Binds
    @Singleton
    abstract fun bindRecentSearchRepository(
        impl: RecentSearchRepositoryImpl
    ): RecentSearchRepository

    @Binds
    @Singleton
    abstract fun bindTvShowRepository(
        impl: TvShowRepositoryImpl
    ): TvShowRepository

    @Binds
    @Singleton
    abstract fun bindWatchHistoryRepository(
        impl: WatchHistoryRepositoryImpl
    ): WatchHistoryRepository
}
