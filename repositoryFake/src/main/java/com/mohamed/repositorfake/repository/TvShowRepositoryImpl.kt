package com.mohamed.repositorfake.repository

import com.example.domain.repository.TvShowRepository
import com.example.entity.Category
import com.example.entity.Movie
import com.example.entity.TvShow

class TvShowRepositoryImpl() : TvShowRepository {
    override suspend fun getTvShowByKeyword(keyword: String): List<TvShow> {
        return if (keyword != "") {
            listOf(
                TvShow(
                    id = 10,
                    name = "spider-man",
                    description = "spooooooder",
                    poster = "",
                    productionYear = 2022,
                    categories = listOf(
                        Category(
                            id = 1,
                            name = "action",
                            image = ""
                        ),
                        Category(
                            id = 2,
                            name = "action",
                            image = ""
                        ),
                    ),
                    rating = 9.9f,
                    popularity = 0.0
                )
            )
        } else emptyList()
    }
}