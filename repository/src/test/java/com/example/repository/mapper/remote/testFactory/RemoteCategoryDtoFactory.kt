package com.example.repository.mapper.remote.testFactory

import com.example.repository.dto.remote.RemoteCategoryDto

fun createRemoteCategoryDto(
    id: Long = 1,
    name: String = "Default Category"
): RemoteCategoryDto {
    return RemoteCategoryDto(
        id = id,
        name = name
    )
}