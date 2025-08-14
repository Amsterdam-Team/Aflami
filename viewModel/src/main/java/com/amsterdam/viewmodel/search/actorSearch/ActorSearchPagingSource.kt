package com.amsterdam.viewmodel.search.actorSearch

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.amsterdam.domain.useCase.search.GetMoviesByActorUseCase
import com.amsterdam.entity.Movie
import com.amsterdam.paging.BasePagingSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ActorSearchPagingSource @Inject constructor(
    private val getMoviesByActorUseCase: GetMoviesByActorUseCase
): BasePagingSource<Movie>() {

    private lateinit var query: String

    override suspend fun fetch(page: Int): List<Movie> {
        return getMoviesByActorUseCase(query, page)
    }

    fun getMovies(query: String): Flow<PagingData<Movie>>{
        this.query = query
        return Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE,
                prefetchDistance = PREFETCH_DISTANCE
            ),
            pagingSourceFactory = { this }
        ).flow
    }
}