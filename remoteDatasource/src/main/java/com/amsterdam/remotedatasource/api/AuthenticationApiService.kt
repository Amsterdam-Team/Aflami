package com.amsterdam.remotedatasource.api

import com.amsterdam.repository.dto.remote.authentication.AuthenticationRemoteResponse
import com.amsterdam.repository.dto.remote.authentication.CreateSessionRemoteDto
import com.amsterdam.repository.dto.remote.authentication.CreateSessionRemoteResponse
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface AuthenticationApiService {
    @GET("authentication/token/new")
    suspend fun createRequestToken(): AuthenticationRemoteResponse

    @POST("authentication/token/validate_with_login")
    suspend fun createSessionWithLogin(
        @Body createSessionRequest: CreateSessionRemoteDto,
    ): AuthenticationRemoteResponse

    @FormUrlEncoded
    @POST("authentication/session/new")
    suspend fun createSession(
        @Field("request_token") requestToken: String,
    ): CreateSessionRemoteResponse


}
