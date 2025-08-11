package com.amsterdam.viewmodel.search.actorSearch

import androidx.paging.testing.asSnapshot
import com.amsterdam.domain.useCase.search.GetMoviesByActorUseCase
import com.amsterdam.viewmodel.utils.entityHelper.createMovie
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

class ActorSearchPagingSourceTest {

    private val getMoviesByActorUseCase: GetMoviesByActorUseCase = mockk()
    private val actorSearchPagingSource = ActorSearchPagingSource(getMoviesByActorUseCase)

    @Test
    fun `should return flow of PagingData when getMovies is called`() = runTest {
        val expectedMovies = listOf(createMovie(id = 1))
        coEvery { getMoviesByActorUseCase("Tom Hanks", any(), any()) } returns expectedMovies

        val flow = actorSearchPagingSource.getMovies("Tom Hanks")
        assertThat(flow.asSnapshot().first()).isEqualTo(expectedMovies.first())
    }
}