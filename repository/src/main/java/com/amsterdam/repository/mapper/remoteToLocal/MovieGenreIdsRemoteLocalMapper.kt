package com.amsterdam.repository.mapper.remoteToLocal

import com.amsterdam.repository.dto.local.LocalMovieCategoryDto
import com.amsterdam.repository.mapper.shared.RemoteToLocalMapper
import com.amsterdam.repository.mapper.shared.mapCategoryIdToMovieGenre
import javax.inject.Inject

class MovieGenreIdsRemoteLocalMapper @Inject constructor():
    RemoteToLocalMapper<Int, LocalMovieCategoryDto> {
    override fun toLocal(remote: Int, args: List<Any>): LocalMovieCategoryDto {
        return LocalMovieCategoryDto(
            categoryId = remote.toLong(),
            name = mapCategoryIdToMovieGenre(remote.toLong()).name,
            storedLanguage = args.first().toString(),
        )
    }
}