package com.amsterdam.remotedatasource.api

import com.amsterdam.repository.dto.remote.profile.AccountDetailsRemoteDto
import retrofit2.http.GET
import retrofit2.http.Query

interface ProfileApiService {
    @GET(GET_ACCOUNT_DETAILS_URL)
    suspend fun getAccountDetails(
        @Query("session_id") sessionId: String
    ): AccountDetailsRemoteDto

    companion object {
        const val GET_ACCOUNT_DETAILS_URL = "account/null"
    }
}