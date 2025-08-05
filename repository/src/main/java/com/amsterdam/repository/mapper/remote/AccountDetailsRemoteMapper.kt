package com.amsterdam.repository.mapper.remote

import com.amsterdam.entity.AccountDetails
import com.amsterdam.repository.dto.remote.profile.AccountDetailsDto

fun AccountDetailsDto.toEntity(): AccountDetails {
    return AccountDetails(
        id = this.id,
        username = this.username,
        avatarUrl = this.accountAvatar.movieDBData.fullAvatarPath.orEmpty(),
    )
}