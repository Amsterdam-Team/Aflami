package com.amsterdam.repository.dto.remote

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class VideoDto(
    @SerialName("iso_639_1")
    val languageCode: String,

    @SerialName("iso_3166_1")
    val countryCode: String,

    @SerialName("name")
    val name: String,

    @SerialName("key")
    val key: String,

    @SerialName("site")
    val site: String,

    @SerialName("size")
    val size: Int,

    @SerialName("type")
    val type: String,

    @SerialName("official")
    val official: Boolean,

    @SerialName("published_at")
    val publishedAt: String,

    @SerialName("id")
    val id: String
){
    val fullVideoUrl: String?
        get() = YOUTUBE_BASE_URL + key

    private companion object {
        const val YOUTUBE_BASE_URL = "https://www.youtube.com/watch?v="
    }
}