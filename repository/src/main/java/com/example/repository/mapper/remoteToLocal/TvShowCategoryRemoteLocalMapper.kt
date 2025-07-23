package com.example.repository.mapper.remoteToLocal

import com.example.repository.dto.local.LocalTvShowCategoryDto
import com.example.repository.dto.remote.RemoteCategoryDto
import com.example.repository.mapper.shared.RemoteToLocalMapper

class TvShowCategoryRemoteLocalMapper :
    RemoteToLocalMapper<RemoteCategoryDto, LocalTvShowCategoryDto> {
    override fun toLocal(remote: RemoteCategoryDto, args: List<Any>): LocalTvShowCategoryDto {
        return LocalTvShowCategoryDto(
            categoryId = remote.id.toLong(),
            storedLanguage = args.first().toString(),
            name = remote.name
        )
    }

}