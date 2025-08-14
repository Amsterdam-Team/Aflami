package com.amsterdam.domain.useCase.authentication

import com.amsterdam.domain.repository.AuthenticationRepository
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

class GetsSessionTypeTest {

    private val authenticationRepository: AuthenticationRepository = mockk(relaxed = true)
    private val getsSessionType by lazy {
        GetsSessionType(authenticationRepository)
    }

    @Test
    fun `should call getSessionType from authenticationRepository`() = runTest {
        getsSessionType()
        coVerify(exactly = 1) { authenticationRepository.getSessionType() }
    }

}