package com.amsterdam.domain.useCase.profile

import com.amsterdam.domain.repository.ProfileRepository

class GetAccountDetailsUseCase(
    private val profileRepository: ProfileRepository
) {
    suspend operator fun invoke() = profileRepository.getAccountDetails()
}