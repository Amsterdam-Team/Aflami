package com.amsterdam.domain.useCase.preferences

import com.amsterdam.domain.repository.AppPreferencesRepository
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

class ManageLocaleLanguageUseCaseTest {

    private val preferencesRepository: AppPreferencesRepository = mockk(relaxed = true)
    private val manageLocaleLanguageUseCase by lazy {
        ManageLocaleLanguageUseCase(preferencesRepository)
    }

    @Test
    fun `initAppLanguage should set english when preferences return null`() = runTest {
        val deviceLanguage = "en"

        coEvery { preferencesRepository.getAppLanguage() } returns flowOf()
        coEvery { preferencesRepository.setAppLanguage(any()) } just runs

        manageLocaleLanguageUseCase.initAppLanguage(deviceLanguage)

        coVerify(exactly = 1) { preferencesRepository.getAppLanguage() }
        coVerify(exactly = 1) { manageLocaleLanguageUseCase.setAppLanguage(
            ManageLocaleLanguageUseCase.Language.ENGLISH) }
    }

    @Test
    fun `initAppLanguage should set arabic when preferences return null and device is arabic`() = runTest {
        val deviceLanguage = "ar"

        coEvery { preferencesRepository.getAppLanguage() } returns flowOf()
        coEvery { preferencesRepository.setAppLanguage(any()) } just runs

        manageLocaleLanguageUseCase.initAppLanguage(deviceLanguage)

        coVerify(exactly = 1) { preferencesRepository.getAppLanguage() }
        coVerify(exactly = 1) { preferencesRepository.setAppLanguage("ar") }
    }

    @Test
    fun `initAppLanguage should set english for unsupported device language when no saved preference`() = runTest {
        val deviceLanguage = "fr"

        coEvery { preferencesRepository.getAppLanguage() } returns flowOf()
        coEvery { preferencesRepository.setAppLanguage(any()) } just runs

        manageLocaleLanguageUseCase.initAppLanguage(deviceLanguage)

        coVerify(exactly = 1) { preferencesRepository.getAppLanguage() }
        coVerify(exactly = 1) { preferencesRepository.setAppLanguage("en") }
    }

    @Test
    fun `initAppLanguage should set language when saved preference is empty string`() = runTest {
        val deviceLanguage = "en"
        val savedLanguage = ""

        coEvery { preferencesRepository.getAppLanguage() } returns flowOf(savedLanguage)
        coEvery { preferencesRepository.setAppLanguage(any()) } just runs

        manageLocaleLanguageUseCase.initAppLanguage(deviceLanguage)

        coVerify(exactly = 1) { preferencesRepository.getAppLanguage() }
        coVerify(exactly = 1) { preferencesRepository.setAppLanguage("en") }
    }

    @Test
    fun `initAppLanguage should not set language when saved preference exists and is not empty`() = runTest {
        val deviceLanguage = "en"
        val savedLanguage = "ar"

        coEvery { preferencesRepository.getAppLanguage() } returns flowOf(savedLanguage)
        coEvery { preferencesRepository.setAppLanguage(any()) } just runs

        manageLocaleLanguageUseCase.initAppLanguage(deviceLanguage)

        coVerify(exactly = 1) { preferencesRepository.getAppLanguage() }
        coVerify(exactly = 0) { preferencesRepository.setAppLanguage(any()) }
    }


    @Test
    fun `initAppLanguage should handle flow that emits multiple values by taking first`() = runTest {
        val deviceLanguage = "en"

        coEvery { preferencesRepository.getAppLanguage() } returns flow {
            emit("existing_value")
            emit("another_value")
        }
        coEvery { preferencesRepository.setAppLanguage(any()) } just runs

        manageLocaleLanguageUseCase.initAppLanguage(deviceLanguage)

        coVerify(exactly = 1) { preferencesRepository.getAppLanguage() }
        coVerify(exactly = 0) { preferencesRepository.setAppLanguage(any()) }
    }


    @Test
    fun `getAppLanguage should call preferencesRepository and get english`() = runTest {
        val language = "en"
        val expectedLanguage = flowOf(language)

        coEvery { preferencesRepository.getAppLanguage() } returns expectedLanguage

        lateinit var resultLang: ManageLocaleLanguageUseCase.Language

        manageLocaleLanguageUseCase.getAppLanguage().collect{
            resultLang = it
        }

        assertThat(resultLang).isEqualTo(ManageLocaleLanguageUseCase.Language.ENGLISH)
        coVerify(exactly = 1) { preferencesRepository.getAppLanguage() }
    }

    @Test
    fun `getAppLanguage should call preferencesRepository and get arabic`() = runTest {
        val language = "ar"
        val expectedLanguage = flowOf(language)

        coEvery { preferencesRepository.getAppLanguage() } returns expectedLanguage

        lateinit var resultLang: ManageLocaleLanguageUseCase.Language

        manageLocaleLanguageUseCase.getAppLanguage().collect{
            resultLang = it
        }

        assertThat(resultLang).isEqualTo(ManageLocaleLanguageUseCase.Language.ARABIC)
        coVerify(exactly = 1) { preferencesRepository.getAppLanguage() }
    }

    @Test
    fun `getAppLanguage should call preferencesRepository and get english as default when device language is not arabic`() = runTest {
        val language = "fr"
        val expectedLanguage = flowOf(language)

        coEvery { preferencesRepository.getAppLanguage() } returns expectedLanguage

        lateinit var resultLang: ManageLocaleLanguageUseCase.Language

        manageLocaleLanguageUseCase.getAppLanguage().collect{
            resultLang = it
        }
        assertThat(resultLang).isEqualTo(ManageLocaleLanguageUseCase.Language.ENGLISH)
        coVerify(exactly = 1) { preferencesRepository.getAppLanguage() }
    }
}