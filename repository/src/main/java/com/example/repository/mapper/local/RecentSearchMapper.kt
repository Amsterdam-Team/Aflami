package com.example.repository.mapper.local

import com.example.repository.dto.local.LocalSearchDto
import com.example.repository.dto.local.utils.SearchType
import kotlinx.datetime.Clock
import kotlin.time.Duration.Companion.hours

class RecentSearchMapper {

    fun toDomainList(recentSearches: List<LocalSearchDto>): List<String> {
        return recentSearches.map { searchKeyword -> toDomain(searchKeyword) }
    }

    fun toLocalSearch(keyword: String, searchType: SearchType): LocalSearchDto {
        return LocalSearchDto(
            searchKeyword = keyword,
            searchType = searchType,
            expireDate = Clock.System.now().plus(1.hours)
        )
    }

    private fun toDomain(recentSearch: LocalSearchDto): String {
        return recentSearch.searchKeyword
    }
}