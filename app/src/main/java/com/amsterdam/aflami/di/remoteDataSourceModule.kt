package com.amsterdam.aflami.di

import com.example.remotedatasource.client.KtorClient
import com.example.remotedatasource.client.NetworkClient
import com.example.remotedatasource.datasource.CategoryRemoteDataSourceImpl
import com.example.remotedatasource.datasource.CountryRemoteDataSourceImpl
import com.example.remotedatasource.datasource.MovieRemoteDataSourceImpl
import com.example.remotedatasource.datasource.TvRemoteDataSourceImpl
import com.example.repository.datasource.remote.CategoryRemoteSource
import com.example.repository.datasource.remote.CountryRemoteSource
import com.example.repository.datasource.remote.MovieRemoteSource
import com.example.repository.datasource.remote.TvShowsRemoteSource
import kotlinx.serialization.json.Json
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val remoteDataSourceModule = module {
    single { Json { prettyPrint = true; isLenient = true; ignoreUnknownKeys = true } }

    singleOf(::KtorClient).bind<NetworkClient>()
    singleOf(::CategoryRemoteDataSourceImpl) { bind<CategoryRemoteSource>() }
    singleOf(::CountryRemoteDataSourceImpl) { bind<CountryRemoteSource>() }
    singleOf(::MovieRemoteDataSourceImpl) { bind<MovieRemoteSource>() }
    singleOf(::TvRemoteDataSourceImpl) { bind<TvShowsRemoteSource>() }
}