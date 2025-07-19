package com.example.repository.mapper.remote

import com.example.entity.Actor
import com.example.entity.Gender
import com.example.repository.dto.remote.RemoteCastDto
import com.example.repository.mapper.shared.EntityMapper

class CastRemoteMapper : EntityMapper<RemoteCastDto, Actor> {
    override fun toEntity(dto: RemoteCastDto): Actor {
        return Actor(
            id = dto.id,
            name = dto.name,
            imageUrl = dto.fullProfilePath.orEmpty(),
            popularity = dto.popularity,
            gender = mapIntToGender(dto.gender)
        )
    }

    private fun mapIntToGender(gender: Int) = if (gender == 2) Gender.Male else Gender.Female
}