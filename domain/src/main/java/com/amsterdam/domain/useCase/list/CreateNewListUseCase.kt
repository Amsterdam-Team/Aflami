package com.amsterdam.domain.useCase.list

import com.amsterdam.domain.repository.UserListRepository

class CreateNewListUseCase(
    private val userListRepository: UserListRepository,
) {
    suspend operator fun invoke(listName: String): Int = userListRepository.createNewList(listName)
}
