package com.amsterdam.domain.useCase.preferences

import com.amsterdam.domain.repository.AppPreferencesRepository
import com.amsterdam.domain.utils.RestrictionLevel
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class ManageRestrictionLevelUseCaseTest {

    private val preferencesRepository: AppPreferencesRepository = mockk(relaxed = true)
    private val manageRestrictionLevelUseCase by lazy {
        ManageRestrictionLevelUseCase(preferencesRepository)
    }

    @Test
    fun `setRestrictionLevel should call repository with correct value`() = runTest {
        val restrictionLevel = RestrictionLevel.STRICT
        coEvery { preferencesRepository.setRestrictionLevel(restrictionLevel) } returns Unit

        manageRestrictionLevelUseCase.setRestrictionLevel(restrictionLevel)

        coVerify { preferencesRepository.setRestrictionLevel(restrictionLevel) }
    }

    @Test
    fun `getRestrictionLevel should return expected restriction level`() = runTest {
        val expected = RestrictionLevel.MODERATE
        coEvery{ preferencesRepository.getRestrictionLevel() } returns flowOf(expected)

        val restrictionLevelFlow = manageRestrictionLevelUseCase.getRestrictionLevel()
        lateinit var actual: RestrictionLevel
        restrictionLevelFlow.collect { value ->
            actual = value
        }

        assertThat(actual).isEqualTo(expected)
    }
}