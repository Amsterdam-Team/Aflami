package com.example.repository.mapper.remote

import com.example.entity.Actor
import com.example.entity.Gender
import com.example.repository.BuildConfig
import com.example.repository.dto.remote.RemoteCastDto

class RemoteCastMapper {

    fun mapToDomain(dto: RemoteCastDto): Actor{
        return Actor(
            id = dto.id,
            name = dto.name,
            imageUrl = BuildConfig.BASE_IMAGE_URL + dto.profilePath,
            popularity = dto.popularity,
            gender = mapIntToGender(dto.gender)
        )
    }

    private fun mapIntToGender(gender: Int) = if (gender == 2) Gender.Male else Gender.Female



}