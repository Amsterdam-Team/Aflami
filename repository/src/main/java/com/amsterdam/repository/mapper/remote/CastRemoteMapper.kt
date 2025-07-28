package com.amsterdam.repository.mapper.remote

import com.amsterdam.entity.Actor
import com.amsterdam.entity.Gender
import com.amsterdam.repository.dto.remote.RemoteCastDto
import com.amsterdam.repository.mapper.shared.EntityMapper
import javax.inject.Inject

class CastRemoteMapper @Inject constructor() : EntityMapper<RemoteCastDto, Actor> {
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