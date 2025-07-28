package com.amsterdam.repository.mapper.remoteToLocal

import com.amsterdam.repository.dto.local.LocalTvShowCategoryDto
import com.amsterdam.repository.mapper.shared.RemoteToLocalMapper
import com.amsterdam.repository.mapper.shared.mapCategoryIdToTvShowGenre
import javax.inject.Inject

class TvShowGenreIdsRemoteLocalMapper @Inject constructor():
    RemoteToLocalMapper<Int, LocalTvShowCategoryDto> {
    override fun toLocal(remote: Int, args: List<Any>): LocalTvShowCategoryDto {
        return LocalTvShowCategoryDto(
            categoryId = remote.toLong(),
            name = mapCategoryIdToTvShowGenre(remote.toLong()).name,
            storedLanguage = args.first().toString(),
        )
    }
}