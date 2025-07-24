package com.example.repository.utils

import com.example.repository.dto.local.LocalSearchDto
import com.example.repository.dto.local.utils.SearchType
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant

fun createLocalSearchDto(
    searchKeyword: String = "keyword",
    searchType: SearchType = SearchType.BY_KEYWORD,
    storedLanguage: String = "en",
    expireDate: Instant = Clock.System.now()
) = LocalSearchDto(
    searchKeyword = searchKeyword,
    searchType = searchType,
    storedLanguage = storedLanguage,
    expireDate = expireDate
)