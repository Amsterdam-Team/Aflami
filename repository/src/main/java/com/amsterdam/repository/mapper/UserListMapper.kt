package com.amsterdam.repository.mapper

import com.amsterdam.entity.UserList
import com.amsterdam.repository.dto.remote.RemoteUserListDto

fun RemoteUserListDto.toEntity(): UserList {
    return UserList(
        id = id,
        name = name,
        description = description,
        itemCount = itemCount,
    )
} 