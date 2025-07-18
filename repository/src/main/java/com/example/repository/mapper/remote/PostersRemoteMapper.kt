package com.example.repository.mapper.remote

import com.example.repository.BuildConfig
import com.example.repository.dto.remote.movieGallery.RemoteMovieGalleryResponse
import com.example.repository.mapper.shared.EntityMapper

class PostersRemoteMapper : EntityMapper<RemoteMovieGalleryResponse, List<String>>{

    override fun toEntity(dto: RemoteMovieGalleryResponse): List<String> =
        dto.posters.map { BuildConfig.BASE_IMAGE_URL +it.filePath }

}