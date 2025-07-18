package com.example.repository.mapper.shared

interface DtoMapper <Domain, Dto>{
    fun toDto(domain: Domain): Dto
    fun toDtoList(domainList: List<Domain>): List<Dto> = domainList.map (::toDto)
}