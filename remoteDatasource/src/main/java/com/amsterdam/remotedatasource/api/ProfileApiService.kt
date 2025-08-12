package com.amsterdam.remotedatasource.api

import com.amsterdam.remotedatasource.utils.RequiresSessionId
import com.amsterdam.repository.dto.remote.profile.AccountDetailsRemoteDto
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface ProfileApiService {
    @RequiresSessionId
    @GET("account/null")
    suspend fun getAccountDetails(): AccountDetailsRemoteDto
}