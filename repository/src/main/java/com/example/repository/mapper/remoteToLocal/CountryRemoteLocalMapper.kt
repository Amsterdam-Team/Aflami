package com.example.repository.mapper.remoteToLocal

import com.example.repository.dto.local.LocalCountryDto
import com.example.repository.dto.remote.RemoteCountryDto
import com.example.repository.mapper.shared.RemoteToLocalMapper

class CountryRemoteLocalMapper: RemoteToLocalMapper<RemoteCountryDto, LocalCountryDto> {
    override fun toLocal(remote: RemoteCountryDto, args: List<Any>): LocalCountryDto {
        return LocalCountryDto(
            name = remote.nativeName,
            storedLanguage = args[0].toString(),
            isoCode = remote.isoCode
        )
    }
}