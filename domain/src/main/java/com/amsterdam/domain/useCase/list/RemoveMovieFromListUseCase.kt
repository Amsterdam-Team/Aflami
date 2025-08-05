package com.amsterdam.domain.useCase.list

import com.amsterdam.domain.repository.UserListRepository

class RemoveMovieFromListUseCase(
    private val userListRepository: UserListRepository
) {
    suspend operator fun invoke(listId: Long, movieId: Long) {
        return userListRepository.removeMovieFromList(listId, movieId)
    }
}