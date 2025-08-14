package com.amsterdam.repository.mapper

import com.amsterdam.entity.UserList
import com.amsterdam.repository.dto.remote.UserListRemoteDto

fun UserListRemoteDto.toEntity(): UserList {
    return UserList(
        id = id,
        name = name,
        description = description,
        itemCount = itemCount,
    )
} 