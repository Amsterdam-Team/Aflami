package com.amsterdam.aflami.di

import com.amsterdam.remotedatasource.datasource.AuthenticationRemoteDataSourceImpl
import com.amsterdam.remotedatasource.datasource.CategoryRemoteDataSourceImpl
import com.amsterdam.remotedatasource.datasource.CountryRemoteDataSourceImpl
import com.amsterdam.remotedatasource.datasource.MovieRemoteDataSourceImpl
import com.amsterdam.remotedatasource.datasource.PeopleRemoteDataDataSourceImpl
import com.amsterdam.remotedatasource.datasource.ProfileRemoteDataSourceImpl
import com.amsterdam.remotedatasource.datasource.TvRemoteDataSourceImpl
import com.amsterdam.remotedatasource.datasource.UserListRemoteDataSourceImpl
import com.amsterdam.repository.datasource.remote.AuthenticationRemoteDataSource
import com.amsterdam.repository.datasource.remote.CategoryRemoteDataSource
import com.amsterdam.repository.datasource.remote.CountryRemoteDataSource
import com.amsterdam.repository.datasource.remote.MovieRemoteDataSource
import com.amsterdam.repository.datasource.remote.PeopleRemoteDataSource
import com.amsterdam.repository.datasource.remote.ProfileRemoteDataSource
import com.amsterdam.repository.datasource.remote.TvShowsRemoteDataSource
import com.amsterdam.repository.datasource.remote.UserListRemoteDataSource
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
    ): AuthenticationRemoteDataSource

    @Binds
    @Singleton
    abstract fun bindCategoryRemoteSource(
        categoryRemoteDataSourceImpl: CategoryRemoteDataSourceImpl
    ): CategoryRemoteDataSource

    @Binds
    @Singleton
    abstract fun bindCountryRemoteSource(
        countryRemoteDataSourceImpl: CountryRemoteDataSourceImpl
    ): CountryRemoteDataSource

    @Binds
    @Singleton
    abstract fun bindMovieRemoteSource(
        movieRemoteDataSourceImpl: MovieRemoteDataSourceImpl
    ): MovieRemoteDataSource


    @Binds
    @Singleton
    abstract fun bindPeopleRemoteSource(
        peopleRemoteDataSourceImpl: PeopleRemoteDataDataSourceImpl
    ): PeopleRemoteDataSource

    @Binds
    @Singleton
    abstract fun bindTvShowsRemoteSource(
        tvRemoteDataSourceImpl: TvRemoteDataSourceImpl
    ): TvShowsRemoteDataSource

    @Binds
    @Singleton
    abstract fun bindUserListRemoteSource(
        impl: UserListRemoteDataSourceImpl
    ): UserListRemoteDataSource

    @Binds
    @Singleton
    abstract fun bindProfileRemoteDataSource(
        profileRemoteDataSourceImpl: ProfileRemoteDataSourceImpl
    ): ProfileRemoteDataSource
}
