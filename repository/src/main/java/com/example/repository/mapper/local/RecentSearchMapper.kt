package com.example.repository.mapper.local

import com.example.repository.dto.local.LocalSearchDto

class RecentSearchMapper {
    private fun toDomain(recentSearchDto: LocalSearchDto): String {
        return recentSearchDto.searchKeyword
    }

    fun toDomainList(recentSearches: List<LocalSearchDto>): List<String> {
        return recentSearches.map { searchKeyword -> toDomain(searchKeyword) }
    }
}