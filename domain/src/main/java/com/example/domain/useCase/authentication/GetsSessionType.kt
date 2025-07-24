package com.example.domain.useCase.authentication

import com.example.domain.repository.AuthenticationRepository
import com.example.domain.utils.SessionType

class GetsSessionType(
    private val authenticationRepository: AuthenticationRepository
) {
    suspend operator fun invoke(): SessionType{
        return authenticationRepository.getSessionType()
    }
}