package com.amsterdam.repository.mapper.remoteToLocal

import com.amsterdam.repository.dto.local.LocalMovieCategoryDto
import com.amsterdam.repository.dto.remote.RemoteCategoryDto
import com.amsterdam.repository.mapper.shared.RemoteToLocalMapper
import javax.inject.Inject

class MovieCategoryRemoteLocalMapper @Inject constructor(): RemoteToLocalMapper<RemoteCategoryDto, LocalMovieCategoryDto> {
    override fun toLocal(remote: RemoteCategoryDto, args: List<Any>): LocalMovieCategoryDto {
        return LocalMovieCategoryDto(
            categoryId = remote.id.toLong(),
            storedLanguage = args.first().toString(),
            name = remote.name
        )
    }
}