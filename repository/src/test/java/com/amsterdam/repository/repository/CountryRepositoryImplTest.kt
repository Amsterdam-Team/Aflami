package com.amsterdam.repository.repository

import com.amsterdam.domain.repository.CountryRepository
import com.amsterdam.entity.Country
import com.amsterdam.repository.datasource.local.CountryLocalSource
import com.amsterdam.repository.datasource.remote.CountryRemoteSource
import com.amsterdam.repository.dto.local.LocalCountryDto
import com.amsterdam.repository.dto.remote.RemoteCountryDto
import com.amsterdam.repository.mapper.local.CountryLocalMapper
import com.amsterdam.repository.mapper.remote.CountryRemoteMapper
import com.amsterdam.repository.mapper.remoteToLocal.CountryRemoteLocalMapper
import com.google.common.truth.Truth.assertThat
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coJustRun
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class CountryRepositoryImplTest {

    private lateinit var repository: CountryRepository

    private val localDataSource: CountryLocalSource = mockk()
    private val remoteDataSource: CountryRemoteSource = mockk()
    private val localMapper: CountryLocalMapper = mockk()
    private val remoteMapper: CountryRemoteMapper = mockk()
    private val countryRemoteLocalMapper: CountryRemoteLocalMapper = mockk()

    private val testLanguage = "en"

    @BeforeEach
    fun setup() {
        clearAllMocks()
        repository = CountryRepositoryImpl(
            localDataSource = localDataSource,
            remoteDataSource = remoteDataSource,
            countryRemoteMapper = remoteMapper,
            countryLocalMapper = localMapper,
            countryRemoteLocalMapper = countryRemoteLocalMapper
        )

        every { localMapper.toEntityList(emptyList()) } returns emptyList()
    }

    @Test
    fun `getCountries should return countries from local when not empty`() = runTest {
        // Given
        val localDto1 =
            LocalCountryDto(isoCode = "EG", name = "Egypt", storedLanguage = testLanguage)
        val localCountriesDto = listOf(localDto1)
        val expectedCountry1 = Country(countryName = "Egypt", countryIsoCode = "EG")
        val expectedCountries = listOf(expectedCountry1)

        coEvery { localDataSource.getCountries(testLanguage) } returns localCountriesDto
        every { localMapper.toEntityList(localCountriesDto) } returns expectedCountries

        // When
        val result = repository.getCountries()

        // Then
        assertThat(result).isEqualTo(expectedCountries)
        coVerify(exactly = 1) { localDataSource.getCountries(testLanguage) }
        verify(exactly = 1) { localMapper.toEntityList(localCountriesDto) }
        coVerify(exactly = 0) { remoteDataSource.getCountries() }
        coVerify(exactly = 0) { localDataSource.addCountries(any()) }
    }

    @Test
    fun `getCountries should fetch countries from remote and cache them when local is empty`() =
        runTest {
            // Given
            coEvery { localDataSource.getCountries(testLanguage) } returns emptyList()

            val remoteDto1 = RemoteCountryDto(
                englishName = "United States",
                isoCode = "US",
                nativeName = "الولايات المتحدة"
            )
            val remoteCountriesDto = listOf(remoteDto1)
            coEvery { remoteDataSource.getCountries() } returns remoteCountriesDto

            val localSavedDto1 = LocalCountryDto(
                isoCode = "US",
                name = "United States",
                storedLanguage = testLanguage
            )
            val localSavedCountriesDto = listOf(localSavedDto1)
            every {
                countryRemoteLocalMapper.toLocalList(
                    remoteCountriesDto,
                    listOf(testLanguage)
                )
            } returns localSavedCountriesDto

            coJustRun { localDataSource.addCountries(localSavedCountriesDto) }

            val expectedCountry1 = Country(countryName = "United States", countryIsoCode = "US")
            val expectedCountries = listOf(expectedCountry1)
            every { remoteMapper.toEntityList(remoteCountriesDto) } returns expectedCountries

            // When
            val result = repository.getCountries()

            // Then
            assertThat(result).isEqualTo(expectedCountries)

            // Verifications
            coVerify(exactly = 1) { localDataSource.getCountries(testLanguage) }
            coVerify(exactly = 1) { remoteDataSource.getCountries() }
            verify(exactly = 1) {
                countryRemoteLocalMapper.toLocalList(
                    remoteCountriesDto,
                    listOf(testLanguage)
                )
            }
            coVerify(exactly = 1) { localDataSource.addCountries(localSavedCountriesDto) }
            verify(exactly = 1) { remoteMapper.toEntityList(remoteCountriesDto) }
            verify(exactly = 1) { localMapper.toEntityList(emptyList()) }
        }

    @Test
    fun `getCountries should handle local source exception by falling back to remote and caching`() =
        runTest {
            // Given
            val localException = RuntimeException("Local DB corrupted!")
            coEvery { localDataSource.getCountries(testLanguage) } throws localException

            val remoteDto1 =
                RemoteCountryDto(englishName = "Canada", isoCode = "CA", nativeName = "Canada")
            val remoteCountriesDto = listOf(remoteDto1)
            coEvery { remoteDataSource.getCountries() } returns remoteCountriesDto

            val localSavedDto1 =
                LocalCountryDto(isoCode = "CA", name = "Canada", storedLanguage = testLanguage)
            val localSavedCountriesDto = listOf(localSavedDto1)
            every {
                countryRemoteLocalMapper.toLocalList(
                    remoteCountriesDto,
                    listOf(testLanguage)
                )
            } returns localSavedCountriesDto

            coJustRun { localDataSource.addCountries(localSavedCountriesDto) }

            val expectedCountry1 = Country(countryName = "Canada", countryIsoCode = "CA")
            val expectedCountries = listOf(expectedCountry1)
            every { remoteMapper.toEntityList(remoteCountriesDto) } returns expectedCountries

            // When
            val result = repository.getCountries()

            // Then
            assertThat(result).isEqualTo(expectedCountries)

            coVerify(exactly = 1) { localDataSource.getCountries(testLanguage) }
            coVerify(exactly = 1) { remoteDataSource.getCountries() }
            verify(exactly = 1) {
                countryRemoteLocalMapper.toLocalList(
                    remoteCountriesDto,
                    listOf(testLanguage)
                )
            }
            coVerify(exactly = 1) { localDataSource.addCountries(localSavedCountriesDto) }
            verify(exactly = 1) { remoteMapper.toEntityList(remoteCountriesDto) }
        }

    @Test
    fun `getCountries should throw exception if remote source fails and local is empty`() =
        runTest {
            // Given
            coEvery { localDataSource.getCountries(testLanguage) } returns emptyList()

            val remoteException = RuntimeException("Network unavailable!")
            coEvery { remoteDataSource.getCountries() } throws remoteException

            // When & Then
            val thrownException = assertThrows<RuntimeException> {
                repository.getCountries()
            }

            assertThat(thrownException).isEqualTo(remoteException)

            coVerify(exactly = 1) { localDataSource.getCountries(testLanguage) }
            coVerify(exactly = 1) { remoteDataSource.getCountries() }
            coVerify(exactly = 0) { localDataSource.addCountries(any()) }
            verify(exactly = 0) { countryRemoteLocalMapper.toLocalList(any(), any()) }
            verify(exactly = 0) { remoteMapper.toEntityList(any()) }
        }

    @Test
    fun `getCountries should throw exception if local source fails and remote also fails`() =
        runTest {
            // Given
            val localException = RuntimeException("DB access denied!")
            coEvery { localDataSource.getCountries(testLanguage) } throws localException

            val remoteException = RuntimeException("Server error!")
            coEvery { remoteDataSource.getCountries() } throws remoteException

            // When & Then
            val thrownException = assertThrows<RuntimeException> {
                repository.getCountries()
            }

            assertThat(thrownException).isEqualTo(remoteException)

            coVerify(exactly = 1) { localDataSource.getCountries(testLanguage) }
            coVerify(exactly = 1) { remoteDataSource.getCountries() }
            coVerify(exactly = 0) { localDataSource.addCountries(any()) }
            verify(exactly = 0) { countryRemoteLocalMapper.toLocalList(any(), any()) }
            verify(exactly = 0) { remoteMapper.toEntityList(any()) }
        }
}