package com.amsterdam.aflami.di

import com.example.remotedatasource.api.CategoryApiService
import com.example.remotedatasource.api.CountryApiService
import com.example.remotedatasource.api.MovieApiService
import com.example.remotedatasource.api.TvShowsApiService
import com.example.remotedatasource.client.RetrofitClient
import com.example.remotedatasource.serviceProvider.CategoryServiceProvider
import com.example.remotedatasource.serviceProvider.CountryServiceProvider
import com.example.remotedatasource.serviceProvider.MovieServiceProvider
import com.example.remotedatasource.serviceProvider.TvShowsServiceProvider
import com.example.remotedatasource.serviceProvider.implementation.CategoryServiceProviderImpl
import com.example.remotedatasource.serviceProvider.implementation.CountryServiceProviderImpl
import com.example.remotedatasource.serviceProvider.implementation.MovieServiceProviderImpl
import com.example.remotedatasource.serviceProvider.implementation.TvShowsServiceProviderImpl
import kotlinx.serialization.json.Json
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val serviceModule = module {
    single { Json { prettyPrint = true; isLenient = true; ignoreUnknownKeys = true } }
    singleOf(::RetrofitClient)

    single { get<RetrofitClient>().movieApiService() } bind MovieApiService::class
    single { get<RetrofitClient>().categoryApiService() } bind CategoryApiService::class
    single { get<RetrofitClient>().countryApiService() } bind CountryApiService::class
    single { get<RetrofitClient>().tvApiService() } bind TvShowsApiService::class

    singleOf(::CategoryServiceProviderImpl) bind CategoryServiceProvider::class
    singleOf(::CountryServiceProviderImpl) bind CountryServiceProvider::class
    singleOf(::MovieServiceProviderImpl) bind MovieServiceProvider::class
    singleOf(::TvShowsServiceProviderImpl) bind TvShowsServiceProvider::class
}