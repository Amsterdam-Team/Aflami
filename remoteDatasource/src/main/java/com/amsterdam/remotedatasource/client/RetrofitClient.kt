package com.amsterdam.remotedatasource.client

import com.amsterdam.domain.repository.AppPreferencesRepository
import com.amsterdam.remotedatasource.BuildConfig
import com.amsterdam.remotedatasource.api.AuthenticationApiService
import com.amsterdam.remotedatasource.api.CategoryApiService
import com.amsterdam.remotedatasource.api.CountryApiService
import com.amsterdam.remotedatasource.api.MovieApiService
import com.amsterdam.remotedatasource.api.PeopleApiService
import com.amsterdam.remotedatasource.api.ProfileApiService
import com.amsterdam.remotedatasource.api.TvShowsApiService
import com.amsterdam.remotedatasource.api.UserListApiService
import com.amsterdam.remotedatasource.utils.RequiresSessionId
import com.amsterdam.repository.datasource.local.AuthenticationLocalDataSource
import com.amsterdam.repository.security.CryptoManager
import com.amsterdam.repository.utils.decryptString
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Invocation
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit

class RetrofitClient(
    private val json: Json,
    private val preferences: AppPreferencesRepository,
    private val authenticationLocalDataSource: AuthenticationLocalDataSource,
    private val cryptoManager: CryptoManager

) {
    private val TOKEN_HEADER_NAME = "Authorization"
    private val LANGUAGE_PARAM_NAME = "language"
    private val SESSION_PARAM_NAME = "session_id"

    private val token = BuildConfig.BEARER_TOKEN

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

            val language = runBlocking { preferences.getAppLanguage().first() }
            originalHttpUrlBuilder.addQueryParameter(LANGUAGE_PARAM_NAME, language)

            val requireSession = originalRequest.tag(Invocation::class.java)?.method()
                ?.getAnnotation(RequiresSessionId::class.java) != null

            if (requireSession) {
                val sessionId = runBlocking {
                    cryptoManager.decryptString(authenticationLocalDataSource.getCachedSessionId())
                }

                if (!sessionId.isNullOrBlank()) {
                    originalHttpUrlBuilder.addQueryParameter(SESSION_PARAM_NAME, sessionId)
                }
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

    fun authenticationApiService(): AuthenticationApiService =
        retrofit.create(AuthenticationApiService::class.java)

    fun movieApiService(): MovieApiService = retrofit.create(MovieApiService::class.java)
    fun categoryApiService(): CategoryApiService = retrofit.create(CategoryApiService::class.java)
    fun countryApiService(): CountryApiService = retrofit.create(CountryApiService::class.java)
    fun tvApiService(): TvShowsApiService = retrofit.create(TvShowsApiService::class.java)
    fun userListApiService(): UserListApiService = retrofit.create(UserListApiService::class.java)
    fun profileApiService(): ProfileApiService = retrofit.create(ProfileApiService::class.java)

    fun peopleApiService(): PeopleApiService = retrofit.create(PeopleApiService::class.java)
}