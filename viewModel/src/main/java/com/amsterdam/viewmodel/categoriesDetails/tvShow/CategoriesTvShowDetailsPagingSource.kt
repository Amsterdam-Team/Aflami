package com.amsterdam.viewmodel.categoriesDetails.tvShow

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.amsterdam.domain.useCase.details.GetTvShowsByGenreUseCase
import com.amsterdam.entity.TvShow
import com.amsterdam.entity.category.TvShowGenre
import com.amsterdam.paging.BasePagingSource
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow

class CategoriesTvShowDetailsPagingSource @Inject constructor(
    private val getTvShowsByGenreUseCase: GetTvShowsByGenreUseCase,
) : BasePagingSource<TvShow>() {
    private var genre: TvShowGenre? = null
    override suspend fun fetch(page: Int): List<TvShow> {
     val safeGenre = genre ?: throw IllegalStateException("Genre must be set before fetching")
        return getTvShowsByGenreUseCase(safeGenre,page)
    }
    fun getTvShows(tvShowGenre: TvShowGenre): Flow<PagingData<TvShow>> {
        this.genre = tvShowGenre
        return Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE,
                prefetchDistance = PREFETCH_DISTANCE
            ),
            pagingSourceFactory = { this }
        ).flow

    }
}