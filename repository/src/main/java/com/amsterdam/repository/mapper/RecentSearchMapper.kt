package com.amsterdam.repository.mapper

import com.amsterdam.repository.dto.local.SearchLocalDto

fun SearchLocalDto.toEntity(): String = searchKeyword

fun List<SearchLocalDto>.toEntityList(): List<String> = map { it.toEntity() }