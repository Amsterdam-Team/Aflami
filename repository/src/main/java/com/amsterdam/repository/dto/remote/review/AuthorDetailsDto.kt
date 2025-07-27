package com.amsterdam.repository.dto.remote.review

import com.amsterdam.repository.utils.ImageBaseUrlsConstant.BASE_IMAGE_URL_W500
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AuthorDetailsDto(
    @SerialName("name")
    val name: String? = null,
    @SerialName("username")
    val username: String,
    @SerialName("avatar_path")
    val avatarPath: String? = null,
    @SerialName("rating")
    val rating: Float? = null
){
    val fullAvatarPath: String?
        get() = avatarPath.let { BASE_IMAGE_URL_W500 + it }
}