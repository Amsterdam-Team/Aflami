import com.example.domain.repository.MovieRepository
import com.example.domain.useCase.IncrementMovieGenreInterestUseCase
import com.example.entity.category.MovieGenre
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.coEvery
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

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
}
