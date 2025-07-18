package com.example.domain.mapper

interface DomainMapper<domain, dto> {
    fun toDomain(dto: dto): domain
    fun toDomainList(dtos: List<dto>): List<domain> = dtos.map (::toDomain)
}