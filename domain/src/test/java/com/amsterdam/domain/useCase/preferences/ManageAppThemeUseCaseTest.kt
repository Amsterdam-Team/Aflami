package com.amsterdam.domain.useCase.preferences

import com.amsterdam.domain.repository.AppPreferencesRepository
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test


class ManageAppThemeUseCaseTest {

    private val preferencesRepository: AppPreferencesRepository = mockk(relaxed = true)
    private val manageAppThemeUseCase: ManageAppThemeUseCase by lazy {
        ManageAppThemeUseCase(preferencesRepository)
    }

    @Test
    fun `should call repository with correct value`() = runTest {
        val isDark = true
        coEvery { preferencesRepository.setAppTheme(isDark) } returns Unit

        manageAppThemeUseCase.setAppTheme(isDark)

        coVerify { preferencesRepository.setAppTheme(isDark) }
    }

    @Test
    fun `should return true when theme is dark`() = runTest {
        val expected = true
        coEvery{ preferencesRepository.getAppTheme() } returns flowOf(expected)

        val themeFlow = manageAppThemeUseCase.getAppTheme()
        var actual: Boolean? = null
        themeFlow.collect { value ->
            actual = value
        }

        assertThat(actual).isEqualTo(expected)
    }
}