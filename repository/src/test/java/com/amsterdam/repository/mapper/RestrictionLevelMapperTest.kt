package com.amsterdam.repository.mapper

import com.amsterdam.domain.utils.RestrictionLevel
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class RestrictionLevelMapperTest {
    @Nested
    inner class ToLocalDtoTest {
        @Test
        fun `toLocalDto should map STRICT to its correct string`() {
            val result = strictLevel.toLocalDto()

            assertThat(result).isEqualTo("STRICT")
        }

        @Test
        fun `toLocalDto should map MODERATE to its correct string`() {
            val result = moderateLevel.toLocalDto()

            assertThat(result).isEqualTo("MODERATE")
        }

        @Test
        fun `toLocalDto should map OFF to its correct string`() {
            val result = offLevel.toLocalDto()

            assertThat(result).isEqualTo("OFF")
        }
    }

    @Nested
    inner class StringToEntityTest {
        @Test
        fun `should map flow of known strings to flow of RestrictionLevel entities`() = runTest {
            val stringFlow: Flow<String> = flowOf("STRICT", "MODERATE", "OFF")

            val resultFlow = stringToRestrictionLevelEntity(stringFlow)
            val resultList = resultFlow.toList()

            assertThat(resultList).containsExactly(
                RestrictionLevel.STRICT,
                RestrictionLevel.MODERATE,
                RestrictionLevel.OFF
            ).inOrder()
        }

        @Test
        fun `should map flow of unknown or invalid strings to OFF as default`() = runTest {
            val stringFlow: Flow<String> = flowOf("UNKNOWN", "moderate", "", "STRICT")

            val resultFlow = stringToRestrictionLevelEntity(stringFlow)
            val resultList = resultFlow.toList()

            assertThat(resultList).containsExactly(
                RestrictionLevel.OFF,
                RestrictionLevel.OFF,
                RestrictionLevel.OFF,
                RestrictionLevel.STRICT
            ).inOrder()
        }
    }

    private val strictLevel = RestrictionLevel.STRICT
    private val moderateLevel = RestrictionLevel.MODERATE
    private val offLevel = RestrictionLevel.OFF
}