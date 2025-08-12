package com.amsterdam.remotedatasource.api

import com.amsterdam.remotedatasource.utils.RequiresSessionId
import com.amsterdam.repository.dto.remote.profile.AccountDetailsRemoteDto
import retrofit2.http.GET

interface ProfileApiService {
    @RequiresSessionId
    @GET("account/null")
    suspend fun getAccountDetails(): AccountDetailsRemoteDto
}