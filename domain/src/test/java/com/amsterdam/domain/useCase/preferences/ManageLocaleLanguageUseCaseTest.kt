package com.amsterdam.domain.useCase.preferences

import com.amsterdam.domain.repository.AppPreferencesRepository
import com.google.common.truth.Truth.assertThat
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
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
    fun `when language is null, should set app language`() = runTest {
        coEvery { preferencesRepository.getAppLanguage() } returns flowOf()
        coEvery { preferencesRepository.setAppLanguage(any()) } just Runs

       val result=  manageLocaleLanguageUseCase.setAppLanguage(ManageLocaleLanguageUseCase.Language.ENGLISH)
        assertThat(result).isEqualTo(Unit)
        coVerify { preferencesRepository.setAppLanguage("en") }

    }




    @Test
    fun `initAppLanguage should call preferencesRepository`() = runTest {
        val language = "en"
        val expectedLanguage = flowOf(language)

        coEvery { preferencesRepository.getAppLanguage() } returns expectedLanguage

        val result = manageLocaleLanguageUseCase.getAppLanguage().first()

        assertThat(result).isEqualTo(ManageLocaleLanguageUseCase.Language.ENGLISH)
        coVerify(exactly = 1) { preferencesRepository.getAppLanguage() }
    }


    @Test
        fun `setAppLanguage should call preferencesRepository `() = runTest {
            val language = "en"
            coEvery { preferencesRepository.setAppLanguage(language) } just Runs
            val result=manageLocaleLanguageUseCase.setAppLanguage(ManageLocaleLanguageUseCase.Language.ENGLISH)
            assertThat(result).isEqualTo(Unit)
            coVerify(exactly = 1) { preferencesRepository.setAppLanguage(language) }
        }

        @Test
        fun `getAppLanguage should call preferencesRepository `() = runTest {
            val language = "en"
            val expectedLanguage = flowOf(language)
            coEvery { preferencesRepository.getAppLanguage() } returns expectedLanguage
            val result=manageLocaleLanguageUseCase.getAppLanguage().first()
            assertThat(result).isEqualTo(ManageLocaleLanguageUseCase.Language.ENGLISH)
            coVerify(exactly = 1) { preferencesRepository.getAppLanguage() }
        }
    }