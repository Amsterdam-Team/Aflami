package com.amsterdam.domain.useCase


import com.amsterdam.domain.exceptions.AflamiException
import com.amsterdam.domain.repository.MovieRepository
import com.amsterdam.domain.useCase.details.GetMovieDetailsUseCase
import com.amsterdam.entity.Actor
import com.amsterdam.entity.Gender
import com.amsterdam.entity.Movie
import com.amsterdam.entity.ProductionCompany
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
    private lateinit var incrementMovieGenreInterestUseCase: IncrementMovieGenreInterestUseCase
    private lateinit var getMovieDetailsUseCase: GetMovieDetailsUseCase

    private val fakeMovie = Movie(
        id = 1L, name = "Test Movie", description = "Desc", posterUrl = "poster.jpg",
        productionYear = (2020).toUInt(), categories = listOf(MovieGenre.ACTION, MovieGenre.DRAMA),
        rating = 8.0f, popularity = 100.0, originCountry = "USA", runTimeInMinutes = 120, hasVideo = true
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
            2L, "Similar Movie", "Desc", "poster2.jpg", (2021).toUInt(),
            listOf(MovieGenre.ACTION), 7.0f, 90.0, "USA", 100, true
        ),
    )
    private val fakeGallery = listOf("gallery1.jpg", "gallery2.jpg")
    private val fakePosters = (1..15).map { "poster$it.jpg" }
    private val fakeProductionCompanies = listOf(
        ProductionCompany(1, "logo.jpg", "Company A", "USA")
    )


    @BeforeEach
    fun setUp() {
        movieRepository = mockk(relaxed = true)
        incrementMovieGenreInterestUseCase = mockk(relaxed = true)
        getMovieDetailsUseCase =
            GetMovieDetailsUseCase(movieRepository, incrementMovieGenreInterestUseCase)

        coEvery { movieRepository.getMovieDetailsById(any()) } returns fakeMovie
        coEvery { movieRepository.getMovieReviews(any()) } returns fakeReviews
        coEvery { movieRepository.getActorsByMovieId(any()) } returns fakeActors
        coEvery { movieRepository.getSimilarMovies(any()) } returns fakeSimilarMovies
        coEvery { movieRepository.getMovieGallery(any()) } returns fakeGallery
        coEvery { movieRepository.getMoviePosters(any()) } returns fakePosters
        coEvery { movieRepository.getProductionCompany(any()) } returns fakeProductionCompanies
        coJustRun { incrementMovieGenreInterestUseCase.invoke(any()) }
    }

    @Test
    fun `should fetch all movie details and return MovieDetails object`() = runTest {
        val movieId = 1L

        val result = getMovieDetailsUseCase(movieId)

        coVerify(exactly = 1) { movieRepository.getMovieDetailsById(movieId) }
        coVerify(exactly = 1) { movieRepository.getMovieReviews(movieId) }
        coVerify(exactly = 1) { movieRepository.getActorsByMovieId(movieId) }
        coVerify(exactly = 1) { movieRepository.getSimilarMovies(movieId) }
        coVerify(exactly = 1) { movieRepository.getMovieGallery(movieId) }
        coVerify(exactly = 1) { movieRepository.getMoviePosters(movieId) }
        coVerify(exactly = 1) { movieRepository.getProductionCompany(movieId) }

        assertThat(result.movie).isEqualTo(fakeMovie)
        assertThat(result.reviews).isEqualTo(fakeReviews)
        assertThat(result.actors).isEqualTo(fakeActors)
        assertThat(result.similarMovies).isEqualTo(fakeSimilarMovies)
        assertThat(result.movieGallery).isEqualTo(fakeGallery)
        assertThat(result.moviePosters).hasSize(10)
        assertThat(result.moviePosters).isEqualTo(fakePosters.take(10))
        assertThat(result.productionsCompanies).isEqualTo(fakeProductionCompanies)
        assertThat(result.categories).isEqualTo(fakeMovie.categories)

        coVerify(exactly = 1) { incrementMovieGenreInterestUseCase(MovieGenre.ACTION) }
        coVerify(exactly = 1) { incrementMovieGenreInterestUseCase(MovieGenre.DRAMA) }
    }

    @Test
    fun `should throw AflamiException when getMovieDetailsById fails`() = runTest {
        coEvery { movieRepository.getMovieDetailsById(any()) } throws AflamiException()
        assertThrows<AflamiException> { getMovieDetailsUseCase(1L) }
    }

    @Test
    fun `should throw AflamiException when getMovieReviews fails`() = runTest {
        coEvery { movieRepository.getMovieReviews(any()) } throws AflamiException()
        assertThrows<AflamiException> { getMovieDetailsUseCase(1L) }
    }

    @Test
    fun `should throw AflamiException when getActorsByMovieId fails`() = runTest {
        coEvery { movieRepository.getActorsByMovieId(any()) } throws AflamiException()
        assertThrows<AflamiException> { getMovieDetailsUseCase(1L) }
    }

    @Test
    fun `should throw AflamiException when getSimilarMovies fails`() = runTest {
        coEvery { movieRepository.getSimilarMovies(any()) } throws AflamiException()
        assertThrows<AflamiException> { getMovieDetailsUseCase(1L) }
    }

    @Test
    fun `should throw AflamiException when getMovieGallery fails`() = runTest {
        coEvery { movieRepository.getMovieGallery(any()) } throws AflamiException()
        assertThrows<AflamiException> { getMovieDetailsUseCase(1L) }
    }

    @Test
    fun `should throw AflamiException when getMoviePosters fails`() = runTest {
        coEvery { movieRepository.getMoviePosters(any()) } throws AflamiException()
        assertThrows<AflamiException> { getMovieDetailsUseCase(1L) }
    }

    @Test
    fun `should throw AflamiException when getProductionCompany fails`() = runTest {
        coEvery { movieRepository.getProductionCompany(any()) } throws AflamiException()
        assertThrows<AflamiException> { getMovieDetailsUseCase(1L) }
    }

    @Test
    fun `should call incrementGenreInterest for each movie category`() = runTest {
        val movieWithMultipleCategories = fakeMovie.copy(
            categories = listOf(
                MovieGenre.ACTION,
                MovieGenre.COMEDY,
                MovieGenre.DRAMA
            )
        )
        coEvery { movieRepository.getMovieDetailsById(any()) } returns movieWithMultipleCategories

        getMovieDetailsUseCase(1L)

        coVerify(exactly = 1) { incrementMovieGenreInterestUseCase(MovieGenre.ACTION) }
        coVerify(exactly = 1) { incrementMovieGenreInterestUseCase(MovieGenre.COMEDY) }
        coVerify(exactly = 1) { incrementMovieGenreInterestUseCase(MovieGenre.DRAMA) }
    }

    @Test
    fun `should not call incrementGenreInterest if movie has no categories`() = runTest {
        val movieWithNoCategories = fakeMovie.copy(categories = emptyList())
        coEvery { movieRepository.getMovieDetailsById(any()) } returns movieWithNoCategories

        getMovieDetailsUseCase(1L)

        coVerify(exactly = 0) { incrementMovieGenreInterestUseCase(any()) }
    }

    @Test
    fun `should return MovieDetails with empty lists if repository returns empty for collections`() =
        runTest {
            coEvery { movieRepository.getMovieReviews(any()) } returns emptyList()
            coEvery { movieRepository.getActorsByMovieId(any()) } returns emptyList()
            coEvery { movieRepository.getSimilarMovies(any()) } returns emptyList()
            coEvery { movieRepository.getMovieGallery(any()) } returns emptyList()
            coEvery { movieRepository.getMoviePosters(any()) } returns emptyList()
            coEvery { movieRepository.getProductionCompany(any()) } returns emptyList()

            val result = getMovieDetailsUseCase(1L)

            assertThat(result.reviews).isEmpty()
            assertThat(result.actors).isEmpty()
            assertThat(result.similarMovies).isEmpty()
            assertThat(result.movieGallery).isEmpty()
            assertThat(result.moviePosters).isEmpty()
            assertThat(result.productionsCompanies).isEmpty()
        }

    @Test
    fun `should handle moviePosters when less than 10 are returned`() = runTest {
        val fewerPosters = listOf("p1.jpg", "p2.jpg", "p3.jpg")
        coEvery { movieRepository.getMoviePosters(any()) } returns fewerPosters

        val result = getMovieDetailsUseCase(1L)

        assertThat(result.moviePosters).hasSize(3)
        assertThat(result.moviePosters).isEqualTo(fewerPosters)
    }

    @Test
    fun `should handle moviePosters when exactly 10 are returned`() = runTest {
        val exactlyTenPosters = (1..10).map { "p$it.jpg" }
        coEvery { movieRepository.getMoviePosters(any()) } returns exactlyTenPosters

        val result = getMovieDetailsUseCase(1L)

        assertThat(result.moviePosters).hasSize(10)
        assertThat(result.moviePosters).isEqualTo(exactlyTenPosters)
    }
}