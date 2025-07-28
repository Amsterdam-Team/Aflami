package com.amsterdam.aflami.di

import com.amsterdam.remotedatasource.datasource.AuthenticationRemoteDataSourceImpl
import com.amsterdam.remotedatasource.datasource.CategoryRemoteDataSourceImpl
import com.amsterdam.remotedatasource.datasource.CountryRemoteDataSourceImpl
import com.amsterdam.remotedatasource.datasource.MovieRemoteDataSourceImpl
import com.amsterdam.remotedatasource.datasource.TvRemoteDataSourceImpl
import com.amsterdam.repository.datasource.remote.AuthenticationRemoteSource
import com.amsterdam.repository.datasource.remote.CategoryRemoteSource
import com.amsterdam.repository.datasource.remote.CountryRemoteSource
import com.amsterdam.repository.datasource.remote.MovieRemoteSource
import com.amsterdam.repository.datasource.remote.TvShowsRemoteSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RemoteDataSourceBindModule {

    @Binds
    @Singleton
    abstract fun bindAuthenticationRemoteSource(
        impl: AuthenticationRemoteDataSourceImpl
    ): AuthenticationRemoteSource

    @Binds
    @Singleton
    abstract fun bindCategoryRemoteSource(
        impl: CategoryRemoteDataSourceImpl
    ): CategoryRemoteSource

    @Binds
    @Singleton
    abstract fun bindCountryRemoteSource(
        impl: CountryRemoteDataSourceImpl
    ): CountryRemoteSource

    @Binds
    @Singleton
    abstract fun bindMovieRemoteSource(
        impl: MovieRemoteDataSourceImpl
    ): MovieRemoteSource

    @Binds
    @Singleton
    abstract fun bindTvShowsRemoteSource(
        impl: TvRemoteDataSourceImpl
    ): TvShowsRemoteSource
}