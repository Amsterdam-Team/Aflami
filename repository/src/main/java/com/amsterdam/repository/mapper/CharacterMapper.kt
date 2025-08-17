package com.amsterdam.repository.mapper

import com.amsterdam.entity.Character
import com.amsterdam.repository.dto.remote.RemoteCharacterItemDto

fun RemoteCharacterItemDto.toEntity() = Character(
    id = this.id.toLong(),
    name = this.name,
    imageUrl = this.fullPosterUrl.orEmpty()
)

fun List<RemoteCharacterItemDto>.toEntityList() = this.map(RemoteCharacterItemDto::toEntity)