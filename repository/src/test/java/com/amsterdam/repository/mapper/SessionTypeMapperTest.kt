package com.amsterdam.repository.mapper

import com.amsterdam.domain.utils.SessionType
import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class SessionTypeMapperTest {
    @Nested
    inner class ToLocalDtoTest {
        @Test
        fun `toLocalDto should map LOGGED_IN to its correct string`() {
            val result = loggedInSession.toLocalDto()

            assertThat(result).isEqualTo("LOGGED_IN")
        }

        @Test
        fun `toLocalDto should map GUEST to its correct string`() {
            val result = guestSession.toLocalDto()

            assertThat(result).isEqualTo("GUEST")
        }

        @Test
        fun `toLocalDto should map NOT_LOGGED_IN to its correct string`() {
            val result = notLoggedInSession.toLocalDto()

            assertThat(result).isEqualTo("NOT_LOGGED_IN")
        }
    }

    @Nested
    inner class StringToEntityTest {
        @Test
        fun `stringToSessionTypeEntity should map 'LOGGED_IN' string to LOGGED_IN entity`() {
            val result = stringToSessionTypeEntity("LOGGED_IN")

            assertThat(result).isEqualTo(SessionType.LOGGED_IN)
        }

        @Test
        fun `stringToSessionTypeEntity should map 'GUEST' string to GUEST entity`() {
            val result = stringToSessionTypeEntity("GUEST")

            assertThat(result).isEqualTo(SessionType.GUEST)
        }

        @Test
        fun `stringToSessionTypeEntity should map 'NOT_LOGGED_IN' string to NOT_LOGGED_IN entity`() {
            val result = stringToSessionTypeEntity("NOT_LOGGED_IN")

            assertThat(result).isEqualTo(SessionType.NOT_LOGGED_IN)
        }

        @Test
        fun `stringToSessionTypeEntity should map any other string to NOT_LOGGED_IN as default`() {
            val resultFromUnknown = stringToSessionTypeEntity("UNKNOWN_STATE")
            val resultFromEmpty = stringToSessionTypeEntity("")

            assertThat(resultFromUnknown).isEqualTo(SessionType.NOT_LOGGED_IN)
            assertThat(resultFromEmpty).isEqualTo(SessionType.NOT_LOGGED_IN)
        }
    }

    private val loggedInSession = SessionType.LOGGED_IN
    private val guestSession = SessionType.GUEST
    private val notLoggedInSession = SessionType.NOT_LOGGED_IN
}