package com.amsterdam.domain.repository

import com.amsterdam.entity.AccountDetails

interface ProfileRepository {
    suspend fun getAccountDetails(): AccountDetails
}