package com.amsterdam.repository.mapper

import com.amsterdam.entity.AccountDetails
import com.amsterdam.repository.dto.local.profile.AccountDetailsLocalDto
import com.amsterdam.repository.dto.remote.profile.AccountDetailsRemoteDto

fun AccountDetailsLocalDto.toEntity(): AccountDetails {
    return AccountDetails(
        accountId = this.accountId,
        username = this.username,
        avatarUrl = this.avatarUrl,
    )
}

fun AccountDetailsRemoteDto.toEntity(): AccountDetails {
    return AccountDetails(
        accountId = this.id,
        username = this.username,
        avatarUrl = this.accountAvatar.movieDBData.fullAvatarPath.orEmpty(),
    )
}

fun AccountDetailsRemoteDto.toLocalDto(): AccountDetailsLocalDto {
    return AccountDetailsLocalDto(
        accountId = this.id,
        username = this.username,
        avatarUrl = this.accountAvatar.movieDBData.fullAvatarPath.orEmpty(),
    )
}