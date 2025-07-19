package com.example.repository.mapper.shared

interface DtoMapper <Entity, Dto>{
    fun toDto(entity: Entity): Dto
    fun toDtoList(entityList: List<Entity>): List<Dto> = entityList.map (::toDto)
}