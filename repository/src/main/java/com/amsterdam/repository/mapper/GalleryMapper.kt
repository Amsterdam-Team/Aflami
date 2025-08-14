package com.amsterdam.repository.mapper

import com.amsterdam.repository.dto.remote.movieGallery.RemoteGalleryResponse

fun RemoteGalleryResponse.toEntityList(): List<String> {
    return this.backdrops.map { it.fullFilePath.orEmpty() }
}