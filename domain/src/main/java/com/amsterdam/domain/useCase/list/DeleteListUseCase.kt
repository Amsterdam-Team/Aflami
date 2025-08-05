package com.amsterdam.domain.useCase.list

import com.amsterdam.domain.repository.UserListRepository

class DeleteListUseCase(
    private val userListRepository: UserListRepository
) {
    suspend operator fun invoke(listId: Long) {
        userListRepository.deleteList(listId)
    }
}