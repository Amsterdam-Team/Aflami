package com.amsterdam.aflami.di

import com.example.remotedatasource.datasource.CategoryRemoteDataSourceImpl
import com.example.remotedatasource.datasource.CountryRemoteDataSourceImpl
import com.example.remotedatasource.datasource.MovieRemoteDataSourceImpl
import com.example.remotedatasource.datasource.TvRemoteDataSourceImpl
import com.example.repository.datasource.remote.CategoryRemoteSource
import com.example.repository.datasource.remote.CountryRemoteSource
import com.example.repository.datasource.remote.MovieRemoteSource
import com.example.repository.datasource.remote.TvShowsRemoteSource
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val remoteDataSourceModule = module {
    singleOf(::CategoryRemoteDataSourceImpl) bind CategoryRemoteSource::class
    singleOf(::CountryRemoteDataSourceImpl) bind CountryRemoteSource::class
    singleOf(::MovieRemoteDataSourceImpl) bind MovieRemoteSource::class
    singleOf(::TvRemoteDataSourceImpl) bind TvShowsRemoteSource::class
}