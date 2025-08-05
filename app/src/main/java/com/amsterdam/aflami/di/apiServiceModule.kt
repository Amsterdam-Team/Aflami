package com.amsterdam.aflami.di

import com.amsterdam.domain.repository.AppPreferencesRepository
import com.amsterdam.remotedatasource.api.AuthenticationApiService
import com.amsterdam.remotedatasource.api.CategoryApiService
import com.amsterdam.remotedatasource.api.CountryApiService
import com.amsterdam.remotedatasource.api.MovieApiService
import com.amsterdam.remotedatasource.api.ProfileApiService
import com.amsterdam.remotedatasource.api.TvShowsApiService
import com.amsterdam.remotedatasource.api.UserListApiService
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
    fun provideRetrofitClient(json: Json, appPreferencesRepository: AppPreferencesRepository): RetrofitClient =
        RetrofitClient(json, appPreferencesRepository)

    @Provides
    @Singleton
    fun provideAuthenticationApiService(retrofitClient:  RetrofitClient): AuthenticationApiService =
        retrofitClient.authenticationApiService()

    @Provides
    @Singleton
    fun provideMovieApiService(retrofitClient: RetrofitClient): MovieApiService =
        retrofitClient.movieApiService()

    @Provides
    @Singleton
    fun provideCategoryApiService(retrofitClient: RetrofitClient): CategoryApiService =
        retrofitClient.categoryApiService()

    @Provides
    @Singleton
    fun provideCountryApiService(retrofitClient: RetrofitClient): CountryApiService =
        retrofitClient.countryApiService()

    @Provides
    @Singleton
    fun provideTvShowsApiService(retrofitClient: RetrofitClient): TvShowsApiService =
        retrofitClient.tvApiService()

    @Provides
    @Singleton
    fun provideUserListApiService(retrofitClient: RetrofitClient): UserListApiService =
        retrofitClient.userListApiService()

    @Provides
    @Singleton
    fun provideProfileApiService(client: RetrofitClient): ProfileApiService =
        client.profileApiService()
}
