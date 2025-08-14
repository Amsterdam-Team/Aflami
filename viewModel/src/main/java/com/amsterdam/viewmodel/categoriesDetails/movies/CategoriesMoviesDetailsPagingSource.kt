package com.amsterdam.viewmodel.categoriesDetails.movies

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.amsterdam.domain.useCase.details.GetMoviesByGenreUseCase
import com.amsterdam.entity.Movie
import com.amsterdam.entity.category.MovieGenre
import com.amsterdam.paging.BasePagingSource
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow

class CategoriesMoviesDetailsPagingSource @Inject constructor(
    private val getMoviesByGenreUseCase: GetMoviesByGenreUseCase,
): BasePagingSource<Movie>(){
    private var genre: MovieGenre? = null
    override suspend fun fetch(page: Int): List<Movie> {
        val safeGenre = genre ?: throw IllegalStateException("Genre must be set before fetching")
        return getMoviesByGenreUseCase(safeGenre,page)
    }
    fun getMovies(genre: MovieGenre): Flow<PagingData<Movie>> {
        this.genre = genre
        return Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE,
                prefetchDistance = PREFETCH_DISTANCE
            ),
            pagingSourceFactory = { this}

        ).flow
    }
}