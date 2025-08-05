package com.amsterdam.domain.useCase.list

import com.amsterdam.domain.repository.UserListRepository
import com.amsterdam.entity.UserList

class GetUserListsUseCase(
    private val userListRepository: UserListRepository,
) {
    suspend operator fun invoke(
        page: Int = 1,
    ): List<UserList> {
        return userListRepository.getUserLists(page = page)
    }
}
