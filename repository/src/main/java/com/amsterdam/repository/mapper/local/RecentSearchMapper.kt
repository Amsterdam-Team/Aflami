package com.amsterdam.repository.mapper.local

import com.amsterdam.repository.dto.local.LocalSearchDto

class RecentSearchMapper {
    private fun toDomain(recentSearchDto: LocalSearchDto): String {
        return recentSearchDto.searchKeyword
    }

    fun toDomainList(recentSearches: List<LocalSearchDto>): List<String> {
        return recentSearches.map { searchKeyword -> toDomain(searchKeyword) }
    }
}