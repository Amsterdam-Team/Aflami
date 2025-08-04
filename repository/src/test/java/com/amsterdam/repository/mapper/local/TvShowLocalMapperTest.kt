/*
package com.amsterdam.repository.mapper.local

import com.amsterdam.repository.mapper.local.testFactory.createLocalTvShowDto
import com.amsterdam.repository.mapper.local.testFactory.createTvShow
import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class TvShowLocalMapperTest {

    private lateinit var mapper: TvShowLocalMapper

    @BeforeEach
    fun setUp() {
        mapper = TvShowLocalMapper()
    }

    @Test
    fun `toEntity should map LocalTvShowDto to TvShow correctly`() {
        val dto = createLocalTvShowDto()

        val expected = createTvShow(
            id = dto.tvShowId,
            name = dto.name,
            description = dto.description,
            posterUrl = dto.poster,
            productionYear = dto.productionYear.toUInt(),
            rating = dto.rating,
            popularity = dto.popularity,
            categories = emptyList()
        )

        val result = mapper.toEntity(dto)

        assertThat(result).isEqualTo(expected)
    }

    @Test
    fun `toDto should map TvShow to LocalTvShowDto correctly`() {
        val entity = createTvShow()

        val expected = createLocalTvShowDto(
            tvShowId = entity.id,
            name = entity.name,
            description = entity.description,
            poster = entity.posterUrl,
            productionYear = entity.productionYear.toInt(),
            rating = entity.rating,
            popularity = entity.popularity
        )

        val result = mapper.toDto(entity)

        assertThat(result).isEqualTo(expected)
    }
}
*/
