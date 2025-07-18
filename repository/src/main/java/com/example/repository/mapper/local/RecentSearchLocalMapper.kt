package com.example.repository.mapper.local

import com.example.repository.dto.local.LocalSearchDto
import com.example.repository.mapper.shared.EntityMapper

class RecentSearchLocalMapper : EntityMapper<LocalSearchDto, String> {
    override fun toEntity(dto: LocalSearchDto): String {
        return dto.searchKeyword
    }
}