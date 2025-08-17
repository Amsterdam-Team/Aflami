package com.amsterdam.entity

data class AccountDetails(
    val accountId: Int,
    val username: String,
    val avatarUrl: String? = null,
)
