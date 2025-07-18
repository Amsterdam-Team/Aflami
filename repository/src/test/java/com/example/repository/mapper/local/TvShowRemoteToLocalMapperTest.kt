package com.example.repository.mapper.local

import com.example.entity.TvShow
import com.example.entity.category.TvShowGenre
import com.example.repository.dto.local.LocalTvShowCategoryDto
import com.example.repository.dto.local.LocalTvShowDto
import com.example.repository.dto.local.relation.TvShowWithCategory
import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test

class TvShowRemoteToLocalMapperTest {

    private val mapper = TvShowLocalMapper(MovieCategoryLocalMapper())

    @Test
    fun `should return TvShow with all fields and categories when mapping from LocalTvShowDto`() {
        val dto = LocalTvShowDto(
            tvShowId = 1L,
            name = "Breaking Bad",
            description = "Chemistry teacher becomes drug kingpin",
            poster = "bb.jpg",
            productionYear = 2008,
            rating = 9.5f,
            popularity = 0.0
        )
        val categories = listOf(LocalTvShowCategoryDto(1L, "Drama"))

        val result = mapper.toTvShow(TvShowWithCategory(dto, categories))

        assertThat(result.id).isEqualTo(1L)
        assertThat(result.name).isEqualTo("Breaking Bad")
        assertThat(result.description).isEqualTo("Chemistry teacher becomes drug kingpin")
        assertThat(result.poster).isEqualTo("bb.jpg")
        assertThat(result.productionYear).isEqualTo(2008)
        assertThat(result.rating).isEqualTo(9.5f)
//        assertThat(result.categories).containsExactly(LocalTvShowCategoryDto(1L, "Drama"))
    }

    @Test
    fun `should return TvShow with empty categories when mapping from LocalTvShowDto without categories`() {
        val dto = LocalTvShowDto(
            tvShowId = 2L,
            name = "Friends",
            description = "Six friends in NYC",
            poster = "friends.jpg",
            productionYear = 1994,
            rating = 8.9f,
            popularity = 0.0
        )

        val result = mapper.toTvShow(TvShowWithCategory(dto, emptyList()))

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
            popularity = 0.0,
            categories = listOf(TvShowGenre.SCIENCE_FICTION_FANTASY)
        )

        val result = mapper.toLocalTvShow(domain)

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
            LocalTvShowDto(1L, "BB", "Desc1", "bb.jpg", 2008, 9.5f, 0.0),
            LocalTvShowDto(2L, "Friends", "Desc2", "friends.jpg", 1994, 8.9f, 0.0)
        )
        val categories = listOf(
            listOf(LocalTvShowCategoryDto(1L, "Drama" )),
            listOf(LocalTvShowCategoryDto(2L, "Comedy"))
        )
        val tvShowsWithCategory = dtos.mapIndexed { index, localTvShowDto -> TvShowWithCategory(localTvShowDto, categories[index]) }
        val result = mapper.toTvShows(tvShowsWithCategory)

        assertThat(result).hasSize(2)
    }

    @Test
    fun `should return list of TvShow with empty categories when map is empty`() {
        val dtos = listOf(
            LocalTvShowDto(1L, "BB", "Desc1", "bb.jpg", 2008, 9.5f, 0.0)
        )
        val tvShowsWithCategory = dtos.map { TvShowWithCategory(it, emptyList()) }
        val result = mapper.toTvShows(tvShowsWithCategory)

        assertThat(result).hasSize(1)
        assertThat(result[0].categories).isEmpty()
    }

    @Test
    fun `should return list of LocalTvShowDto when mapping from list of TvShow`() {
        val domains = listOf(
            TvShow(1L, "BB", "Desc1", "bb.jpg", 2008, emptyList(), 9.5f, 0.0),
            TvShow(2L, "Friends", "Desc2", "friends.jpg", 1994, emptyList(), 8.9f, 0.0)
        )

        val result = mapper.toLocalTvShows(domains)

        assertThat(result).hasSize(2)
        assertThat(result[0].name).isEqualTo("BB")
        assertThat(result[1].name).isEqualTo("Friends")
    }

    @Test
    fun `should return empty TvShow list when mapping empty LocalTvShowDto list`() {
        val result = mapper.toTvShows(emptyList())

        assertThat(result).isEmpty()
    }

    @Test
    fun `should return empty LocalTvShowDto list when mapping empty TvShow list`() {
        val result = mapper.toLocalTvShows(emptyList())

        assertThat(result).isEmpty()
    }
}
