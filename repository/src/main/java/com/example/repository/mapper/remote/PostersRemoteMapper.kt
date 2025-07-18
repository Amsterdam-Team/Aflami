package com.example.repository.mapper.remote

import com.example.repository.BuildConfig
import com.example.repository.dto.remote.movieGallery.RemoteMovieGalleryResponse

class PostersRemoteMapper {

    fun mapPostersToDomain(galleryResponse: RemoteMovieGalleryResponse) : List<String> =
        galleryResponse.posters.map { BuildConfig.BASE_IMAGE_URL +it.filePath }
}