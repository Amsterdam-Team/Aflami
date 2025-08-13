package com.amsterdam.domain.useCase.list

import com.amsterdam.domain.repository.UserListRepository

class AddMovieToListUseCase(
    private val userListRepository: UserListRepository,
) {
    suspend operator fun invoke(
        listId: Long,
        movieId: Long,
    ) {
        userListRepository.addMovieToList(listId, movieId)
    }
}
