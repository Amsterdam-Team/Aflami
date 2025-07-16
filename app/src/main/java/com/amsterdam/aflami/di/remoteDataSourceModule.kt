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
import org.koin.dsl.module

val remoteDataSourceModule = module {
    single { Json { ignoreUnknownKeys = true; isLenient = true } }

    single<KtorClient> { KtorClient() }

    single<CategoryRemoteSource> { CategoryRemoteSourceImpl(get()) }
    single<CountryRemoteSource> { CountryRemoteSourceImpl(get(), get()) }
    single<MovieRemoteSource> { MovieRemoteSourceImpl(get(), get()) }
    single<TvShowsRemoteSource> { TvRemoteSourceImpl(get()) }
}