package com.example.repository.mapper.remoteToLocal

import com.example.repository.dto.local.LocalMovieCategoryDto
import com.example.repository.dto.remote.RemoteCategoryDto
import com.example.repository.mapper.shared.RemoteToLocalMapper

class MovieCategoryRemoteLocalMapper :
    RemoteToLocalMapper<RemoteCategoryDto, LocalMovieCategoryDto> {
    override fun toLocal(remote: RemoteCategoryDto): LocalMovieCategoryDto {
        return LocalMovieCategoryDto(
            categoryId = remote.id,
            name = remote.name
        )
    }
}