package com.amsterdam.repository.dto.remote.profile

import com.amsterdam.repository.utils.ImageBaseUrlsConstant.BASE_IMAGE_URL_W500
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AccountDetailsDto(
    @SerialName("avatar") val accountAvatar: AccountAvatar,
    @SerialName("id") val id: Int,
    @SerialName("include_adult") val includeAdult: Boolean,
    @SerialName("iso_3166_1") val countryIsoCode: String,
    @SerialName("iso_639_1") val languageCode: String,
    @SerialName("name") val name: String,
    @SerialName("username") val username: String
) {
    @Serializable
    data class AccountAvatar(
        @SerialName("gravatar") val gravatar: Gravatar,
        @SerialName("tmdb") val movieDBData: MovieDBData
    )

    @Serializable
    data class Gravatar(
        @SerialName("hash") val hash: String
    )

    @Serializable
    data class MovieDBData(
        @SerialName("avatar_path") val avatarPath: String?
    ) {
        val fullAvatarPath: String?
            get() = avatarPath?.let { BASE_IMAGE_URL_W500 + it }
    }
}