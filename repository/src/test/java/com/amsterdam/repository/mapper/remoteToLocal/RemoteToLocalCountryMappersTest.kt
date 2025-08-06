package com.amsterdam.repository.mapper.remoteToLocal

import com.amsterdam.repository.mapper.remoteToLocal.testFactory.createRemoteCountryDto
import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test


class RemoteToLocalCountryMappersTest {

    @Test
    fun `should map a single RemoteCountryDto to LocalCountryDto correctly`() {
        // Arrange
        val remoteDto = createRemoteCountryDto(
            englishName = "Canada",
            isoCode = "CA",
            nativeName = "Canada"
        )
        val storedLanguage = "en"

        // Act
        val localDto = remoteDto.toLocalDto(storedLanguage)

        // Assert
        assertThat(localDto.isoCode).isEqualTo("CA")
        assertThat(localDto.name).isEqualTo("Canada")
        assertThat(localDto.storedLanguage).isEqualTo("en")
    }

    @Test
    fun `should map a list of RemoteCountryDto to a list of LocalCountryDto correctly`() {
        // Arrange
        val remoteDtoList = listOf(
            createRemoteCountryDto(
                englishName = "Brazil",
                isoCode = "BR",
                nativeName = "Brasil"
            ),
            createRemoteCountryDto(
                englishName = "Japan",
                isoCode = "JP",
                nativeName = "日本"
            )
        )
        val storedLanguage = "es"

        // Act
        val localDtoList = remoteDtoList.toLocalDtoList(storedLanguage)

        // Assert
        assertThat(localDtoList).hasSize(2)

        val firstCountry = localDtoList[0]
        assertThat(firstCountry.isoCode).isEqualTo("BR")
        assertThat(firstCountry.name).isEqualTo("Brasil")
        assertThat(firstCountry.storedLanguage).isEqualTo("es")

        val secondCountry = localDtoList[1]
        assertThat(secondCountry.isoCode).isEqualTo("JP")
        assertThat(secondCountry.name).isEqualTo("日本")
        assertThat(secondCountry.storedLanguage).isEqualTo("es")
    }
}