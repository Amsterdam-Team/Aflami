package com.example.repository.mapper.local

import com.example.entity.Category
import com.example.entity.TvShow
import com.example.repository.dto.local.LocalTvShowDto
import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test

class TvShowLocalMapperTest {

    private val mapper = TvShowLocalMapper()

    @Test
    fun `should return TvShow with all fields and categories when mapping from LocalTvShowDto`() {
        val dto = LocalTvShowDto(
            tvShowId = 1L,
            name = "Breaking Bad",
            description = "Chemistry teacher becomes drug kingpin",
            poster = "bb.jpg",
            productionYear = 2008,
            rating = 9.5f
        )
        val categories = listOf(Category(1L, "Drama", "drama.png"))

        val result = mapper.mapFromLocal(dto, categories)

        assertThat(result.id).isEqualTo(1L)
        assertThat(result.name).isEqualTo("Breaking Bad")
        assertThat(result.description).isEqualTo("Chemistry teacher becomes drug kingpin")
        assertThat(result.poster).isEqualTo("bb.jpg")
        assertThat(result.productionYear).isEqualTo(2008)
        assertThat(result.rating).isEqualTo(9.5f)
        assertThat(result.categories).containsExactly(Category(1L, "Drama", "drama.png"))
    }

    @Test
    fun `should return TvShow with empty categories when mapping from LocalTvShowDto without categories`() {
        val dto = LocalTvShowDto(
            tvShowId = 2L,
            name = "Friends",
            description = "Six friends in NYC",
            poster = "friends.jpg",
            productionYear = 1994,
            rating = 8.9f
        )

        val result = mapper.mapFromLocal(dto)

        assertThat(result.categories).isEmpty()
    }

    @Test
    fun `should return LocalTvShowDto with same fields when mapping from TvShow`() {
        val domain = TvShow(
            id = 3L,
            name = "Stranger Things",
            description = "Mystery in Hawkins",
            poster = "st.jpg",
            productionYear = 2016,
            rating = 8.7f,
            categories = listOf(Category(1L, "Sci-Fi", ""))
        )

        val result = mapper.mapToLocal(domain)

        assertThat(result.tvShowId).isEqualTo(3L)
        assertThat(result.name).isEqualTo("Stranger Things")
        assertThat(result.description).isEqualTo("Mystery in Hawkins")
        assertThat(result.poster).isEqualTo("st.jpg")
        assertThat(result.productionYear).isEqualTo(2016)
        assertThat(result.rating).isEqualTo(8.7f)
    }

    @Test
    fun `should return list of TvShow with categories when mapping from list of LocalTvShowDto`() {
        val dtos = listOf(
            LocalTvShowDto(1L, "BB", "Desc1", "bb.jpg", 2008, 9.5f),
            LocalTvShowDto(2L, "Friends", "Desc2", "friends.jpg", 1994, 8.9f)
        )
        val categoriesMap = mapOf(
            1L to listOf(Category(1L, "Drama", "drama.png")),
            2L to listOf(Category(2L, "Comedy", "comedy.png"))
        )

        val result = mapper.mapListFromLocal(dtos, categoriesMap)

        assertThat(result).hasSize(2)
        assertThat(result[0].categories).containsExactly(Category(1L, "Drama", "drama.png"))
        assertThat(result[1].categories).containsExactly(Category(2L, "Comedy", "comedy.png"))
    }

    @Test
    fun `should return list of TvShow with empty categories when map is empty`() {
        val dtos = listOf(
            LocalTvShowDto(1L, "BB", "Desc1", "bb.jpg", 2008, 9.5f)
        )

        val result = mapper.mapListFromLocal(dtos, emptyMap())

        assertThat(result).hasSize(1)
        assertThat(result[0].categories).isEmpty()
    }

    @Test
    fun `should return list of LocalTvShowDto when mapping from list of TvShow`() {
        val domains = listOf(
            TvShow(1L, "BB", "Desc1", "bb.jpg", 2008, emptyList(), 9.5f),
            TvShow(2L, "Friends", "Desc2", "friends.jpg", 1994, emptyList(), 8.9f)
        )

        val result = mapper.mapListToLocal(domains)

        assertThat(result).hasSize(2)
        assertThat(result[0].name).isEqualTo("BB")
        assertThat(result[1].name).isEqualTo("Friends")
    }

    @Test
    fun `should return empty TvShow list when mapping empty LocalTvShowDto list`() {
        val result = mapper.mapListFromLocal(emptyList(), emptyMap())

        assertThat(result).isEmpty()
    }

    @Test
    fun `should return empty LocalTvShowDto list when mapping empty TvShow list`() {
        val result = mapper.mapListToLocal(emptyList())

        assertThat(result).isEmpty()
    }
}
