package com.amsterdam.viewmodel.search.countrySearch

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.amsterdam.domain.useCase.search.GetMoviesByCountryUseCase
import com.amsterdam.entity.Country
import com.amsterdam.entity.Movie
import com.amsterdam.paging.BasePagingSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CountrySearchPagingSource @Inject constructor(
    private val getMoviesByCountryUseCase: GetMoviesByCountryUseCase,
) : BasePagingSource<Movie>() {

    private lateinit var country: Country

    override suspend fun fetch(page: Int): List<Movie> {
        return getMoviesByCountryUseCase(country, page)
    }

    fun getMovies(country: Country): Flow<PagingData<Movie>> {
        this.country = country
        return Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE,
                prefetchDistance = PREFETCH_DISTANCE
            ),
            pagingSourceFactory = { this }
        ).flow
    }
}