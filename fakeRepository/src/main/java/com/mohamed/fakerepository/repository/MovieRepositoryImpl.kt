package com.mohamed.fakerepository.repository

import com.example.domain.repository.MovieRepository
import com.example.entity.Category
import com.example.entity.Movie
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.datetime.Clock

class MovieRepositoryImpl(
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : MovieRepository {
    override suspend fun getMoviesByKeyword(keyword: String): List<Movie> {
        return if (keyword != "") {
            listOf(
                Movie(
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
                    rating = 9.9f
                )
            )
        } else emptyList()
    }

    override suspend fun getMoviesByActor(actorName: String): List<Movie> {
        return if (actorName != "") {
            listOf(
                Movie(
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
                    rating = 9.9f
                )
            )
        } else emptyList()

    }

    override suspend fun getMoviesByCountryIsoCode(countryIsoCode: String): List<Movie> {
        return if (countryIsoCode != "") {
            listOf(
                Movie(
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
                    rating = 9.9f
                )
            )
        } else emptyList()
    }
}