package com.example.domain.useCase

import com.example.domain.exceptions.NoSearchByKeywordResultFoundException
import com.example.domain.repository.TvShowRepository
import com.example.entity.TvShow
import kotlin.math.roundToInt

class GetTvShowByKeywordUseCase(
    private val tvShowRepository: TvShowRepository
) {

    suspend operator fun invoke(
        keyword: String,
        rating: Float = 0f,
        tvShowGenreId: Int = 0
    ): List<TvShow> {
        return tvShowRepository.getTvShowByKeyword(keyword = keyword)
            .filter { tv -> tv.rating.roundToInt() >= rating }
            .filter { tvGenre ->
                if (tvShowGenreId == 0)
                    return@filter true

                tvGenre.categories.any { it.id == tvShowGenreId.toLong() }
            }
            .sortedByDescending { it.popularity }
            .ifEmpty {
                throw NoSearchByKeywordResultFoundException()
            }
    }
}