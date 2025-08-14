package com.amsterdam.localdatasource.dataStore

import com.amsterdam.domain.utils.RestrictionLevel
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

class AppPreferencesImplTest : BasePreferencesTest() {
    private val appPreferencesImpl by lazy {
        AppPreferencesImpl(dataStore)
    }

    @Test
    fun `getAppLanguage should return empty string when no value`() = runTest {
        val result = appPreferencesImpl.getAppLanguage()

        assertThat(result.first()).isEmpty()
    }

    @Test
    fun `getAppLanguage should return app language when has value`() = runTest {
        appPreferencesImpl.setAppLanguage(appLanguage)

        val result = appPreferencesImpl.getAppLanguage()

        assertThat(result.first()).isEqualTo(appLanguage)
    }

    @Test
    fun `setAppLanguage should save provided value when called`() = runTest {
        appPreferencesImpl.setAppLanguage(appLanguage)
        val result = appPreferencesImpl.getAppLanguage()

        assertThat(result.first()).isEqualTo(appLanguage)
    }

    @Test
    fun `getAppTheme should return true when no value`() = runTest {
        val result = appPreferencesImpl.getAppTheme()

        assertThat(result.first()).isTrue()
    }

    @Test
    fun `getAppTheme should return app theme when has value`() = runTest {
        appPreferencesImpl.setAppTheme(appTheme)

        val result = appPreferencesImpl.getAppTheme()

        assertThat(result.first()).isEqualTo(appTheme)
    }

    @Test
    fun `setAppTheme should save provided value when called`() = runTest {
        appPreferencesImpl.setAppTheme(appTheme)
        val result = appPreferencesImpl.getAppTheme()

        assertThat(result.first()).isEqualTo(appTheme)
    }

    @Test
    fun `getRestrictionLevel should return STRICT when no value`() = runTest {
        val result = appPreferencesImpl.getRestrictionLevel()

        assertThat(result.first()).isEqualTo(defaultRestrictLevel)
    }

    @Test
    fun `getRestrictionLevel should return restrict level when has value`() = runTest {
        appPreferencesImpl.setRestrictionLevel(restrictLevel)

        val result = appPreferencesImpl.getRestrictionLevel()

        assertThat(result.first()).isEqualTo(restrictLevel)
    }

    @Test
    fun `setRestrictionLevel should save provided value when called`() = runTest {
        appPreferencesImpl.setRestrictionLevel(restrictLevel)
        val result = appPreferencesImpl.getRestrictionLevel()

        assertThat(result.first()).isEqualTo(restrictLevel)
    }

    @Test
    fun `isOnboardingCompleted should return false when no value`() = runTest {
        val result = appPreferencesImpl.isOnboardingCompleted()

        assertThat(result).isFalse()
    }

    @Test
    fun `isOnboardingCompleted should return is onboarding completed when has value`() = runTest {
        appPreferencesImpl.setOnboardingCompleted(isOnboardingCompleted)

        val result = appPreferencesImpl.isOnboardingCompleted()

        assertThat(result).isEqualTo(isOnboardingCompleted)
    }

    @Test
    fun `setOnboardingCompleted should save provided value when called`() = runTest {
        appPreferencesImpl.setAppTheme(isOnboardingCompleted)
        val result = appPreferencesImpl.getAppTheme()

        assertThat(result.first()).isEqualTo(isOnboardingCompleted)
    }
}

private const val appLanguage = "en"
private const val appTheme = false
private val restrictLevel = RestrictionLevel.MODERATE.name
private val defaultRestrictLevel = RestrictionLevel.STRICT.name
private const val isOnboardingCompleted = true
