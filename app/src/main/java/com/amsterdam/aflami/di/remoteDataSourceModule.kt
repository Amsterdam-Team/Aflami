package com.amsterdam.aflami.di

import com.amsterdam.remotedatasource.datasource.AuthenticationRemoteDataSourceImpl
import com.amsterdam.remotedatasource.datasource.CategoryRemoteDataSourceImpl
import com.amsterdam.remotedatasource.datasource.CountryRemoteDataSourceImpl
import com.amsterdam.remotedatasource.datasource.MovieRemoteDataSourceImpl
import com.amsterdam.remotedatasource.datasource.PeopleRemoteDataSourceImpl
import com.amsterdam.remotedatasource.datasource.ProfileRemoteDataSourceImpl
import com.amsterdam.remotedatasource.datasource.TvRemoteDataSourceImpl
import com.amsterdam.remotedatasource.datasource.UserListRemoteDataSourceImpl
import com.amsterdam.repository.datasource.remote.AuthenticationRemoteSource
import com.amsterdam.repository.datasource.remote.CategoryRemoteSource
import com.amsterdam.repository.datasource.remote.CountryRemoteSource
import com.amsterdam.repository.datasource.remote.MovieRemoteSource
import com.amsterdam.repository.datasource.remote.PeopleRemoteSource
import com.amsterdam.repository.datasource.remote.ProfileRemoteDataSource
import com.amsterdam.repository.datasource.remote.TvShowsRemoteSource
import com.amsterdam.repository.datasource.remote.UserListRemoteSource
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
        authenticationRemoteDataSourceImpl: AuthenticationRemoteDataSourceImpl
    ): AuthenticationRemoteSource

    @Binds
    @Singleton
    abstract fun bindCategoryRemoteSource(
        categoryRemoteDataSourceImpl: CategoryRemoteDataSourceImpl
    ): CategoryRemoteSource

    @Binds
    @Singleton
    abstract fun bindCountryRemoteSource(
        countryRemoteDataSourceImpl: CountryRemoteDataSourceImpl
    ): CountryRemoteSource

    @Binds
    @Singleton
    abstract fun bindMovieRemoteSource(
        movieRemoteDataSourceImpl: MovieRemoteDataSourceImpl
    ): MovieRemoteSource


    @Binds
    @Singleton
    abstract fun bindPeopleRemoteSource(
        peopleRemoteDataSourceImpl: PeopleRemoteDataSourceImpl
    ): PeopleRemoteSource

    @Binds
    @Singleton
    abstract fun bindTvShowsRemoteSource(
        tvRemoteDataSourceImpl: TvRemoteDataSourceImpl
    ): TvShowsRemoteSource

    @Binds
    @Singleton
    abstract fun bindUserListRemoteSource(
        impl: UserListRemoteDataSourceImpl
    ): UserListRemoteSource

    @Binds
    @Singleton
    abstract fun bindProfileRemoteDataSource(
        profileRemoteDataSourceImpl: ProfileRemoteDataSourceImpl
    ): ProfileRemoteDataSource
}
