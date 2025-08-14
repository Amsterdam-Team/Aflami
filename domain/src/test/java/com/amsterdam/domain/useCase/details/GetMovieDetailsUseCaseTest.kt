package com.amsterdam.domain.useCase.details

import com.amsterdam.domain.exceptions.AflamiException
import com.amsterdam.domain.repository.MovieRepository
import com.amsterdam.domain.useCase.common.AddMovieWatchHistoryUseCase
import com.amsterdam.domain.useCase.utils.fakeMovieList
import com.amsterdam.entity.Actor
import com.amsterdam.entity.Gender
import com.amsterdam.entity.Movie
import com.amsterdam.entity.Review
import com.amsterdam.entity.category.MovieGenre
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coJustRun
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.LocalDate
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class GetMovieDetailsUseCaseTest {
    private val movieRepository: MovieRepository = mockk(relaxed = true)
    private val addWatchHistoryUseCase: AddMovieWatchHistoryUseCase = mockk(relaxed = true)
    private val getMovieDetailsUseCase by lazy {
        GetMovieDetailsUseCase(movieRepository, addWatchHistoryUseCase)
    }

    private val fakeReviews = listOf(
        Review(1, "Reviewer1", "user1", 4.5f, "Great", LocalDate(2023, 1, 1), "url1"),
    )
    private val fakeActors = listOf(
        Actor(1, "Actor A", "a.jpg", 90.0, Gender.Male),
        Actor(2, "Actor B", "b.jpg", 80.0, Gender.Female)
    )
    private val fakeSimilarMovies = listOf(
        Movie(
            2L,
            "Similar Movie",
            "Desc",
            "poster2.jpg",
            LocalDate(2021, 1, 1),
            listOf(MovieGenre.ACTION),
            7.0f,
            90.0,
            "USA",
            100,
        ),
    )
    private val fakeGallery = listOf("gallery1.jpg", "gallery2.jpg")
    private val fakePosters = (1..15).map { "poster$it.jpg" }


    @BeforeEach
    fun setUp() {
        coEvery { movieRepository.getMovieDetailsById(any()) } returns fakeMovieDetails

        coJustRun { addWatchHistoryUseCase.invoke(any()) }
    }

    @Test
    fun `should return MovieDetails object when called with valid movieId`() = runTest {
        val movieId = 1L

        val result = getMovieDetailsUseCase(movieId)

        coVerify(exactly = 1) { movieRepository.getMovieDetailsById(movieId) }
        coVerify(exactly = 1) { addWatchHistoryUseCase(movieId) }

        assertThat(result).isEqualTo(fakeMovieDetails)
    }

    @Test
    fun `should throw AflamiException when getMovieDetailsById fails`() = runTest {
        coEvery { movieRepository.getMovieDetailsById(any()) } throws AflamiException()

        assertThrows<AflamiException> { getMovieDetailsUseCase(1L) }
    }

    @Test
    fun `should not add to watch history when getMovieDetailsById fails`() = runTest {
        coEvery { movieRepository.getMovieDetailsById(any()) } throws AflamiException()

        assertThrows<AflamiException> { getMovieDetailsUseCase(1L) }

        coVerify(exactly = 0) { addWatchHistoryUseCase(any()) }
    }

    @Test
    fun `should return MovieDetails with empty lists when repository returns empty for collections`() =
        runTest {
            coEvery { movieRepository.getMovieDetailsById(any()) } returns fakeEmptyMovieDetails

            val result = getMovieDetailsUseCase(1L)

            assertThat(result).isEqualTo(fakeEmptyMovieDetails)
        }

    @Test
    fun `should handle the case where MovieDetails data class fields are null`() = runTest {
        coEvery { movieRepository.getMovieDetailsById(any()) } returns fakeEmptyMovieDetails
        val result = getMovieDetailsUseCase(1L)

        assertThat(result).isEqualTo(fakeEmptyMovieDetails)
    }

    private val fakeEmptyMovieDetails = GetMovieDetailsUseCase.MovieDetails(
        movie = fakeMovieList.first(),
        reviews = emptyList(),
        actors = emptyList(),
        similarMovies = emptyList(),
        movieGallery = emptyList(),
        moviePosters = emptyList(),
        productionCompanies = emptyList(),
        userRate = null,
    )

    private val fakeMovieDetails = GetMovieDetailsUseCase.MovieDetails(
        movie = fakeMovieList.first(),
        reviews = fakeReviews,
        actors = fakeActors,
        similarMovies = fakeSimilarMovies,
        movieGallery = fakeGallery,
        moviePosters = fakePosters,
        productionCompanies = emptyList(),
        userRate = null,
    )
}