package com.amsterdam.aflami.di

import com.example.remotedatasource.client.KtorClient
import com.example.remotedatasource.datasource.RemoteCategoryDatasourceImpl
import com.example.remotedatasource.datasource.RemoteCountryDataSourceImpl
import com.example.remotedatasource.datasource.RemoteMovieDatasourceImpl
import com.example.remotedatasource.datasource.RemoteTvDatasourceImpl
import com.example.repository.datasource.remote.RemoteCategoryDatasource
import com.example.repository.datasource.remote.RemoteCountryDataSource
import com.example.repository.datasource.remote.RemoteMovieDatasource
import com.example.repository.datasource.remote.RemoteTvShowsDatasource
import kotlinx.serialization.json.Json
import org.koin.dsl.module

val remoteDataSourceModule = module {
    single { Json { ignoreUnknownKeys = true; isLenient = true } }

    single<KtorClient> { KtorClient() }

    single<RemoteCategoryDatasource> { RemoteCategoryDatasourceImpl(get()) }
    single<RemoteCountryDataSource> { RemoteCountryDataSourceImpl(get(), get()) }
    single<RemoteMovieDatasource> { RemoteMovieDatasourceImpl(get(), get()) }
    single<RemoteTvShowsDatasource> { RemoteTvDatasourceImpl(get()) }
}