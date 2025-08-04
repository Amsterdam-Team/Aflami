/*
package com.amsterdam.repository.mapper.remote

import com.amsterdam.entity.Movie
import com.amsterdam.repository.mapper.remote.testFactory.createRemoteMovieItemDto
import com.amsterdam.repository.mapper.shared.toMovieCategory
import com.amsterdam.repository.utils.DateParser
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
    fun `toTvShowEntity should return instance of Movie`() {
        val dto = createRemoteMovieItemDto(
            genreIds = listOf(28, 12),
            genres = emptyList()
        )
        val result = mapper.toTvShowEntity(dto)

        assertThat(result).isInstanceOf(Movie::class.java)
    }

    @Test
    fun `toTvShowEntity should map id correctly`() {
        val dto = createRemoteMovieItemDto(
            genreIds = listOf(28, 12),
            genres = emptyList()
        )
        val result = mapper.toTvShowEntity(dto)

        assertThat(result.id).isEqualTo(dto.id)
    }

    @Test
    fun `toTvShowEntity should map title to name`() {
        val dto = createRemoteMovieItemDto(
            genreIds = listOf(28, 12),
            genres = emptyList()
        )
        val result = mapper.toTvShowEntity(dto)

        assertThat(result.name).isEqualTo(dto.title)
    }

    @Test
    fun `toTvShowEntity should map overview to description`() {
        val dto = createRemoteMovieItemDto(
            genreIds = listOf(28, 12),
            genres = emptyList()
        )
        val result = mapper.toTvShowEntity(dto)

        assertThat(result.description).isEqualTo(dto.overview)
    }

    @Test
    fun `toTvShowEntity should map posterPath to posterUrl`() {
        val dto = createRemoteMovieItemDto(
            genreIds = listOf(28, 12),
            genres = emptyList()
        )
        val result = mapper.toTvShowEntity(dto)

        assertThat(result.posterUrl).contains(dto.posterPath!!)
    }

    @Test
    fun `toTvShowEntity should map release date to productionYear`() {
        val dto = createRemoteMovieItemDto(
            genreIds = listOf(28, 12),
            genres = emptyList()
        )
        val result = mapper.toTvShowEntity(dto)

        assertThat(result.productionYear).isEqualTo(2010u)
    }

    @Test
    fun `toTvShowEntity should map genreIds to categories`() {
        val dto = createRemoteMovieItemDto(
            genreIds = listOf(28, 12),
            genres = emptyList()
        )
        val result = mapper.toTvShowEntity(dto)

        assertThat(result.categories).containsExactlyElementsIn(
            listOf(
                28L.toMovieCategory(),
                12L.toMovieCategory()
            )
        )
    }

    @Test
    fun `toTvShowEntity should map voteAverage to rating`() {
        val dto = createRemoteMovieItemDto(
            genreIds = listOf(28, 12),
            genres = emptyList()
        )
        val result = mapper.toTvShowEntity(dto)

        assertThat(result.rating).isEqualTo(dto.voteAverage.toFloat())
    }

    @Test
    fun `toTvShowEntity should map popularity correctly`() {
        val dto = createRemoteMovieItemDto(
            genreIds = listOf(28, 12),
            genres = emptyList()
        )
        val result = mapper.toTvShowEntity(dto)

        assertThat(result.popularity).isEqualTo(dto.popularity)
    }

    @Test
    fun `toTvShowEntity should map first originCountry correctly`() {
        val dto = createRemoteMovieItemDto(
            genreIds = listOf(28, 12),
            genres = emptyList()
        )
        val result = mapper.toTvShowEntity(dto)

        assertThat(result.originCountry).isEqualTo(dto.originCountry.first())
    }

    @Test
    fun `toTvShowEntity should map runTimeInMinutes correctly`() {
        val dto = createRemoteMovieItemDto(
            genreIds = listOf(28, 12),
            genres = emptyList()
        )
        val result = mapper.toTvShowEntity(dto)

        assertThat(result.runTimeInMinutes).isEqualTo(dto.runTimeInMinutes)
    }

    @Test
    fun `toTvShowEntity should map video correctly`() {
        val dto = createRemoteMovieItemDto(
            genreIds = listOf(28, 12),
            genres = emptyList()
        )
        val result = mapper.toTvShowEntity(dto)

        assertThat(result.hasVideo).isEqualTo(dto.video)
    }

    @Test
    fun `toTvShowEntity should map genres when genreIds is empty`() {
        val dto = createRemoteMovieItemDto(
            genreIds = emptyList(),
            genres = listOf(
                com.amsterdam.repository.dto.remote.RemoteCategoryDto(35, "Comedy"),
                com.amsterdam.repository.dto.remote.RemoteCategoryDto(18, "Drama")
            )
        )
        val result = mapper.toTvShowEntity(dto)

        assertThat(result.categories).containsExactlyElementsIn(
            listOf(
                35L.toMovieCategory(),
                18L.toMovieCategory()
            )
        )
    }
}
*/
