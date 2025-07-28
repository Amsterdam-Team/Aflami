package com.amsterdam.repository.mapper.remote

import com.amsterdam.repository.dto.remote.movieGallery.RemoteGalleryResponse
import com.amsterdam.repository.mapper.shared.EntityMapper
import javax.inject.Inject

class GalleryRemoteMapper @Inject constructor(): EntityMapper<RemoteGalleryResponse, List<String>>{

    override fun toEntity(dto: RemoteGalleryResponse): List<String> =
        dto.backdrops.map { it.fullFilePath.orEmpty() }

}