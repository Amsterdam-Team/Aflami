package com.example.domain.useCase.authentication

import com.example.domain.repository.AuthenticationRepository
import com.example.domain.utils.SessionType

class LoginAsGuestUseCase(
    private val authenticationRepository: AuthenticationRepository,
) {
    suspend operator fun invoke(){
        authenticationRepository.setSessionType(SessionType.GUEST)
    }
}