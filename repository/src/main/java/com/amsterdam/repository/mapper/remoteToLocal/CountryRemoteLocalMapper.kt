package com.amsterdam.repository.mapper.remoteToLocal

import com.amsterdam.repository.dto.local.LocalCountryDto
import com.amsterdam.repository.dto.remote.RemoteCountryDto
import com.amsterdam.repository.mapper.shared.RemoteToLocalMapper
import javax.inject.Inject

class CountryRemoteLocalMapper @Inject constructor(): RemoteToLocalMapper<RemoteCountryDto, LocalCountryDto> {
    override fun toLocal(remote: RemoteCountryDto, args: List<Any>): LocalCountryDto {
        return LocalCountryDto(
            name = remote.nativeName,
            storedLanguage = args.first().toString(),
            isoCode = remote.isoCode
        )
    }
}