package com.amsterdam.repository.repository

import com.amsterdam.domain.repository.AppPreferencesRepository
import com.amsterdam.domain.utils.RestrictionLevel
import com.amsterdam.repository.datasource.local.AppPreferences
import com.amsterdam.repository.mapper.local.toLocalDto
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

    private val preferences: AppPreferences = mockk()

    @BeforeEach
    fun setUp() {
        clearAllMocks()
        repository = AppPreferencesRepositoryImpl(preferences)
    }

    @Test
    fun `getAppLanguage should return flow from preferences`() = runTest {
        // Arrange
        val expectedLanguage = "en"
        every { preferences.getAppLanguage() } returns flowOf(expectedLanguage)

        // Act
        val result = repository.getAppLanguage().first()

        // Assert
        assertThat(result).isEqualTo(expectedLanguage)
        coVerify(exactly = 1) { preferences.getAppLanguage() }
    }

    @Test
    fun `setAppLanguage should call preferences to set language`() = runTest {
        // Arrange
        val language = "es"
        coJustRun { preferences.setAppLanguage(language) }

        // Act
        repository.setAppLanguage(language)

        // Assert
        coVerify(exactly = 1) { preferences.setAppLanguage(language) }
    }

    @Test
    fun `getAppTheme should return flow from preferences`() = runTest {
        // Arrange
        val expectedTheme = true
        every { preferences.getAppTheme() } returns flowOf(expectedTheme)

        // Act
        val result = repository.getAppTheme().first()

        // Assert
        assertThat(result).isEqualTo(expectedTheme)
        coVerify(exactly = 1) { preferences.getAppTheme() }
    }

    @Test
    fun `setAppTheme should call preferences to set theme`() = runTest {
        // Arrange
        val isDarkTheme = true
        coJustRun { preferences.setAppTheme(isDarkTheme) }

        // Act
        repository.setAppTheme(isDarkTheme)

        // Assert
        coVerify(exactly = 1) { preferences.setAppTheme(isDarkTheme) }
    }

    @Test
    fun `isOnboardingCompleted should return boolean from preferences`() = runTest {
        // Arrange
        val isCompleted = true
        coEvery { preferences.isOnboardingCompleted() } returns isCompleted

        // Act
        val result = repository.isOnboardingCompleted()

        // Assert
        assertThat(result).isEqualTo(isCompleted)
        coVerify(exactly = 1) { preferences.isOnboardingCompleted() }
    }

    @Test
    fun `setOnboardingCompleted should call preferences to set completion state`() = runTest {
        // Arrange
        val isCompleted = true
        coJustRun { preferences.setOnboardingCompleted(isCompleted) }

        // Act
        repository.setOnboardingCompleted(isCompleted)

        // Assert
        coVerify(exactly = 1) { preferences.setOnboardingCompleted(isCompleted) }
    }

    @Test
    fun `getRestrictionLevel should return mapped restriction level from preferences`() = runTest {
        // Arrange
        val localDto = "OFF"
        val expectedLevel = RestrictionLevel.OFF
        every { preferences.getRestrictionLevel() } returns flowOf(localDto)

        // Act
        val result = repository.getRestrictionLevel().first()

        // Assert
        assertThat(result).isEqualTo(expectedLevel)
        coVerify(exactly = 1) { preferences.getRestrictionLevel() }
    }

    @Test
    fun `setRestrictionLevel should call preferences with mapped DTO`() = runTest {
        // Arrange
        val restrictionLevel = RestrictionLevel.STRICT
        val localDto = restrictionLevel.toLocalDto()
        coJustRun { preferences.setRestrictionLevel(localDto) }

        // Act
        repository.setRestrictionLevel(restrictionLevel)

        // Assert
        coVerify(exactly = 1) { preferences.setRestrictionLevel(localDto) }
    }
}