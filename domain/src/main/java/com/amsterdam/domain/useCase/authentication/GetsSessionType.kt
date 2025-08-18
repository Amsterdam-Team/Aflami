package com.amsterdam.domain.useCase.authentication

import com.amsterdam.domain.repository.AuthenticationRepository
import com.amsterdam.domain.utils.SessionType

class GetsSessionType (
    private val authenticationRepository: AuthenticationRepository
) {
    suspend operator fun invoke(): SessionType? {
        return authenticationRepository.getSessionType()
    }
}