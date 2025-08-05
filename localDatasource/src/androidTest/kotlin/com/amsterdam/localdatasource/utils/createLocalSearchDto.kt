package com.amsterdam.localdatasource.utils

import com.amsterdam.repository.dto.local.LocalSearchDto
import com.amsterdam.repository.dto.local.utils.SearchType
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlin.time.Duration.Companion.milliseconds

fun createLocalSearchDto(
    keyword: String,
    dateAdded: Instant = Clock.System.now(),
): LocalSearchDto {
    return LocalSearchDto(
        searchKeyword = keyword,
        dateAdded = dateAdded
    )
}