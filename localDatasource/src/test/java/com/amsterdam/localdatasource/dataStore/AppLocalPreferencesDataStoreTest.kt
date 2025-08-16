package com.amsterdam.localdatasource.dataStore

import com.amsterdam.domain.utils.RestrictionLevel
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

class AppLocalPreferencesDataStoreTest : BasePreferencesTest() {
    private val appPreferencesDataStore by lazy {
        AppLocalPreferencesDataStore(dataStore)
    }

    @Test
    fun `getAppLanguage should return empty string when no value`() = runTest {
        val result = appPreferencesDataStore.getAppLanguage()

        assertThat(result.first()).isEmpty()
    }

    @Test
    fun `getAppLanguage should return app language when has value`() = runTest {
        appPreferencesDataStore.setAppLanguage(appLanguage)

        val result = appPreferencesDataStore.getAppLanguage()

        assertThat(result.first()).isEqualTo(appLanguage)
    }

    @Test
    fun `setAppLanguage should save provided value when called`() = runTest {
        appPreferencesDataStore.setAppLanguage(appLanguage)
        val result = appPreferencesDataStore.getAppLanguage()

        assertThat(result.first()).isEqualTo(appLanguage)
    }

    @Test
    fun `getAppTheme should return true when no value`() = runTest {
        val result = appPreferencesDataStore.getAppTheme()

        assertThat(result.first()).isTrue()
    }

    @Test
    fun `getAppTheme should return app theme when has value`() = runTest {
        appPreferencesDataStore.setAppTheme(appTheme)

        val result = appPreferencesDataStore.getAppTheme()

        assertThat(result.first()).isEqualTo(appTheme)
    }

    @Test
    fun `setAppTheme should save provided value when called`() = runTest {
        appPreferencesDataStore.setAppTheme(appTheme)
        val result = appPreferencesDataStore.getAppTheme()

        assertThat(result.first()).isEqualTo(appTheme)
    }

    @Test
    fun `getRestrictionLevel should return STRICT when no value`() = runTest {
        val result = appPreferencesDataStore.getRestrictionLevel()

        assertThat(result.first()).isEqualTo(defaultRestrictLevel)
    }

    @Test
    fun `getRestrictionLevel should return restrict level when has value`() = runTest {
        appPreferencesDataStore.setRestrictionLevel(restrictLevel)

        val result = appPreferencesDataStore.getRestrictionLevel()

        assertThat(result.first()).isEqualTo(restrictLevel)
    }

    @Test
    fun `setRestrictionLevel should save provided value when called`() = runTest {
        appPreferencesDataStore.setRestrictionLevel(restrictLevel)
        val result = appPreferencesDataStore.getRestrictionLevel()

        assertThat(result.first()).isEqualTo(restrictLevel)
    }

    @Test
    fun `isOnboardingCompleted should return false when no value`() = runTest {
        val result = appPreferencesDataStore.isOnboardingCompleted()

        assertThat(result).isFalse()
    }

    @Test
    fun `isOnboardingCompleted should return is onboarding completed when has value`() = runTest {
        appPreferencesDataStore.setOnboardingCompleted(isOnboardingCompleted)

        val result = appPreferencesDataStore.isOnboardingCompleted()

        assertThat(result).isEqualTo(isOnboardingCompleted)
    }

    @Test
    fun `setOnboardingCompleted should save provided value when called`() = runTest {
        appPreferencesDataStore.setAppTheme(isOnboardingCompleted)
        val result = appPreferencesDataStore.getAppTheme()

        assertThat(result.first()).isEqualTo(isOnboardingCompleted)
    }
}

private const val appLanguage = "en"
private const val appTheme = false
private val restrictLevel = RestrictionLevel.MODERATE.name
private val defaultRestrictLevel = RestrictionLevel.STRICT.name
private const val isOnboardingCompleted = true
