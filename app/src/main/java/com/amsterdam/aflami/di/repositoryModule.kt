package com.amsterdam.aflami.di

import com.amsterdam.domain.repository.AppPreferencesRepository
import com.amsterdam.domain.repository.AuthenticationRepository
import com.amsterdam.domain.repository.CountryRepository
import com.amsterdam.domain.repository.GameRepository
import com.amsterdam.domain.repository.MovieRepository
import com.amsterdam.domain.repository.ProfileRepository
import com.amsterdam.domain.repository.RecentSearchRepository
import com.amsterdam.domain.repository.TvShowRepository
import com.amsterdam.domain.repository.UserListRepository
import com.amsterdam.domain.repository.WatchHistoryRepository
import com.amsterdam.repository.repository.AppPreferencesRepositoryImpl
import com.amsterdam.repository.repository.AuthenticationRepositoryImpl
import com.amsterdam.repository.repository.CountryRepositoryImpl
import com.amsterdam.repository.repository.GameRepositoryImpl
import com.amsterdam.repository.repository.MovieRepositoryImpl
import com.amsterdam.repository.repository.ProfileRepositoryImpl
import com.amsterdam.repository.repository.RecentSearchRepositoryImpl
import com.amsterdam.repository.repository.TvShowRepositoryImpl
import com.amsterdam.repository.repository.UserListRepositoryImpl
import com.amsterdam.repository.repository.WatchHistoryRepositoryImpl
import com.amsterdam.repository.security.CryptoData
import com.amsterdam.repository.security.CryptoDataImpl
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
    abstract fun bindMovieRepository(
        impl: MovieRepositoryImpl
    ): MovieRepository

    @Binds
    @Singleton
    abstract fun bindRecentSearchRepository(
        recentSearchRepositoryImpl: RecentSearchRepositoryImpl
    ): RecentSearchRepository

    @Binds
    @Singleton
    abstract fun bindTvShowRepository(
        tvShowRepositoryImpl: TvShowRepositoryImpl
    ): TvShowRepository

    @Binds
    @Singleton
    abstract fun bindWatchHistoryRepository(
        watchHistoryRepositoryImpl: WatchHistoryRepositoryImpl
    ): WatchHistoryRepository

    @Binds
    @Singleton
    abstract fun bindUserListRepository(
        userListRepositoryImpl: UserListRepositoryImpl
    ): UserListRepository

    @Binds
    @Singleton
    abstract fun bindProfileRepository(
        profileRepositoryImpl: ProfileRepositoryImpl
    ): ProfileRepository


    @Binds
    @Singleton
    abstract fun bindGameRepository(
        gameRepository: GameRepositoryImpl
    ): GameRepository
}
