package com.amsterdam.repository.mapper.remoteToLocal.testFactory

import com.amsterdam.repository.dto.remote.RemoteCategoryDto

fun createRemoteCategoryDto(
    id: Int = 28,
    name: String = "Action"
): RemoteCategoryDto {
    return RemoteCategoryDto(
        id = id,
        name = name
    )
}