package com.amsterdam.repository.mapper

import com.amsterdam.entity.category.MovieGenre
import com.amsterdam.repository.dto.local.MovieLocalDto
import com.amsterdam.repository.dto.remote.CategoryRemoteDto
import com.amsterdam.repository.dto.remote.MovieItemRemoteDto
import com.google.common.truth.Truth.assertThat
import kotlinx.datetime.LocalDate
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class MovieMapperTest {
    @Nested
    inner class LocalDtoToEntityTest {
        @Test
        fun `toEntity should map MovieLocalDto to Movie entity`() {
            val result = localDto.toEntity()

            assertThat(result.id).isEqualTo(1L)
            assertThat(result.name).isEqualTo("Local Movie")
            assertThat(result.categories).isEmpty()
        }
    }

    @Nested
    inner class RemoteDtoToEntityTest {
        @Test
        fun `toEntity should map with poster image (w500) when isPoster is true`() {
            val result = remoteDto.toEntity(isPoster = true)

            assertThat(result.posterUrl).isEqualTo("https://image.tmdb.org/t/p/w500/poster.jpg")
        }

        @Test
        fun `toEntity should map with backdrop image (w300) when isPoster is false`() {
            val result = remoteDto.toEntity(isPoster = false)

            assertThat(result.posterUrl).isEqualTo("https://image.tmdb.org/t/p/w300/backdrop.jpg")
        }

        @Test
        fun `toEntity should use genreIds when it's not empty`() {
            val result = remoteDto.toEntity()

            assertThat(result.categories).containsExactly(MovieGenre.ACTION)
        }

        @Test
        fun `toEntity should use genres list when genreIds is empty`() {
            val dtoWithGenres = remoteDto.copy(
                genreIds = emptyList(),
                genres = listOf(CategoryRemoteDto(28, "Action"))
            )

            val result = dtoWithGenres.toEntity()

            assertThat(result.categories).containsExactly(MovieGenre.ACTION)
        }
    }

    @Nested
    inner class RemoteDtoToLocalDtoTest {
        @Test
        fun `toLocalDto should map with poster image (w500) when isPoster is true`() {
            val result = remoteDto.toLocalDto(isPoster = true, storedLanguage = "en")

            assertThat(result.poster).isEqualTo("https://image.tmdb.org/t/p/w500/poster.jpg")
        }

        @Test
        fun `toLocalDto should map with backdrop image (w300) when isPoster is false`() {
            val result = remoteDto.toLocalDto(isPoster = false, storedLanguage = "en")

            assertThat(result.poster).isEqualTo("https://image.tmdb.org/t/p/w300/backdrop.jpg")
        }

        @Test
        fun `toLocalDto should map other fields correctly`() {
            val result = remoteDto.toLocalDto(storedLanguage = "ar")

            assertThat(result.movieId).isEqualTo(101L)
            assertThat(result.storedLanguage).isEqualTo("ar")
            assertThat(result.originCountry).isEqualTo("GB")
        }
    }

    @Nested
    inner class ListMappingTests {
        @Test
        fun `toMovieEntityList should map list correctly`() {
            val dtoList = listOf(remoteDto, remoteDto.copy(id = 102L))

            val result = dtoList.toMovieEntityList()

            assertThat(result).hasSize(2)
            assertThat(result[0].id).isEqualTo(101L)
            assertThat(result[1].id).isEqualTo(102L)
        }

        @Test
        fun `toLocalMovieDtoList should map list correctly`() {
            val dtoList = listOf(remoteDto, remoteDto.copy(id = 102L))

            val result = dtoList.toLocalMovieDtoList(storedLanguage = "fr")

            assertThat(result).hasSize(2)
            assertThat(result[0].storedLanguage).isEqualTo("fr")
            assertThat(result[1].movieId).isEqualTo(102L)
        }
    }

    private val localDto = MovieLocalDto(
        movieId = 1L, storedLanguage = "en", name = "Local Movie",
        description = "A movie from local.", poster = "/local.jpg",
        releaseDate = LocalDate.parse("2023-01-01"), rating = 7.5f,
        popularity = 150.0, movieLength = 90, originCountry = "US", isAdult = false
    )

    private val remoteDto = MovieItemRemoteDto(
        id = 101L, title = "Remote Movie", overview = "An overview.",
        posterPath = "/poster.jpg", backdropPath = "/backdrop.jpg",
        releaseDate = "2023-10-26", voteAverage = 8.8,
        genreIds = listOf(28), genres = emptyList(),
        popularity = 1234.5, originCountry = listOf("GB"), runtime = 120,
        adult = false, originalLanguage = "en", originalTitle = "Remote Movie",
        video = false, voteCount = 1000
    )
}