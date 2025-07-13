package com.mohamed.repositorfake.repository

import com.example.domain.repository.CategoryRepository
import com.example.entity.Category

class CategoryRepositoryImpl() : CategoryRepository {
    override suspend fun getMovieCategories(): List<Category> {
        return listOf(
            Category(
                id = 1,
                name = "Action",
                image = ""
            ),
            Category(
                id = 2,
                name = "Action",
                image = ""
            ),
            Category(
                id = 3,
                name = "Action",
                image = ""
            ),
            Category(
                id = 4,
                name = "Action",
                image = ""
            ),
            Category(
                id = 5,
                name = "Action",
                image = ""
            ),
        )
    }

    override suspend fun getTvShowCategories(): List<Category> {
        return listOf(
            Category(
                id = 1,
                name = "Action",
                image = ""
            ),
            Category(
                id = 2,
                name = "Action",
                image = ""
            ),
            Category(
                id = 3,
                name = "Action",
                image = ""
            ),
            Category(
                id = 4,
                name = "Action",
                image = ""
            ),
            Category(
                id = 5,
                name = "Action",
                image = ""
            ),
        )
    }
}