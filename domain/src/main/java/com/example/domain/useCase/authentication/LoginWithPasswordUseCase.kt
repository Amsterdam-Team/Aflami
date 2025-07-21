package com.example.domain.useCase.authentication

import com.example.domain.repository.AuthenticationRepository

class LoginWithPasswordUseCase(
    private val authenticationRepository: AuthenticationRepository,
) {
    suspend operator fun invoke(
        username: String,
        password: String,
    ) {
        authenticationRepository.loginWithPassword(username, password)
    }
}
