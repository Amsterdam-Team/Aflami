package com.example.repository.repository

import com.example.entity.Country
import com.example.repository.datasource.local.CountryLocalSource
import com.example.repository.datasource.remote.CountryRemoteSource
import com.example.repository.dto.local.LocalCountryDto
import com.example.repository.dto.remote.RemoteCountryDto
import com.example.repository.mapper.local.CountryLocalMapper
import com.example.repository.mapper.remote.RemoteCountryMapper
import com.google.common.truth.Truth.assertThat
import io.mockk.*
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class CountryRepositoryImplTest {

    private lateinit var repository: CountryRepositoryImpl

    private val localDataSource: CountryLocalSource = mockk()
    private val remoteDataSource: CountryRemoteSource = mockk()
    private val localMapper: CountryLocalMapper = mockk()
    private val remoteMapper: RemoteCountryMapper = mockk()

    @BeforeEach
    fun setup() {
        repository = CountryRepositoryImpl(
            localDataSource = localDataSource,
            remoteDataSource = remoteDataSource,
            remoteCountryMapper = remoteMapper,
            localCountryMapper = localMapper
        )
    }

    @Test
    fun `should return countries from local when not empty`() = runTest {
        // Given
        val localDto = listOf(LocalCountryDto("EG", "Egypt"))
        val expected = listOf(Country("Egypt", "EG"))

        coEvery { localDataSource.getCountries() } returns localDto
        every { localMapper.mapToCountry(localDto[0]) } returns expected[0]

        // When
        val result = repository.getAllCountries()

        // Then
        assertThat(result).isEqualTo(expected)
        coVerify(exactly = 0) { remoteDataSource.getCountries() }
    }

    @Test
    fun `should fetch countries from remote and cache them when local is empty`() = runTest {
        // Given
        coEvery { localDataSource.getCountries() } returns emptyList()

        val remoteDto = RemoteCountryDto(
            englishName = "United States",
            isoCode = "US",
            nativeName = "الولايات المتحدة"
        )

        val domainModel = Country(
            countryName = "United States",
            countryIsoCode = "US"
        )

        val localDto = LocalCountryDto(
            isoCode = "US",
            name = "United States"
        )

        coEvery { remoteDataSource.getCountries() } returns listOf(remoteDto)
        every { remoteMapper.mapToCountry(remoteDto) } returns domainModel
        every { localMapper.mapToLocalCountry(domainModel) } returns localDto
        coEvery { localDataSource.addCountries(listOf(localDto)) } just Runs

        // When
        val result = repository.getAllCountries()

        // Then
        assertThat(result).isEqualTo(listOf(domainModel))
        coVerify { remoteDataSource.getCountries() }
        coVerify { localDataSource.addCountries(listOf(localDto)) }
    }
}
