package com.example.domain.useCase

import com.example.domain.exceptions.NoSearchByKeywordResultFoundException
import com.example.domain.repository.TvShowRepository
import com.example.entity.Category
import com.example.entity.TvShow
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class GetTvShowByKeywordUseCaseTest {
    private lateinit var tvShowRepository: TvShowRepository
    private lateinit var getTvShowByKeywordUseCase: GetTvShowByKeywordUseCase


    @BeforeEach
    fun setUp() {
        tvShowRepository = mockk(relaxed = true)
        getTvShowByKeywordUseCase = GetTvShowByKeywordUseCase(tvShowRepository)
    }

    @Test
    fun `should call getTvShowByKeyword from tvShowRepository`() =
        runBlocking {
            coEvery { tvShowRepository.getTvShowByKeyword(any()) } returns fakeTvShowList

            getTvShowByKeywordUseCase("keyword")
            coVerify { tvShowRepository.getTvShowByKeyword("keyword") }
        }

    @Test
    fun `should return a list of tv shows sorted by popularity`() =
        runBlocking {
            coEvery { tvShowRepository.getTvShowByKeyword(any()) } returns fakeTvShowList
            val tvShows = getTvShowByKeywordUseCase("keyword")
            assertThat(fakeTvShowList.sortedByDescending { it.popularity }).isEqualTo(tvShows)
        }

    @Test
    fun `should throw NoSearchByKeywordResultFoundException if repository returns empty list`(): Unit =
        runBlocking {
            coEvery { tvShowRepository.getTvShowByKeyword(any()) } returns emptyList()

            assertThrows<NoSearchByKeywordResultFoundException> {
                getTvShowByKeywordUseCase("nonexistentKeyword")
            }
        }

    @Test
    fun `should throw NoSearchByKeywordResultFoundException if filters result in empty list`(): Unit =
        runBlocking {
            val specificTvShowList = listOf(
                TvShow(
                    id = 1,
                    name = "Low Rated",
                    description = "",
                    poster = "",
                    productionYear = 2023,
                    categories = listOf(),
                    rating = 1.0f,
                    popularity = 5.0
                ),
                TvShow(
                    id = 2,
                    name = "Wrong Category",
                    description = "",
                    poster = "",
                    productionYear = 2023,
                    categories = listOf(
                        Category(id = 99, name = "Other", image = "")
                    ),
                    rating = 5.0f,
                    popularity = 5.0
                )
            )
            coEvery { tvShowRepository.getTvShowByKeyword(any()) } returns specificTvShowList

            assertThrows<NoSearchByKeywordResultFoundException> {
                getTvShowByKeywordUseCase("keyword", rating = 10, tvShowGenreId = 1)
            }
        }

    @Test
    fun `should filter tv shows by minimum rating`() = runBlocking {
        coEvery { tvShowRepository.getTvShowByKeyword(any()) } returns fakeTvShowListWithRatings

        val result = getTvShowByKeywordUseCase("keyword", rating = 6)

        assertThat(result).hasSize(2)

        assertThat(result[0].id).isEqualTo(1)
        assertThat(result[1].id).isEqualTo(2)
    }

    @Test
    fun `should return all tv shows when rating is 0`() = runBlocking {
        coEvery { tvShowRepository.getTvShowByKeyword(any()) } returns fakeTvShowListWithRatings

        val result = getTvShowByKeywordUseCase("keyword", rating = 0)

        assertThat(result).isEqualTo(fakeTvShowListWithRatings.sortedByDescending { it.popularity })
    }

    @Test
    fun `should filter tv shows by genre ID`(): Unit = runBlocking {
        coEvery { tvShowRepository.getTvShowByKeyword(any()) } returns fakeTvShowListWithCategories

        val result = getTvShowByKeywordUseCase("keyword", tvShowGenreId = 10)

        assertThat(result).hasSize(2)
        assertThat(result).containsExactly(
            fakeTvShowListWithCategories[2],
            fakeTvShowListWithCategories[0]
        )
    }

    @Test
    fun `should return all tv shows when tvShowGenreId is 0`() = runBlocking {
        coEvery { tvShowRepository.getTvShowByKeyword(any()) } returns fakeTvShowListWithCategories


        val result = getTvShowByKeywordUseCase("keyword", tvShowGenreId = 0)

        assertThat(result).isEqualTo(fakeTvShowListWithCategories.sortedByDescending { it.popularity })
    }

    @Test
    fun `should return empty list if no tv shows match the genre`(): Unit = runBlocking {
        coEvery { tvShowRepository.getTvShowByKeyword(any()) } returns fakeTvShowListWithCategories
        assertThrows<NoSearchByKeywordResultFoundException> {
            getTvShowByKeywordUseCase("keyword", tvShowGenreId = 999)
        }
    }


    private val fakeTvShowList =
        listOf(
            TvShow(
                id = 1,
                name = "abc",
                description = "",
                poster = "",
                productionYear = 2023,
                categories = emptyList(),
                rating = 2.5f,
                popularity = 10.2,
            ),
            TvShow(
                id = 2,
                name = "dfg",
                description = "",
                poster = "",
                productionYear = 2023,
                categories = emptyList(),
                rating = 2.5f,
                popularity = 11.2,
            ),
            TvShow(
                id = 3,
                name = "hij",
                description = "",
                poster = "",
                productionYear = 2023,
                categories = emptyList(),
                rating = 2.5f,
                popularity = 0.2,
            ),
        )
    private val fakeTvShowListWithRatings =
        listOf(
            TvShow(
                id = 1,
                name = "High Rated",
                description = "",
                poster = "",
                productionYear = 2023,
                categories = emptyList(),
                rating = 8.0f,
                popularity = 10.0
            ),
            TvShow(
                id = 2,
                name = "Medium Rated",
                description = "",
                poster = "",
                productionYear = 2023,
                categories = emptyList(),
                rating = 5.5f,
                popularity = 9.0
            ),
            TvShow(
                id = 3,
                name = "Low Rated",
                description = "",
                poster = "",
                productionYear = 2023,
                categories = emptyList(),
                rating = 3.0f,
                popularity = 8.0
            ),
        )
    private val fakeTvShowListWithCategories =
        listOf(
            TvShow(
                id = 1,
                name = "Action Show",
                description = "",
                poster = "",
                productionYear = 2023,
                categories = listOf(Category(id = 10L, name = "Action", image = "")),
                rating = 8.0f,
                popularity = 10.0
            ),
            TvShow(
                id = 2,
                name = "Comedy Show",
                description = "",
                poster = "",
                productionYear = 2023,
                categories = listOf(Category(id = 20L, name = "Comedy", image = "")),
                rating = 7.0f,
                popularity = 9.0
            ),
            TvShow(
                id = 3,
                name = "Action & Drama",
                description = "",
                poster = "",
                productionYear = 2023,
                categories = listOf(
                    Category(id = 10L, name = "Action", image = ""),
                    Category(id = 30L, name = "Drama", image = "")
                ),
                rating = 7.5f,
                popularity = 11.0
            ),
            TvShow(
                id = 4,
                name = "Thriller",
                description = "",
                poster = "",
                productionYear = 2023,
                categories = listOf(Category(id = 40L, name = "Thriller", image = "")),
                rating = 6.0f,
                popularity = 8.0
            )
        )

}
