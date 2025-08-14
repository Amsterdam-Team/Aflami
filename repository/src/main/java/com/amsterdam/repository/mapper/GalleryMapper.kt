package com.amsterdam.repository.mapper

import com.amsterdam.repository.dto.remote.movieGallery.GalleryRemoteResponse

fun GalleryRemoteResponse.toEntityList(): List<String> {
    return this.backdrops.map { it.fullFilePath.orEmpty() }
}