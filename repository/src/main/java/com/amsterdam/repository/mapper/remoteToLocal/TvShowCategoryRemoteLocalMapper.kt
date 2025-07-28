package com.amsterdam.repository.mapper.remoteToLocal

import com.amsterdam.repository.dto.local.LocalTvShowCategoryDto
import com.amsterdam.repository.dto.remote.RemoteCategoryDto
import com.amsterdam.repository.mapper.shared.RemoteToLocalMapper
import javax.inject.Inject

class TvShowCategoryRemoteLocalMapper @Inject constructor():
    RemoteToLocalMapper<RemoteCategoryDto, LocalTvShowCategoryDto> {
    override fun toLocal(remote: RemoteCategoryDto, args: List<Any>): LocalTvShowCategoryDto {
        return LocalTvShowCategoryDto(
            categoryId = remote.id.toLong(),
            storedLanguage = args.first().toString(),
            name = remote.name
        )
    }

}