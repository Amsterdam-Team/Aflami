/*
package com.amsterdam.repository.mapper.local

import com.amsterdam.entity.category.TvShowGenre
import com.amsterdam.repository.mapper.local.testFactory.createLocalTvShowCategoryDto
import com.google.common.truth.Truth.assertThat
import org.junit.Test

class TvShowGenreLocalMapperTest {

    private val mapper = TvShowGenreLocalMapper()

    @Test
    fun `toEntity should map known categoryIds to correct TvShowGenre`() {
        val testCases = mapOf(
            1L to TvShowGenre.SCIENCE_FICTION_FANTASY,
            2L to TvShowGenre.ACTION_ADVENTURE,
            3L to TvShowGenre.CRIME,
            4L to TvShowGenre.FAMILY,
            5L to TvShowGenre.MYSTERY,
            6L to TvShowGenre.WAR_POLITICS,
            7L to TvShowGenre.ANIMATION,
            8L to TvShowGenre.COMEDY,
            9L to TvShowGenre.DOCUMENTARY,
            10L to TvShowGenre.DRAMA,
            11L to TvShowGenre.KIDS,
            12L to TvShowGenre.NEWS,
            13L to TvShowGenre.REALITY,
            14L to TvShowGenre.SOAP,
            15L to TvShowGenre.TALK,
            16L to TvShowGenre.WESTERN,
        )

        testCases.forEach { (id, expectedGenre) ->
            val dto = createLocalTvShowCategoryDto(id = id)
            val actual = mapper.toEntity(dto)
            assertThat(actual).isEqualTo(expectedGenre)
        }
    }

    @Test
    fun `toEntity should return TvShowGenre_ALL for unknown categoryIds`() {
        listOf(0L, -1L, 99L).forEach { id ->
            val dto = createLocalTvShowCategoryDto(id = id, name = "Unknown")
            val actual = mapper.toEntity(dto)
            assertThat(actual).isEqualTo(TvShowGenre.ALL)
        }
    }
}
*/
