package com.example.domain.mapper

interface DtoMapper <domain, dto>{
    fun toDto(domain: domain): dto
    fun toDtoList(domains: List<domain>): List<dto> = domains.map (::toDto)
}