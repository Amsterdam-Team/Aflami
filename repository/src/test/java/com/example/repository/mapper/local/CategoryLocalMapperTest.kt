package com.example.repository.mapper.local

import com.example.entity.Category
import com.example.entity.category.MovieGenre
import com.example.repository.dto.local.LocalMovieCategoryDto
import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test

class CategoryLocalMapperTest {

    private val mapper = CategoryLocalMapper()


    @Test
    fun `should return LocalCategoryDto with same id and name when mapping from Category`() {
        val category = Category(id = 2, name = "Drama", imageUrl = "someImage.png")

        val result = mapper.mapToLocalMovieCategory(category)

        assertThat(result.categoryId).isEqualTo(2)
        assertThat(result.name).isEqualTo("Drama")
    }

    @Test
    fun `should return list of Categories when mapping from list of LocalCategoryDto`() {
        val dtos = listOf(
            LocalMovieCategoryDto(categoryId = 28, name = "Action"),
            LocalMovieCategoryDto(categoryId = 35, name = "Comedy")
        )

        val result = mapper.mapToMovieCategories(dtos)

        assertThat(result).hasSize(2)
        assertThat(result).containsExactly(
            MovieGenre.ACTION,
            MovieGenre.COMEDY
        )
    }

    @Test
    fun `should return list of LocalCategoryDto when mapping from list of Category`() {
        val domains = listOf(
            Category(id = 1, name = "Action", imageUrl = ""),
            Category(id = 2, name = "Comedy", imageUrl = "")
        )

        val result = mapper.mapToLocalMovieCategories(domains)

        assertThat(result).hasSize(2)
        assertThat(result.map { it.name }).containsExactly("Action", "Comedy")
    }
}