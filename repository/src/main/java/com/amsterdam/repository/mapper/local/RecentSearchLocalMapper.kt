package com.amsterdam.repository.mapper.local

import com.amsterdam.repository.dto.local.LocalSearchDto
import com.amsterdam.repository.mapper.shared.EntityMapper
import javax.inject.Inject

class RecentSearchLocalMapper @Inject constructor(): EntityMapper<LocalSearchDto, String> {
    override fun toEntity(dto: LocalSearchDto): String {
        return dto.searchKeyword
    }
}