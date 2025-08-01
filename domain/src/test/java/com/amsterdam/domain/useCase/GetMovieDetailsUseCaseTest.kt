package com.amsterdam.domain.useCase

import com.amsterdam.domain.exceptions.AflamiException
import com.amsterdam.domain.repository.MovieRepository
import com.amsterdam.domain.useCase.common.AddMovieWatchHistoryUseCase
import com.amsterdam.domain.useCase.details.GetMovieDetailsUseCase
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
    private lateinit var movieRepository: MovieRepository
    private lateinit var addWatchHistoryUseCase: AddMovieWatchHistoryUseCase
    private lateinit var getMovieDetailsUseCase: GetMovieDetailsUseCase

    private val fakeMovie = Movie(
        id = 1L,
        name = "Test Movie",
        description = "Desc",
        posterUrl = "poster.jpg",
        releaseDate = LocalDate(2020, 1, 1),
        categories = listOf(MovieGenre.ACTION, MovieGenre.DRAMA),
        rating = 8.0f,
        popularity = 100.0,
        originCountry = "USA",
        runTimeInMinutes = 120,
        hasVideo = true,
        productionCompanies = emptyList()
    )
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
            true,
            productionCompanies = emptyList()
        ),
    )
    private val fakeGallery = listOf("gallery1.jpg", "gallery2.jpg")
    private val fakePosters = (1..15).map { "poster$it.jpg" }


    @BeforeEach
    fun setUp() {
        movieRepository = mockk(relaxed = true)
        addWatchHistoryUseCase = mockk(relaxed = true)
        getMovieDetailsUseCase =
            GetMovieDetailsUseCase(movieRepository, addWatchHistoryUseCase)

        coEvery { movieRepository.getMovieDetailsById(any()) } returns GetMovieDetailsUseCase.MovieDetails(
            movie = fakeMovie,
            reviews = fakeReviews,
            actors = fakeActors,
            similarMovies = fakeSimilarMovies,
            movieGallery = fakeGallery,
            moviePosters = fakePosters,
        )
        coJustRun { addWatchHistoryUseCase.invoke(any()) }
    }

    @Test
    fun `should fetch all movie details and return MovieDetails object`() = runTest {
        val movieId = 1L

        val result = getMovieDetailsUseCase(movieId)

        coVerify(exactly = 1) { movieRepository.getMovieDetailsById(movieId) }
        coVerify(exactly = 1) { addWatchHistoryUseCase(movieId) }

        assertThat(result.movie).isEqualTo(fakeMovie)
        assertThat(result.reviews).isEqualTo(fakeReviews)
        assertThat(result.actors).isEqualTo(fakeActors)
        assertThat(result.similarMovies).isEqualTo(fakeSimilarMovies)
        assertThat(result.movieGallery).isEqualTo(fakeGallery)
        assertThat(result.moviePosters).isEqualTo(fakePosters)
    }

    @Test
    fun `should throw AflamiException when getMovieDetailsById fails`() = runTest {
        coEvery { movieRepository.getMovieDetailsById(any()) } throws AflamiException()
        assertThrows<AflamiException> { getMovieDetailsUseCase(1L) }
    }

    @Test
    fun `should not add to watch history if getMovieDetailsById fails`() = runTest {
        coEvery { movieRepository.getMovieDetailsById(any()) } throws AflamiException()

        try {
            getMovieDetailsUseCase(1L)
        } catch (e: AflamiException) {
            coVerify(exactly = 0) { addWatchHistoryUseCase(any()) }
        }
    }

    @Test
    fun `should return MovieDetails with empty lists if repository returns empty for collections`() =
        runTest {
            coEvery { movieRepository.getMovieDetailsById(any()) } returns GetMovieDetailsUseCase.MovieDetails(
                movie = fakeMovie,
                reviews = emptyList(),
                actors = emptyList(),
                similarMovies = emptyList(),
                movieGallery = emptyList(),
                moviePosters = emptyList(),
            )

            val result = getMovieDetailsUseCase(1L)

            assertThat(result.reviews).isEmpty()
            assertThat(result.actors).isEmpty()
            assertThat(result.similarMovies).isEmpty()
            assertThat(result.movieGallery).isEmpty()
            assertThat(result.moviePosters).isEmpty()
        }

    @Test
    fun `should handle the case where MovieDetails data class fields are null`() = runTest {
        coEvery { movieRepository.getMovieDetailsById(any()) } returns GetMovieDetailsUseCase.MovieDetails(
            movie = fakeMovie,
            reviews = emptyList(),
            actors = emptyList(),
            similarMovies = emptyList(),
            movieGallery = emptyList(),
            moviePosters = emptyList(),
        )
        val result = getMovieDetailsUseCase(1L)

        assertThat(result.reviews).isEmpty()
        assertThat(result.actors).isEmpty()
        assertThat(result.similarMovies).isEmpty()
    }
}