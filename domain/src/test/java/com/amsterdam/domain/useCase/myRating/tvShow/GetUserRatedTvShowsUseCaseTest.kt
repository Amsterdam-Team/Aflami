import com.amsterdam.domain.repository.TvShowRepository
import com.amsterdam.domain.useCase.myRating.tvShow.GetUserRatedTvShowsUseCase
import com.amsterdam.domain.useCase.utils.tvShow1
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

class GetUserRatedTvShowsUseCaseTest {

    private val mockRepository = mockk<TvShowRepository>()
    private val useCase = GetUserRatedTvShowsUseCase(mockRepository)

    @Test
    fun `getRatedTvShows returns sorted list by userRate descending`() = runTest {
        // Given
        val show1 = tvShow1
        val show2 = tvShow1.copy(2,"Show B")
        val show3 = tvShow1.copy(3,"Show C")

        val ratedShows = listOf(
            GetUserRatedTvShowsUseCase.UserRatedTvShow(show1, userRate = 5),
            GetUserRatedTvShowsUseCase.UserRatedTvShow(show2, userRate = 8),
            GetUserRatedTvShowsUseCase.UserRatedTvShow(show3, userRate = 7),
        )

        coEvery { mockRepository.getUserRatedTvShows() } returns ratedShows

        // When
        val result = useCase.getRatedTvShows()

        // Then
        assertThat(result).isEqualTo(ratedShows.sortedByDescending { it.userRate })
    }
}
