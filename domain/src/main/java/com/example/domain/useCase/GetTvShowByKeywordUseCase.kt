package com.example.domain.useCase

import com.example.domain.exceptions.NoSearchByKeywordResultFoundException
import com.example.domain.repository.TvShowRepository
import com.example.domain.useCase.genreTypes.MovieGenre
import com.example.domain.useCase.genreTypes.TvShowGenre
import com.example.domain.utils.mapToGenreId
import com.example.entity.TvShow

class GetTvShowByKeywordUseCase(
    private val tvShowRepository: TvShowRepository
) {

    suspend operator fun invoke(
        keyword: String,
        rating: Float = 0f,
        tvShowGenre: TvShowGenre = TvShowGenre.ALL
    ): List<TvShow> {
        return tvShowRepository.getTvShowByKeyword(keyword = keyword)
            .filter { tv -> tv.rating >= rating }
            .filter { tv ->
                if (tvShowGenre == TvShowGenre.ALL)
                    return@filter true
                tv.categories.any { it.id == tvShowGenre.mapToGenreId() }
            }
            .sortedByDescending { it.popularity }
            .ifEmpty {
                throw NoSearchByKeywordResultFoundException()
            }
    }
}