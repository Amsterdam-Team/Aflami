package com.example.repository.mapper.remote

import com.example.repository.dto.remote.movieGallery.RemoteMovieGalleryResponse
import com.example.repository.mapper.shared.EntityMapper

class GalleryRemoteMapper : EntityMapper<RemoteMovieGalleryResponse, List<String>>{

    override fun toEntity(dto: RemoteMovieGalleryResponse): List<String> =
        dto.backdrops.map { it.fullFilePath.orEmpty() }

}