package com.amsterdam.repository.mapper.local

import com.amsterdam.entity.Movie
import com.amsterdam.repository.dto.local.MovieLocalDto
import com.amsterdam.repository.dto.local.relation.MovieWithCategories
import com.amsterdam.repository.mapper.toEntity
import com.google.common.truth.Truth.assertThat
import kotlinx.datetime.LocalDate
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class MovieWithCategoriesLocalMapperTest {


    @Test
    @DisplayName("should return Movie when converting from MovieWithCategories")
    fun `toEntity should return Movie when given MovieWithCategories`() {
        // Given
        val localMovie = MovieWithCategories(
            movie = MovieLocalDto(
                movieId = 101,
                name = "Inception",
                description = "A mind-bending thriller",
                poster = "poster_url.jpg",
                releaseDate = LocalDate.parse("2020-01-01"),
                rating = 8.8f,
                popularity = 99.5,
                movieLength = 148,
                originCountry = "USA",
                storedLanguage = "en",
            ),
            categories = emptyList()
        )


        val expectedDomainMovie = Movie(
            id = 101,
            name = "Inception",
            description = "A mind-bending thriller",
            posterUrl = "poster_url.jpg",
            rating = 8.8f,
            popularity = 99.5,
            runTimeInMinutes = 148,
            originCountry = "USA",
            releaseDate = LocalDate.parse("2020-01-01"),
            categories = emptyList(),
            videoUrl = "",
        )
        //When
        val result = localMovie.toEntity()
        // Then
        assertThat(result).isEqualTo(expectedDomainMovie)


    }

}