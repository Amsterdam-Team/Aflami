import com.amsterdam.domain.repository.TvShowRepository
import com.amsterdam.domain.useCase.myRating.tvShow.SetUserTvShowRatingUseCase
import com.google.common.truth.Truth.assertThat
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class SetUserTvShowRatingUseCaseTest {

    private lateinit var tvShowRepository: TvShowRepository
    private lateinit var setUserTvShowRatingUseCase: SetUserTvShowRatingUseCase

    @BeforeEach
    fun setUp() {
        tvShowRepository = mockk(relaxed = true)
        setUserTvShowRatingUseCase = SetUserTvShowRatingUseCase(tvShowRepository)
    }

    @Test
    fun `setUserMovieRate should call setTvShowRate on repository with correct parameters`() = runTest {
        // Given
        val rate = 8
        val tvShowId = 123L
        coEvery { tvShowRepository.setTvShowRate(rate = rate, tvShowId = tvShowId) } just Runs
        // When
        val result = setUserTvShowRatingUseCase.setUserMovieRate(rate, tvShowId)

        // Then
        assertThat(result).isEqualTo(Unit)
        coVerify { tvShowRepository.setTvShowRate(rate = rate, tvShowId = tvShowId) }
    }
}
