package com.example.repository.mapper.local

import com.example.repository.dto.local.LocalSearchDto

fun LocalSearchDto.toDomain(): String {
    return this.searchKeyword
}

fun List<LocalSearchDto>.toDomainList(): List<String> {
    return this.map { it.toDomain() }
}
