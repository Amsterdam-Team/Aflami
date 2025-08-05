package com.amsterdam.domain.useCase.preferences

import com.amsterdam.domain.repository.AppPreferencesRepository
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class ManageLocaleLanguageUseCaseTest {

    private lateinit var manageLocaleLanguageUseCase: ManageLocaleLanguageUseCase
    private lateinit var preferencesRepository: AppPreferencesRepository

    @BeforeEach
    fun setUp() {
        preferencesRepository = mockk(relaxed = true)
        manageLocaleLanguageUseCase = ManageLocaleLanguageUseCase(preferencesRepository)
    }

    @Test
    fun `setDeviceLanguage should call preferencesRepository setDeviceLanguage `() = runTest {
        val language = "en"
        manageLocaleLanguageUseCase.setDeviceLanguage(language)
        coVerify(exactly = 1) { preferencesRepository.setDeviceLanguage(language) }
    }

    @Test
    fun `getDeviceLanguage should call preferencesRepository getDeviceLanguage `() = runTest {
        manageLocaleLanguageUseCase.getDeviceLanguage()
        coVerify(exactly = 1) { preferencesRepository.getDeviceLanguage() }
    }
}