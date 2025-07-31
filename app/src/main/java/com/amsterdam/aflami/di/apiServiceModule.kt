package com.amsterdam.aflami.di

import com.amsterdam.domain.repository.AppPreferencesRepository
import com.amsterdam.remotedatasource.api.AuthenticationApiService
import com.amsterdam.remotedatasource.api.CategoryApiService
import com.amsterdam.remotedatasource.api.CountryApiService
import com.amsterdam.remotedatasource.api.MovieApiService
import com.amsterdam.remotedatasource.api.TvShowsApiService
import com.amsterdam.remotedatasource.client.RetrofitClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object ServiceProvidesModule {

    @Provides
    @Singleton
    fun provideJson(): Json = Json {
        prettyPrint = true
        isLenient = true
        ignoreUnknownKeys = true
    }

    @Provides
    @Singleton
    fun provideRetrofitClient(json: Json, preferences: AppPreferencesRepository): RetrofitClient =
        RetrofitClient(json, preferences)

    @Provides
    @Singleton
    fun provideAuthenticationApiService(client: RetrofitClient): AuthenticationApiService =
        client.authenticationApiService()

    @Provides
    @Singleton
    fun provideMovieApiService(client: RetrofitClient): MovieApiService =
        client.movieApiService()

    @Provides
    @Singleton
    fun provideCategoryApiService(client: RetrofitClient): CategoryApiService =
        client.categoryApiService()

    @Provides
    @Singleton
    fun provideCountryApiService(client: RetrofitClient): CountryApiService =
        client.countryApiService()

    @Provides
    @Singleton
    fun provideTvShowsApiService(client: RetrofitClient): TvShowsApiService =
        client.tvApiService()
}
