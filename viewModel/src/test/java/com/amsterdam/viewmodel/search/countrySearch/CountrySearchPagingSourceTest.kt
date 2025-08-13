package com.amsterdam.viewmodel.search.countrySearch

import androidx.paging.testing.asSnapshot
import com.amsterdam.domain.useCase.search.GetMoviesByCountryUseCase
import com.amsterdam.entity.Country
import com.amsterdam.viewmodel.utils.entityHelper.createMovie
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

class CountrySearchPagingSourceTest {
    private val getMoviesByCountryUseCase: GetMoviesByCountryUseCase = mockk(relaxed = true)
    private val countrySearchPagingSource = CountrySearchPagingSource(getMoviesByCountryUseCase)

    @Test
    fun `should return flow of PagingData when getMovies is called`() = runTest {
        val expectedMovies = listOf(createMovie(id = 1))
        coEvery { getMoviesByCountryUseCase(Country("Egypt", "eg")) } returns expectedMovies

        val flow = countrySearchPagingSource.getMovies(Country("Egypt", "eg"))
        assertThat(flow.asSnapshot().first()).isEqualTo(expectedMovies.first())
    }
}