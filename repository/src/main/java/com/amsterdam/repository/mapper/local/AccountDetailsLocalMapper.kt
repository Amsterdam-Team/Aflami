package com.amsterdam.repository.mapper.local

import com.amsterdam.entity.AccountDetails
import com.amsterdam.repository.dto.local.profile.AccountDetailsLocalDto

fun AccountDetailsLocalDto.toEntity(): AccountDetails {
    return AccountDetails(
        accountId = this.accountId,
        username = this.username,
        avatarUrl = this.avatarUrl,
    )
}