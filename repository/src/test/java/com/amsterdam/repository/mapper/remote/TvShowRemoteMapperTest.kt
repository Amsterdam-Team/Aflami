/*
package com.amsterdam.repository.mapper.remote

import com.amsterdam.entity.TvShow
import com.amsterdam.repository.mapper.remote.testFactory.createRemoteTvShowItemDto
import com.amsterdam.repository.mapper.shared.toTvShowCategory
import com.amsterdam.repository.utils.DateParser
import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class TvShowRemoteMapperTest {

    private lateinit var dateParser: DateParser
    private lateinit var mapper: TvShowRemoteMapper

    @BeforeEach
    fun setUp() {
        dateParser = mockk()
        mapper = TvShowRemoteMapper(dateParser)
    }

    private val fakeDto = createRemoteTvShowItemDto()

    @Test
    fun `toEntity returns correct id`() {
        every { dateParser.parseYear(fakeDto.releaseDate) } returns 2022

        val result: TvShow = mapper.toEntity(fakeDto)

        assertThat(result.id).isEqualTo(101)
    }

    @Test
    fun `toEntity returns correct name`() {
        every { dateParser.parseYear(fakeDto.releaseDate) } returns 2022

        val result: TvShow = mapper.toEntity(fakeDto)

        assertThat(result.name).isEqualTo("Test Show")
    }

    @Test
    fun `toEntity returns correct description`() {
        every { dateParser.parseYear(fakeDto.releaseDate) } returns 2022

        val result: TvShow = mapper.toEntity(fakeDto)

        assertThat(result.description).isEqualTo("A test TV show")
    }

    @Test
    fun `toEntity returns correct production year`() {
        every { dateParser.parseYear(fakeDto.releaseDate) } returns 2022

        val result: TvShow = mapper.toEntity(fakeDto)

        assertThat(result.productionYear).isEqualTo(2022u)
    }

    @Test
    fun `toEntity returns correct category ids`() {
        every { dateParser.parseYear(fakeDto.releaseDate) } returns 2022

        val result: TvShow = mapper.toEntity(fakeDto)

        assertThat(result.categories).containsExactly(
            10765L.toTvShowCategory(),
            18L.toTvShowCategory()
        )
    }

    @Test
    fun `toEntity returns correct rating`() {
        every { dateParser.parseYear(fakeDto.releaseDate) } returns 2022

        val result: TvShow = mapper.toEntity(fakeDto)

        assertThat(result.rating).isEqualTo(8.3f)
    }

    @Test
    fun `toEntity returns correct popularity`() {
        every { dateParser.parseYear(fakeDto.releaseDate) } returns 2022

        val result: TvShow = mapper.toEntity(fakeDto)

        assertThat(result.popularity).isEqualTo(99.9)
    }
}
*/
