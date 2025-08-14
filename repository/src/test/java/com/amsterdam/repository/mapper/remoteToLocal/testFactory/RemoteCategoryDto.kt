package com.amsterdam.repository.mapper.remoteToLocal.testFactory

import com.amsterdam.repository.dto.remote.CategoryRemoteDto

fun createRemoteCategoryDto(
    id: Int = 28,
    name: String = "Action"
): CategoryRemoteDto {
    return CategoryRemoteDto(
        id = id,
        name = name
    )
}