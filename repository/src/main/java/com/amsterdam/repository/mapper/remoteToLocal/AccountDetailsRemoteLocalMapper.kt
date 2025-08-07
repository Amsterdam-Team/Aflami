package com.amsterdam.repository.mapper.remoteToLocal

import com.amsterdam.repository.dto.local.profile.AccountDetailsLocalDto
import com.amsterdam.repository.dto.remote.profile.AccountDetailsRemoteDto

fun AccountDetailsRemoteDto.toLocal(): AccountDetailsLocalDto {
    return AccountDetailsLocalDto(
        accountId = this.id,
        username = this.username,
        avatarUrl = this.accountAvatar.movieDBData.fullAvatarPath.orEmpty(),
    )
}