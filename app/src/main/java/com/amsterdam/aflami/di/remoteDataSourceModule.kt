package com.amsterdam.aflami.di

import com.amsterdam.remotedatasource.datasource.AuthenticationRemoteDataDataSourceImpl
import com.amsterdam.remotedatasource.datasource.CategoryRemoteDataDataSourceImpl
import com.amsterdam.remotedatasource.datasource.CountryRemoteDataDataSourceImpl
import com.amsterdam.remotedatasource.datasource.MovieRemoteDataDataSourceImpl
import com.amsterdam.remotedatasource.datasource.PeopleRemoteDataDataSourceImpl
import com.amsterdam.remotedatasource.datasource.ProfileRemoteDataSourceImpl
import com.amsterdam.remotedatasource.datasource.TvRemoteDataDataSourceImpl
import com.amsterdam.remotedatasource.datasource.UserListRemoteDataDataSourceImpl
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
        authenticationRemoteDataSourceImpl: AuthenticationRemoteDataDataSourceImpl
    ): AuthenticationRemoteDataSource

    @Binds
    @Singleton
    abstract fun bindCategoryRemoteSource(
        categoryRemoteDataSourceImpl: CategoryRemoteDataDataSourceImpl
    ): CategoryRemoteDataSource

    @Binds
    @Singleton
    abstract fun bindCountryRemoteSource(
        countryRemoteDataSourceImpl: CountryRemoteDataDataSourceImpl
    ): CountryRemoteDataSource

    @Binds
    @Singleton
    abstract fun bindMovieRemoteSource(
        movieRemoteDataSourceImpl: MovieRemoteDataDataSourceImpl
    ): MovieRemoteDataSource


    @Binds
    @Singleton
    abstract fun bindPeopleRemoteSource(
        peopleRemoteDataSourceImpl: PeopleRemoteDataDataSourceImpl
    ): PeopleRemoteDataSource

    @Binds
    @Singleton
    abstract fun bindTvShowsRemoteSource(
        tvRemoteDataSourceImpl: TvRemoteDataDataSourceImpl
    ): TvShowsRemoteDataSource

    @Binds
    @Singleton
    abstract fun bindUserListRemoteSource(
        impl: UserListRemoteDataDataSourceImpl
    ): UserListRemoteDataSource

    @Binds
    @Singleton
    abstract fun bindProfileRemoteDataSource(
        profileRemoteDataSourceImpl: ProfileRemoteDataSourceImpl
    ): ProfileRemoteDataSource
}
