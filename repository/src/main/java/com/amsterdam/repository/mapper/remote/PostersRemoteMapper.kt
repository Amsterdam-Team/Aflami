package com.amsterdam.repository.mapper.remote

import com.amsterdam.repository.dto.remote.movieGallery.RemoteGalleryResponse
import com.amsterdam.repository.mapper.shared.EntityMapper
import com.amsterdam.repository.utils.ImageBaseUrlsConstant.BASE_IMAGE_URL_W500
import javax.inject.Inject

class PostersRemoteMapper @Inject constructor(): EntityMapper<RemoteGalleryResponse, List<String>> {

    override fun toEntity(dto: RemoteGalleryResponse): List<String> =
        dto.posters.map { BASE_IMAGE_URL_W500 + it.filePath }

}