package com.example.repository.mapper.remote

import com.example.entity.Movie
import com.example.repository.mapper.remote.testFactory.createRemoteMovieItemDto
import com.example.repository.mapper.shared.toMovieCategory
import com.example.repository.utils.DateParser
import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class MovieRemoteMapperTest {

    private lateinit var mapper: MovieRemoteMapper
    private val dateParser: DateParser = mockk()

    @BeforeEach
    fun setUp() {
        every { dateParser.parseYear(any()) } returns 2010
        mapper = MovieRemoteMapper(dateParser)
    }

    @Test
    fun `toEntity should return instance of Movie`() {
        val dto = createRemoteMovieItemDto(
            genreIds = listOf(28, 12),
            genres = emptyList()
        )
        val result = mapper.toEntity(dto)

        assertThat(result).isInstanceOf(Movie::class.java)
    }

    @Test
    fun `toEntity should map id correctly`() {
        val dto = createRemoteMovieItemDto(
            genreIds = listOf(28, 12),
            genres = emptyList()
        )
        val result = mapper.toEntity(dto)

        assertThat(result.id).isEqualTo(dto.id)
    }

    @Test
    fun `toEntity should map title to name`() {
        val dto = createRemoteMovieItemDto(
            genreIds = listOf(28, 12),
            genres = emptyList()
        )
        val result = mapper.toEntity(dto)

        assertThat(result.name).isEqualTo(dto.title)
    }

    @Test
    fun `toEntity should map overview to description`() {
        val dto = createRemoteMovieItemDto(
            genreIds = listOf(28, 12),
            genres = emptyList()
        )
        val result = mapper.toEntity(dto)

        assertThat(result.description).isEqualTo(dto.overview)
    }

    @Test
    fun `toEntity should map posterPath to posterUrl`() {
        val dto = createRemoteMovieItemDto(
            genreIds = listOf(28, 12),
            genres = emptyList()
        )
        val result = mapper.toEntity(dto)

        assertThat(result.posterUrl).contains(dto.posterPath!!)
    }

    @Test
    fun `toEntity should map release date to productionYear`() {
        val dto = createRemoteMovieItemDto(
            genreIds = listOf(28, 12),
            genres = emptyList()
        )
        val result = mapper.toEntity(dto)

        assertThat(result.productionYear).isEqualTo(2010u)
    }

    @Test
    fun `toEntity should map genreIds to categories`() {
        val dto = createRemoteMovieItemDto(
            genreIds = listOf(28, 12),
            genres = emptyList()
        )
        val result = mapper.toEntity(dto)

        assertThat(result.categories).containsExactlyElementsIn(
            listOf(
                28L.toMovieCategory(),
                12L.toMovieCategory()
            )
        )
    }

    @Test
    fun `toEntity should map voteAverage to rating`() {
        val dto = createRemoteMovieItemDto(
            genreIds = listOf(28, 12),
            genres = emptyList()
        )
        val result = mapper.toEntity(dto)

        assertThat(result.rating).isEqualTo(dto.voteAverage.toFloat())
    }

    @Test
    fun `toEntity should map popularity correctly`() {
        val dto = createRemoteMovieItemDto(
            genreIds = listOf(28, 12),
            genres = emptyList()
        )
        val result = mapper.toEntity(dto)

        assertThat(result.popularity).isEqualTo(dto.popularity)
    }

    @Test
    fun `toEntity should map first originCountry correctly`() {
        val dto = createRemoteMovieItemDto(
            genreIds = listOf(28, 12),
            genres = emptyList()
        )
        val result = mapper.toEntity(dto)

        assertThat(result.originCountry).isEqualTo(dto.originCountry.first())
    }

    @Test
    fun `toEntity should map runtime correctly`() {
        val dto = createRemoteMovieItemDto(
            genreIds = listOf(28, 12),
            genres = emptyList()
        )
        val result = mapper.toEntity(dto)

        assertThat(result.runTime).isEqualTo(dto.runtime)
    }

    @Test
    fun `toEntity should map video correctly`() {
        val dto = createRemoteMovieItemDto(
            genreIds = listOf(28, 12),
            genres = emptyList()
        )
        val result = mapper.toEntity(dto)

        assertThat(result.hasVideo).isEqualTo(dto.video)
    }

    @Test
    fun `toEntity should map genres when genreIds is empty`() {
        val dto = createRemoteMovieItemDto(
            genreIds = emptyList(),
            genres = listOf(
                com.example.repository.dto.remote.RemoteCategoryDto(35, "Comedy"),
                com.example.repository.dto.remote.RemoteCategoryDto(18, "Drama")
            )
        )
        val result = mapper.toEntity(dto)

        assertThat(result.categories).containsExactlyElementsIn(
            listOf(
                35L.toMovieCategory(),
                18L.toMovieCategory()
            )
        )
    }
}
