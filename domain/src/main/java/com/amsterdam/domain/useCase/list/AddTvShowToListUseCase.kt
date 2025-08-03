package com.amsterdam.domain.useCase.list

import com.amsterdam.domain.repository.UserListRepository

class AddTvShowToListUseCase(
    private val userListRepository: UserListRepository,
) {
    suspend operator fun invoke(
        listId: Long,
        tvShowId: Int,
    ) {
        userListRepository.addTvShowToList(listId, tvShowId)
    }
}
