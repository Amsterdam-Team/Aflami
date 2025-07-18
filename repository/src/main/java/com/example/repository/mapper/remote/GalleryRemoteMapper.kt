package com.example.repository.mapper.remote

import com.example.repository.BuildConfig
import com.example.repository.dto.remote.movieGallery.RemoteGalleryResponse

class GalleryRemoteMapper {

    fun mapGalleryToDomain(galleryResponse: RemoteGalleryResponse) : List<String> =
        galleryResponse.posters.map { BuildConfig.BASE_IMAGE_URL +it.filePath }

}