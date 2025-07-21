package com.amsterdam.aflami.di

import com.example.remotedatasource.api.CategoryApiService
import com.example.remotedatasource.api.CountryApiService
import com.example.remotedatasource.api.MovieApiService
import com.example.remotedatasource.api.TvShowsApiService
import com.example.remotedatasource.client.RetrofitClient
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

    singleOf(::RetrofitClient)
    single { get<RetrofitClient>().movieApiService() } bind MovieApiService::class
    single { get<RetrofitClient>().categoryApiService() } bind CategoryApiService::class
    single { get<RetrofitClient>().countryApiService() } bind CountryApiService::class
    single { get<RetrofitClient>().tvApiService() } bind TvShowsApiService::class
    singleOf(::CategoryRemoteDataSourceImpl) { bind<CategoryRemoteSource>() }
    singleOf(::CountryRemoteDataSourceImpl) { bind<CountryRemoteSource>() }
    singleOf(::MovieRemoteDataSourceImpl) { bind<MovieRemoteSource>() }
    singleOf(::TvRemoteDataSourceImpl) { bind<TvShowsRemoteSource>() }
}