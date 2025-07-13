package com.amsterdam.aflami.di

import com.amsterdam.remotedatasource.client.KtorClient
import com.amsterdam.remotedatasource.datasource.RemoteCategoryDatasourceImpl
import com.amsterdam.remotedatasource.datasource.RemoteCountryDataSourceImpl
import com.amsterdam.remotedatasource.datasource.RemoteMovieDatasourceImpl
import com.amsterdam.remotedatasource.datasource.RemoteTvDatasourceImpl
import com.amsterdam.repository.datasource.remote.RemoteCategoryDatasource
import com.amsterdam.repository.datasource.remote.RemoteCountryDataSource
import com.amsterdam.repository.datasource.remote.RemoteMovieDatasource
import com.amsterdam.repository.datasource.remote.RemoteTvShowsDatasource
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