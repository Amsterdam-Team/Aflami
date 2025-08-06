package com.amsterdam.repository.repository

import com.amsterdam.domain.repository.CountryRepository
import com.amsterdam.entity.Country
import com.amsterdam.repository.datasource.local.AppPreferences
import com.amsterdam.repository.datasource.local.CountryLocalSource
import com.amsterdam.repository.datasource.remote.CountryRemoteSource
import com.amsterdam.repository.dto.local.LocalCountryDto
import com.amsterdam.repository.dto.remote.RemoteCountryDto
import com.amsterdam.repository.mapper.local.toEntityList
import com.amsterdam.repository.mapper.remote.toEntityList
import com.amsterdam.repository.mapper.remoteToLocal.toLocalDtoList
import com.amsterdam.repository.repository.testFactory.testLocalCountryDto
import com.amsterdam.repository.repository.testFactory.testRemoteCountryDto
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coJustRun
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class CountryRepositoryImplTest {

    private lateinit var repository: CountryRepository

    private val localDataSource: CountryLocalSource = mockk()
    private val remoteDataSource: CountryRemoteSource = mockk()
    private val preferences: AppPreferences = mockk()

    private val testLanguage = "en"

    @BeforeEach
    fun setup() {
        repository = CountryRepositoryImpl(
            localDataSource = localDataSource,
            remoteDataSource = remoteDataSource,
            preferences = preferences
        )

        coEvery { preferences.getAppLanguage() } returns flowOf(testLanguage)
    }

    @Test
    fun `getCountries should return cached data if local data source is not empty`() = runTest {
        // Arrange
        val localCountries = listOf(testLocalCountryDto)
        val expectedCountries = localCountries.toEntityList()

        coEvery { localDataSource.getCountries(testLanguage) } returns localCountriesDto
        // When
        coEvery { localDataSource.getCountries(testLanguage) } returns localCountries

        // Act
        val result = repository.getCountries()

        // Assert
        assertThat(result).isEqualTo(expectedCountries)
        coVerify(exactly = 1) { preferences.getAppLanguage() }
        coVerify(exactly = 1) { localDataSource.getCountries(testLanguage) }
        coVerify(exactly = 0) { remoteDataSource.getCountries() }
        coVerify(exactly = 0) { localDataSource.addCountries(any()) }
    }

    @Test
    fun `getCountries should fetch from remote and cache if local data source is empty`() =
        runTest {
            // Given
            val remoteDto1 = RemoteCountryDto(
                englishName = "United States",
                isoCode = "US",
                nativeName = "الولايات المتحدة"
            )
            coEvery { localDataSource.getCountries(testLanguage) } returns emptyList()
            val remoteCountriesDto = listOf(remoteDto1)
            coEvery { remoteDataSource.getCountries() } returns remoteCountriesDto

            val localSavedDto1 = LocalCountryDto(
                isoCode = "US",
                name = "United States",
                storedLanguage = testLanguage
            )
            val localSavedCountriesDto = listOf(localSavedDto1)


            coJustRun { localDataSource.addCountries(localSavedCountriesDto) }
            // Arrange
            val remoteCountries = listOf(testRemoteCountryDto)
            val localSavedCountries = remoteCountries.toLocalDtoList(testLanguage)
            val expectedCountries = remoteCountries.toEntityList()

            coEvery { localDataSource.getCountries(testLanguage) } returns emptyList()
            coEvery { remoteDataSource.getCountries() } returns remoteCountries
            coJustRun { localDataSource.addCountries(localSavedCountries) }

            // Act
            val expectedCountry1 = Country(countryName = "United States", countryIsoCode = "US")
            val expectedCountries = listOf(expectedCountry1)

            // When
            val result = repository.getCountries()

            // Assert
            assertThat(result).isEqualTo(expectedCountries)
            coVerify(exactly = 2) { preferences.getAppLanguage() }
            coVerify(exactly = 1) { localDataSource.getCountries(testLanguage) }
            coVerify(exactly = 1) { remoteDataSource.getCountries() }
            coVerify(exactly = 1) { localDataSource.addCountries(localSavedCountries) }

            coVerify(exactly = 1) { localDataSource.addCountries(localSavedCountriesDto) }
        }

    @Test
    fun `getCountries handles local source exception by falling back to remote and caching`() =
        runTest {
            // Arrange
            val remoteCountries = listOf(testRemoteCountryDto)
            val localSavedCountries = remoteCountries.toLocalDtoList(testLanguage)
            val expectedCountries = remoteCountries.toEntityList()

            coEvery { localDataSource.getCountries(testLanguage) } throws Exception("Database error")
            coEvery { remoteDataSource.getCountries() } returns remoteCountries
            coJustRun { localDataSource.addCountries(localSavedCountries) }
            val localSavedDto1 =
                LocalCountryDto(isoCode = "CA", name = "Canada", storedLanguage = testLanguage)
            val localSavedCountriesDto = listOf(localSavedDto1)

            // Act
            coJustRun { localDataSource.addCountries(localSavedCountriesDto) }

            val expectedCountry1 = Country(countryName = "Canada", countryIsoCode = "CA")

            // When
            val result = repository.getCountries()

            // Assert
            assertThat(result).isEqualTo(expectedCountries)
            coVerify(exactly = 2) { preferences.getAppLanguage() }
            coVerify(exactly = 1) { localDataSource.getCountries(testLanguage) }
            coVerify(exactly = 1) { remoteDataSource.getCountries() }
            coVerify(exactly = 1) { localDataSource.addCountries(localSavedCountries) }

        }

    @Test
    fun `getCountries should throw exception if remote data source fails and local is empty`() =
        runTest {
            // Arrange
            val expectedException = RuntimeException("Network error")
            coEvery { localDataSource.getCountries(testLanguage) } returns emptyList()
            coEvery { remoteDataSource.getCountries() } throws expectedException

            // Act & Assert
            val thrownException = assertThrows<RuntimeException> {
                repository.getCountries()
            }

            assertThat(thrownException).isEqualTo(expectedException)
            coVerify(exactly = 1) { preferences.getAppLanguage() }
            coVerify(exactly = 1) { localDataSource.getCountries(testLanguage) }
            coVerify(exactly = 1) { remoteDataSource.getCountries() }
            coVerify(exactly = 0) { localDataSource.addCountries(any()) }
        }

    @Test
    fun `getCountries should throw exception if both local and remote sources fail`() = runTest {
        // Arrange
        val localException = RuntimeException("DB access denied!")
        val remoteException = RuntimeException("Server error!")
        coEvery { localDataSource.getCountries(testLanguage) } throws localException
        coEvery { remoteDataSource.getCountries() } throws remoteException

        // Act & Assert
        val thrownException = assertThrows<RuntimeException> {
            repository.getCountries()
        }

        assertThat(thrownException).isEqualTo(remoteException)
        coVerify(exactly = 1) { preferences.getAppLanguage() }
        coVerify(exactly = 1) { localDataSource.getCountries(testLanguage) }
        coVerify(exactly = 1) { remoteDataSource.getCountries() }
        coVerify(exactly = 0) { localDataSource.addCountries(any()) }
    }
}