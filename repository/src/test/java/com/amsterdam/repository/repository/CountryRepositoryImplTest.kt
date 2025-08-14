package com.amsterdam.repository.repository

import com.amsterdam.domain.repository.CountryRepository
import com.amsterdam.entity.Country
import com.amsterdam.repository.datasource.local.AppPreferences
import com.amsterdam.repository.datasource.local.CountryLocalDataSource
import com.amsterdam.repository.datasource.remote.CountryRemoteDataSource
import com.amsterdam.repository.dto.local.CountryLocalDto
import com.amsterdam.repository.dto.remote.CountryRemoteDto
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

    private val localDataSource: CountryLocalDataSource = mockk()
    private val remoteDataSource: CountryRemoteDataSource = mockk()
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
    fun `Given local countries not empty, When getCountries is called, Then return local countries without calling remote`() =
        runTest {
            // Given
            val localDto1 = CountryLocalDto(
                isoCode = "US",
                name = "United States",
                storedLanguage = testLanguage
            )
            val localDto2 =
                CountryLocalDto(isoCode = "CA", name = "Canada", storedLanguage = testLanguage)
            val localDtos = listOf(localDto1, localDto2)
            coEvery { localDataSource.getCountries(testLanguage) } returns localDtos


            val expectedCountries = listOf(
                Country(countryName = "United States", countryIsoCode = "US"),
                Country(countryName = "Canada", countryIsoCode = "CA")
            )

            // When
            val result = repository.getCountries()

            // Then
            assertThat(result).isEqualTo(expectedCountries)
            coVerify(exactly = 1) { localDataSource.getCountries(testLanguage) }
            coVerify(exactly = 0) { remoteDataSource.getCountries() }
            coVerify(exactly = 0) { localDataSource.upsertCountries(any()) }
        }

    @Test
    fun `getCountries should fetch countries from remote and cache them when local is empty`() =
        runTest {
            // Given
            val remoteDto1 = CountryRemoteDto(
                englishName = "United States",
                isoCode = "US",
                nativeName = "الولايات المتحدة"
            )
            coEvery { localDataSource.getCountries(testLanguage) } returns emptyList()
            val remoteCountriesDto = listOf(remoteDto1)
            coEvery { remoteDataSource.getCountries() } returns remoteCountriesDto

            val localSavedDto1 = CountryLocalDto(
                isoCode = "US",
                name = "الولايات المتحدة",
                storedLanguage = testLanguage
            )
            val localSavedCountriesDto = listOf(localSavedDto1)

            coJustRun { localDataSource.upsertCountries(localSavedCountriesDto) }

            val expectedCountry1 = Country(countryName = "الولايات المتحدة", countryIsoCode = "US")
            val expectedCountries = listOf(expectedCountry1)

            // When
            val result = repository.getCountries()

            // Then
            assertThat(result).isEqualTo(expectedCountries)

            coVerify(exactly = 1) { localDataSource.getCountries(testLanguage) }
            coVerify(exactly = 1) { remoteDataSource.getCountries() }
            coVerify(exactly = 1) { localDataSource.upsertCountries(localSavedCountriesDto) }
        }

    @Test
    fun `getCountries should handle local source exception by falling back to remote and caching`() =
        runTest {
            // Given
            val localException = RuntimeException("Local DB corrupted!")
            coEvery { localDataSource.getCountries(testLanguage) } throws localException

            val remoteDto1 =
                CountryRemoteDto(englishName = "Canada", isoCode = "CA", nativeName = "كندا")
            val remoteCountriesDto = listOf(remoteDto1)
            coEvery { remoteDataSource.getCountries() } returns remoteCountriesDto

            val localSavedDto1 =
                CountryLocalDto(isoCode = "CA", name = "كندا", storedLanguage = testLanguage)
            val localSavedCountriesDto = listOf(localSavedDto1)

            coJustRun { localDataSource.upsertCountries(localSavedCountriesDto) }

            val expectedCountry1 = Country(countryName = "كندا", countryIsoCode = "CA")
            val expectedCountries = listOf(expectedCountry1)

            // When
            val result = repository.getCountries()

            // Then
            assertThat(result).isEqualTo(expectedCountries)

            coVerify(exactly = 1) { localDataSource.getCountries(testLanguage) }
            coVerify(exactly = 1) { remoteDataSource.getCountries() }

            coVerify(exactly = 1) { localDataSource.upsertCountries(localSavedCountriesDto) }
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
            coVerify(exactly = 0) { localDataSource.upsertCountries(any()) }
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
            coVerify(exactly = 0) { localDataSource.upsertCountries(any()) }
        }
}