package com.example.repository.mapper.remote

import com.example.domain.mapper.DomainMapper
import com.example.repository.BuildConfig
import com.example.repository.dto.remote.movieGallery.RemoteMovieGalleryResponse

class GalleryRemoteMapper : DomainMapper<List<String>, RemoteMovieGalleryResponse> {

    override fun toDomain(dto: RemoteMovieGalleryResponse): List<String> {
        return dto.posters.map { BuildConfig.BASE_IMAGE_URL + it.filePath }
    }

}