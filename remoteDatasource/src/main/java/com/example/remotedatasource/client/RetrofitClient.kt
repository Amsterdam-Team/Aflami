package com.example.remotedatasource.client

import com.example.remotedatasource.BuildConfig
import com.example.remotedatasource.api.AuthenticationApiService
import com.example.remotedatasource.api.CategoryApiService
import com.example.remotedatasource.api.CountryApiService
import com.example.remotedatasource.api.MovieApiService
import com.example.remotedatasource.api.TvShowsApiService
import com.example.repository.utils.getDeviceLanguage
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit

class RetrofitClient(
    private val json: Json
) {
    private val TOKEN_HEADER_NAME = "Authorization"
    private val LANGUAGE_PARAM_NAME = "language"
    private val SESSION_PARAM_NAME = "session_id"

    private val token = BuildConfig.BEARER_TOKEN
    private val sessionId: String? = null

    private val okHttpClient: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .addInterceptor(createAuthAndParamInterceptor())
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .build()
    }

    private fun createAuthAndParamInterceptor(): Interceptor {
        return Interceptor { chain ->
            val originalRequest = chain.request()
            val originalHttpUrlBuilder = originalRequest.url.newBuilder()

            originalHttpUrlBuilder.addQueryParameter(LANGUAGE_PARAM_NAME, getDeviceLanguage())

            if (!sessionId.isNullOrBlank()) {
                originalHttpUrlBuilder.addQueryParameter(SESSION_PARAM_NAME, sessionId)
            }

            val newRequest = originalRequest.newBuilder()
                .url(originalHttpUrlBuilder.build())
                .header(TOKEN_HEADER_NAME, "Bearer $token")
                .build()

            chain.proceed(newRequest)
        }
    }

    private val retrofit: Retrofit by lazy {
        val contentType = "application/json".toMediaType()
        Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(json.asConverterFactory(contentType))
            .build()
    }

    fun authenticationApiService(): AuthenticationApiService = retrofit.create(AuthenticationApiService::class.java)

    fun movieApiService(): MovieApiService = retrofit.create(MovieApiService::class.java)
    fun categoryApiService(): CategoryApiService = retrofit.create(CategoryApiService::class.java)
    fun countryApiService(): CountryApiService = retrofit.create(CountryApiService::class.java)
    fun tvApiService(): TvShowsApiService = retrofit.create(TvShowsApiService::class.java)
}