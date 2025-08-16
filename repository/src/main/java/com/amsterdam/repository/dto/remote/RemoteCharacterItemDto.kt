package com.amsterdam.repository.dto.remote

import com.amsterdam.repository.utils.ImageBaseUrlsConstant.BASE_IMAGE_URL_W500
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RemoteCharacterItemDto(
    val adult: Boolean,
    val gender: Int,
    val id: Int,
    val name: String,
    val popularity: Double,
    @SerialName("media_type") val mediaType: String,
    @SerialName("original_name") val originalName: String,
    @SerialName("known_for_department") val knownForDepartment: String? = null,
    @SerialName("profile_path") val profilePath: String? = null
) {
    val fullPosterUrl: String?
        get() = profilePath?.let { BASE_IMAGE_URL_W500 + it }

}