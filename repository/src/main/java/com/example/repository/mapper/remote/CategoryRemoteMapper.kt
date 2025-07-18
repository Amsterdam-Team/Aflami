package com.example.repository.mapper.remote

import com.example.entity.Category
import com.example.repository.dto.remote.RemoteCategoryDto
import com.example.repository.mapper.shared.EntityMapper

class CategoryRemoteMapper : EntityMapper<RemoteCategoryDto, Category> {
    override fun toEntity(dto: RemoteCategoryDto): Category {
        return Category(
            id = dto.id,
            name = dto.name,
            imageUrl = ""
        )
    }
}