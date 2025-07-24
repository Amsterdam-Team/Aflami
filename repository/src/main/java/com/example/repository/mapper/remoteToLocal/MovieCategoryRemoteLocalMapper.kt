package com.example.repository.mapper.remoteToLocal

import android.util.Log
import com.example.repository.dto.local.LocalMovieCategoryDto
import com.example.repository.dto.remote.RemoteCategoryDto
import com.example.repository.mapper.shared.RemoteToLocalMapper

class MovieCategoryRemoteLocalMapper : RemoteToLocalMapper<RemoteCategoryDto, LocalMovieCategoryDto> {
    override fun toLocal(remote: RemoteCategoryDto, args: List<Any>): LocalMovieCategoryDto {
        return LocalMovieCategoryDto(
            categoryId = remote.id.toLong(),
            storedLanguage = args.first().toString(),
            name = remote.name
        )
    }
}