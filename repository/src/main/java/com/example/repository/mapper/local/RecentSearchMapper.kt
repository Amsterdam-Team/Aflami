package com.example.repository.mapper.local

import com.example.domain.mapper.DomainMapper
import com.example.repository.dto.local.LocalSearchDto

class RecentSearchMapper: DomainMapper<String, LocalSearchDto> {

    override fun toDomain(recentSearch: LocalSearchDto): String {
        return recentSearch.searchKeyword
    }
}