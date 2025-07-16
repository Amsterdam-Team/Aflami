package com.amsterdam.aflami.di

import com.example.remotedatasource.client.KtorClient
import com.example.remotedatasource.datasource.CategoryRemoteSourceImpl
import com.example.remotedatasource.datasource.CountryRemoteSourceImpl
import com.example.remotedatasource.datasource.MovieRemoteSourceImpl
import com.example.remotedatasource.datasource.TvRemoteSourceImpl
import com.example.repository.datasource.remote.CategoryRemoteSource
import com.example.repository.datasource.remote.CountryRemoteSource
import com.example.repository.datasource.remote.MovieRemoteSource
import com.example.repository.datasource.remote.TvShowsRemoteSource
import kotlinx.serialization.json.Json
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val remoteDataSourceModule = module {
    single { Json { prettyPrint = true; isLenient = true; ignoreUnknownKeys = true } }

    singleOf(::KtorClient)
    singleOf(::CategoryRemoteSourceImpl) { bind<CategoryRemoteSource>() }
    singleOf(::CountryRemoteSourceImpl) { bind<CountryRemoteSource>() }
    singleOf(::MovieRemoteSourceImpl) { bind<MovieRemoteSource>() }
    singleOf(::TvRemoteSourceImpl) { bind<TvShowsRemoteSource>() }
}