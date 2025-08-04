package com.amsterdam.domain.useCase.authentication

import com.amsterdam.domain.repository.AuthenticationRepository
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class GetsSessionTypeTest {

    private lateinit var getsSessionType: GetsSessionType
    private lateinit var authenticationRepository: AuthenticationRepository


    @BeforeEach
    fun setUp() {
        authenticationRepository = mockk(relaxed = true)
        getsSessionType = GetsSessionType(authenticationRepository)
    }


    @Test
    fun `should call getSessionType from authenticationRepository`() = runTest {
        getsSessionType()
        coVerify(exactly = 1) { authenticationRepository.getSessionType() }
    }

}