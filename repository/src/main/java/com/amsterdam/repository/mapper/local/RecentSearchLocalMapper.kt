package com.amsterdam.repository.mapper.local

import com.amsterdam.repository.dto.local.LocalSearchDto

fun LocalSearchDto.toTvShowEntity(): String =
    searchKeyword

fun List<LocalSearchDto>.toEntityList(): List<String> = map { it.toTvShowEntity() }