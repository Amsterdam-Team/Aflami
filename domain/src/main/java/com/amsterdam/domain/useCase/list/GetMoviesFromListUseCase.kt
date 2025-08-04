package com.amsterdam.domain.useCase.list

import com.amsterdam.domain.repository.UserListRepository
import com.amsterdam.entity.Movie

class GetMoviesFromListUseCase(
    private val userListRepository: UserListRepository
) {
    suspend operator fun invoke(listId: Long, page: Int): List<Movie> {
        return userListRepository.getMoviesFromList(listId, page)
    }
}
