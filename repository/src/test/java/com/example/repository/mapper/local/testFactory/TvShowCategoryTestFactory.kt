package com.example.repository.mapper.local.testFactory

import com.example.entity.TvShow
import com.example.repository.dto.local.LocalTvShowCategoryDto
import com.example.repository.dto.local.LocalTvShowDto
import com.example.repository.dto.local.relation.TvShowWithCategory

fun createLocalTvShowCategoryDto(
    id: Long = 1L,
    name: String = "Default Name"
): LocalTvShowCategoryDto {
    return LocalTvShowCategoryDto(
        categoryId = id,
        name = name
    )
}

fun createLocalTvShowDto(
    tvShowId: Long = 1L,
    name: String = "Breaking Bad",
    description: String = "A high school chemistry teacher turned meth producer.",
    poster: String = "/breaking_bad.jpg",
    productionYear: Int = 2008,
    rating: Float = 9.5f,
    popularity: Double = 99.9
): LocalTvShowDto {
    return LocalTvShowDto(
        tvShowId = tvShowId,
        name = name,
        description = description,
        poster = poster,
        productionYear = productionYear,
        rating = rating,
        popularity = popularity,
        seasonCount = 4,
        originCountry = "US",
    )
}

fun createTvShow(
    id: Long = 2L,
    name: String = "Stranger Things",
    description: String = "A group of kids uncover supernatural mysteries.",
    posterUrl: String = "/stranger_things.jpg",
    productionYear: UInt = 2016u,
    rating: Float = 8.8f,
    popularity: Double = 95.0,
    categories: List<com.example.entity.category.TvShowGenre> = emptyList()
): TvShow {
    return TvShow(
        id = id,
        name = name,
        description = description,
        posterUrl = posterUrl,
        productionYear = productionYear,
        rating = rating,
        popularity = popularity,
        categories = categories,
        seasonCount = 4,
        originCountry = "US",
    )
}

fun createTvShowWithCategory(
    dto: LocalTvShowDto = createLocalTvShowDto(),
    categories: List<LocalTvShowCategoryDto> = listOf(createLocalTvShowCategoryDto())
): TvShowWithCategory = TvShowWithCategory(dto, categories)

fun createLocalTvShowCategoryDtoList(): List<LocalTvShowCategoryDto> {
    return listOf(
        LocalTvShowCategoryDto(categoryId = 10L, name = "DRAMA"),
        LocalTvShowCategoryDto(categoryId = 11L, name = "CRIME")
    )
}