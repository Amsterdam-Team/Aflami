package com.amsterdam.repository.mapper

import com.amsterdam.entity.WishList
import com.amsterdam.repository.dto.remote.WishListRemoteDto

fun WishListRemoteDto.toEntity(): WishList {
    return WishList(
        id = id,
        name = name,
        description = description,
        itemCount = itemCount,
    )
} 