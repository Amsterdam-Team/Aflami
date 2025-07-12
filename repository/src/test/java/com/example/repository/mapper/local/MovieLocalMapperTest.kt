package com.example.repository.mapper.local

import com.example.entity.Category
import com.example.entity.Movie
import com.example.repository.dto.local.LocalMovieDto
import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test

class MovieLocalMapperTest {

    private val mapper = MovieLocalMapper()

    @Test
    fun `should return Movie with all fields and categories when mapping from LocalMovieDto`() {
        val dto = LocalMovieDto(
            id = 1L,
            name = "Inception",
            description = "Dream within a dream",
            poster = "inception.jpg",
            productionYear = 2010,
            rating = 8.8f
        )
        val categories = listOf(Category(1L, "Sci-Fi", "sci-fi.png"))

        val result = mapper.mapFromLocal(dto, categories)

        assertThat(result.id).isEqualTo(1L)
        assertThat(result.name).isEqualTo("Inception")
        assertThat(result.description).isEqualTo("Dream within a dream")
        assertThat(result.poster).isEqualTo("inception.jpg")
        assertThat(result.productionYear).isEqualTo(2010)
        assertThat(result.rating).isEqualTo(8.8f)
        assertThat(result.categories).containsExactly(Category(1L, "Sci-Fi", "sci-fi.png"))
    }

    @Test
    fun `should return Movie with empty categories when mapping from LocalMovieDto without categories`() {
        val dto = LocalMovieDto(
            id = 2L,
            name = "Titanic",
            description = "Romantic tragedy",
            poster = "titanic.jpg",
            productionYear = 1997,
            rating = 7.9f
        )

        val result = mapper.mapFromLocal(dto)

        assertThat(result.categories).isEmpty()
    }

    @Test
    fun `should return list of Movies with correct categories when mapping from LocalMovieDto list`() {
        val dtos = listOf(
            LocalMovieDto(1L, "Movie A", "Desc A", "a.jpg", 2001, 7.1f),
            LocalMovieDto(2L, "Movie B", "Desc B", "b.jpg", 2002, 7.2f)
        )
        val categoriesMap = mapOf(
            1L to listOf(Category(1L, "Action", "action.png")),
            2L to listOf(Category(2L, "Drama", "drama.png"))
        )

        val result = mapper.mapListFromLocal(dtos, categoriesMap)

        assertThat(result).hasSize(2)
        assertThat(result[0].categories).containsExactly(Category(1L, "Action", "action.png"))
        assertThat(result[1].categories).containsExactly(Category(2L, "Drama", "drama.png"))
    }

    @Test
    fun `should return list of Movies with empty categories when categories map is empty`() {
        val dtos = listOf(
            LocalMovieDto(1L, "Movie A", "Desc A", "a.jpg", 2001, 7.1f)
        )

        val categoriesMap = emptyMap<Long, List<Category>>()

        val result = mapper.mapListFromLocal(dtos, categoriesMap)

        assertThat(result).hasSize(1)
        assertThat(result[0].categories).isEmpty()
    }

    @Test
    fun `should return list of LocalMovieDto when mapping from list of Movies`() {
        val domains = listOf(
            Movie(1L, "Movie A", "Desc A", "a.jpg", 2001, emptyList(),7.1f),
            Movie(2L, "Movie B", "Desc B", "b.jpg", 2002, emptyList(), 7.2f)
        )

        val mapper = MovieLocalMapper()

        val result = mapper.mapListToLocal(domains)

        assertThat(result).hasSize(2)
        assertThat(result[0].name).isEqualTo("Movie A")
        assertThat(result[1].name).isEqualTo("Movie B")
    }


    @Test
    fun `should return empty Movie list when mapping empty LocalMovieDto list`() {
        val result = mapper.mapListFromLocal(emptyList(), emptyMap())

        assertThat(result).isEmpty()
    }

    @Test
    fun `should return empty LocalMovieDto list when mapping empty Movie list`() {
        val result = mapper.mapListToLocal(emptyList())

        assertThat(result).isEmpty()
    }
}
