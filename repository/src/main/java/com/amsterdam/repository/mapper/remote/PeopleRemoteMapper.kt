package com.amsterdam.repository.mapper.remote

import com.amsterdam.entity.People
import com.amsterdam.repository.dto.remote.RemotePeopleItemDto

fun RemotePeopleItemDto.toEntity() = People(
    id = this.id.toLong(),
    name = this.name,
    imageUrl = this.fullPosterUrl.orEmpty()
)

fun List<RemotePeopleItemDto>.toEntityList() = this.map(RemotePeopleItemDto::toEntity)