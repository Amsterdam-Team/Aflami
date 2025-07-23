import com.example.domain.exceptions.AflamiException
import com.example.domain.repository.MovieRepository
import com.example.domain.useCase.IncrementMovieGenreInterestUseCase
import com.example.entity.category.MovieGenre
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class IncrementMovieGenreInterestUseCaseTest {

    private lateinit var movieRepository: MovieRepository
    private lateinit var useCase: IncrementMovieGenreInterestUseCase

    @BeforeEach
    fun setUp() {
        movieRepository = mockk(relaxed = true)
        useCase = IncrementMovieGenreInterestUseCase(movieRepository)
    }

    @Test
    fun `should call repository to increment genre interest`() = runTest {
        // Given
        val genre = MovieGenre.ACTION
        coEvery { movieRepository.incrementGenreInterest(genre) } returns Unit

        // When
        useCase(genre)

        // Then
        coVerify(exactly = 1) { movieRepository.incrementGenreInterest(genre) }
    }

    @Test
    fun `should throw AflamiException when repository fails to increment genre interest`() =
        runTest {
            // Given
            val genre = MovieGenre.ACTION
            coEvery { movieRepository.incrementGenreInterest(genre) } throws AflamiException()

            // When & Then
            assertThrows<AflamiException> {
                useCase(genre)
            }
            coVerify(exactly = 1) { movieRepository.incrementGenreInterest(genre) }
        }
}
