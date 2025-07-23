package com.example.repository.mapper.remote

import com.example.repository.dto.remote.movieGallery.RemoteGalleryResponse
import com.example.repository.mapper.shared.EntityMapper

class GalleryRemoteMapper : EntityMapper<RemoteGalleryResponse, List<String>>{

    override fun toEntity(dto: RemoteGalleryResponse): List<String> =
        dto.backdrops.map { it.fullFilePath.orEmpty() }

}