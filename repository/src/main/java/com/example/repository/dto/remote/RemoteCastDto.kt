package com.example.repository.dto.remote

import com.example.repository.BuildConfig
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RemoteCastDto(
    @SerialName("adult")
    val adult: Boolean,
    
    @SerialName("gender")
    val gender: Int,
    
    @SerialName("id")
    val id: Long,
    
    @SerialName("known_for_department")
    val knownForDepartment: String,
    
    @SerialName("name")
    val name: String,
    
    @SerialName("original_name")
    val originalName: String,
    
    @SerialName("popularity")
    val popularity: Double,
    
    @SerialName("profile_path")
    val profilePath: String? = null,
    
    @SerialName("cast_id")
    val castId: Int,
    
    @SerialName("character")
    val character: String,
    
    @SerialName("credit_id")
    val creditId: String,
    
    @SerialName("order")
    val order: Int
){
    val fullProfilePath: String?
        get() = profilePath.let { BuildConfig.BASE_IMAGE_URL + it }
}