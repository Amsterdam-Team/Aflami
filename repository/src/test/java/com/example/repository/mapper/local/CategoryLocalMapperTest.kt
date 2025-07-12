package com.example.repository.mapper.local

import com.example.entity.Category
import com.example.repository.dto.local.LocalCategoryDto
import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test

class CategoryLocalMapperTest {

    private val mapper = CategoryLocalMapper()

    @Test
    fun `should return Category with same id and name and empty image when mapping from LocalCategoryDto`() {
        val dto = LocalCategoryDto(id = 1, name = "Action")

        val result = mapper.mapFromLocal(dto)

        assertThat(result.id).isEqualTo(1)
        assertThat(result.name).isEqualTo("Action")
        assertThat(result.image).isEmpty()
    }

    @Test
    fun `should return LocalCategoryDto with same id and name when mapping from Category`() {
        val category = Category(id = 2, name = "Drama", image = "someImage.png")

        val result = mapper.mapToLocal(category)

        assertThat(result.categoryId).isEqualTo(2)
        assertThat(result.name).isEqualTo("Drama")
    }

    @Test
    fun `should return list of Categories when mapping from list of LocalCategoryDto`() {
        val dtos = listOf(
            LocalCategoryDto(id = 1, name = "Action"),
            LocalCategoryDto(id = 2, name = "Comedy")
        )

        val result = mapper.mapListFromLocal(dtos)

        assertThat(result).hasSize(2)
        assertThat(result.map { it.name }).containsExactly("Action", "Comedy")
    }

    @Test
    fun `should return list of LocalCategoryDto when mapping from list of Category`() {
        val domains = listOf(
            Category(id = 1, name = "Action", image = ""),
            Category(id = 2, name = "Comedy", image = "")
        )

        val result = mapper.mapListToLocal(domains)

        assertThat(result).hasSize(2)
        assertThat(result.map { it.name }).containsExactly("Action", "Comedy")
    }
}