package com.example.repository.mapper.remoteToLocal

import com.example.repository.dto.local.LocalTvShowCategoryDto
import com.example.repository.mapper.shared.RemoteToLocalMapper
import com.example.repository.mapper.shared.toTvShowCategory

class TvShowGenreIdsRemoteLocalMapper :
    RemoteToLocalMapper<Int, LocalTvShowCategoryDto> {
    override fun toLocal(remote: Int, args: List<Any>): LocalTvShowCategoryDto {
        return LocalTvShowCategoryDto(
            categoryId = remote.toLong(),
            name = remote.toLong().toTvShowCategory().name,
            storedLanguage = args.first().toString(),
        )
    }
}