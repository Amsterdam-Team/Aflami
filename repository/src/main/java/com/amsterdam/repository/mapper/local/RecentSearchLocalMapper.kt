package com.amsterdam.repository.mapper.local

import com.amsterdam.repository.dto.local.LocalSearchDto

fun LocalSearchDto.toEntity(): String =
    searchKeyword

fun List<LocalSearchDto>.toEntityList(): List<String> = map { it.toEntity() }