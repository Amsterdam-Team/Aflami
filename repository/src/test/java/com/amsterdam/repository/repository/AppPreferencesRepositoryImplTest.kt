package com.amsterdam.repository.repository

import com.amsterdam.domain.repository.AppPreferencesRepository
import com.amsterdam.domain.utils.RestrictionLevel
import com.amsterdam.repository.datasource.local.AppLocalPreferences
import com.amsterdam.repository.mapper.toLocalDto
import com.google.common.truth.Truth.assertThat
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coJustRun
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class AppPreferencesRepositoryImplTest {

    private lateinit var repository: AppPreferencesRepository
    private val preferences: AppLocalPreferences = mockk()

    @BeforeEach
    fun setUp() {
        clearAllMocks()
        repository = AppPreferencesRepositoryImpl(preferences)
    }

    @Test
    fun `getAppLanguage should return flow from preferences`() = runTest {
        // Given
        val expectedLanguage = "en"
        every { preferences.getAppLanguage() } returns flowOf(expectedLanguage)

        // When
        val result = repository.getAppLanguage().first()

        // Then
        assertThat(result).isEqualTo(expectedLanguage)
        coVerify(exactly = 1) { preferences.getAppLanguage() }
    }

    @Test
    fun `setAppLanguage should call preferences to set language`() = runTest {
        // Given
        val language = "es"
        coJustRun { preferences.setAppLanguage(language) }

        // When
        repository.setAppLanguage(language)

        // Then
        coVerify(exactly = 1) { preferences.setAppLanguage(language) }
    }

    @Test
    fun `getAppTheme should return flow from preferences`() = runTest {
        // Given
        val expectedTheme = true
        every { preferences.getAppTheme() } returns flowOf(expectedTheme)

        // When
        val result = repository.getAppTheme().first()

        // Then
        assertThat(result).isEqualTo(expectedTheme)
        coVerify(exactly = 1) { preferences.getAppTheme() }
    }

    @Test
    fun `setAppTheme should call preferences to set theme`() = runTest {
        // Given
        val isDarkTheme = true
        coJustRun { preferences.setAppTheme(isDarkTheme) }

        // When
        repository.setAppTheme(isDarkTheme)

        // Then
        coVerify(exactly = 1) { preferences.setAppTheme(isDarkTheme) }
    }

    @Test
    fun `isOnboardingCompleted should return boolean from preferences`() = runTest {
        // Given
        val isCompleted = true
        coEvery { preferences.isOnboardingCompleted() } returns isCompleted

        // When
        val result = repository.isOnboardingCompleted()

        // Then
        assertThat(result).isEqualTo(isCompleted)
        coVerify(exactly = 1) { preferences.isOnboardingCompleted() }
    }

    @Test
    fun `setOnboardingCompleted should call preferences to set completion state`() = runTest {
        // Given
        val isCompleted = true
        coJustRun { preferences.setOnboardingCompleted(isCompleted) }

        // When
        repository.setOnboardingCompleted(isCompleted)

        // Then
        coVerify(exactly = 1) { preferences.setOnboardingCompleted(isCompleted) }
    }

    @Test
    fun `getRestrictionLevel should return mapped restriction level from preferences`() = runTest {
        // Given
        val localDto = "OFF"
        val expectedLevel = RestrictionLevel.OFF
        every { preferences.getRestrictionLevel() } returns flowOf(localDto)

        // When
        val result = repository.getRestrictionLevel().first()

        // Then
        assertThat(result).isEqualTo(expectedLevel)
        coVerify(exactly = 1) { preferences.getRestrictionLevel() }
    }

    @Test
    fun `setRestrictionLevel should call preferences with mapped DTO`() = runTest {
        // Given
        val restrictionLevel = RestrictionLevel.STRICT
        val localDto = restrictionLevel.toLocalDto()
        coJustRun { preferences.setRestrictionLevel(localDto) }

        // When
        repository.setRestrictionLevel(restrictionLevel)

        // Then
        coVerify(exactly = 1) { preferences.setRestrictionLevel(localDto) }
    }
}
