package com.amsterdam.repository.mapper.remote

import com.amsterdam.entity.AccountDetails
import com.amsterdam.repository.dto.remote.profile.AccountDetailsRemoteDto

fun AccountDetailsRemoteDto.toEntity(): AccountDetails {
    return AccountDetails(
        accountId = this.id,
        username = this.username,
        avatarUrl = this.accountAvatar.movieDBData.fullAvatarPath.orEmpty(),
    )
}