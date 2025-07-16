package com.example.repository.mapper.local

import com.example.repository.dto.local.LocalSearchDto

class RecentSearchMapper {

    fun toDomainList(recentSearches: List<LocalSearchDto>): List<String> {
        return recentSearches.map { searchKeyword -> toDomain(searchKeyword) }
    }

    private fun toDomain(recentSearch: LocalSearchDto): String {
        return recentSearch.searchKeyword
    }
}