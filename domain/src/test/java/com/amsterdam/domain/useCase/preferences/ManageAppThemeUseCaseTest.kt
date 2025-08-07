import com.amsterdam.domain.repository.AppPreferencesRepository
import com.amsterdam.domain.useCase.preferences.ManageAppThemeUseCase
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class ManageAppThemeUseCaseTest {

    private lateinit var preferencesRepository: AppPreferencesRepository
    private lateinit var manageAppThemeUseCase: ManageAppThemeUseCase

    @Before
    fun setup() {
        preferencesRepository = mockk(relaxed = true)
        manageAppThemeUseCase = ManageAppThemeUseCase(preferencesRepository)
    }

    @Test
    fun `setAppTheme should call repository with correct value`() = runTest {
        // Given
        val isDark = true
        coEvery { preferencesRepository.setAppTheme(isDark) } returns Unit

        // When
        manageAppThemeUseCase.setAppTheme(isDark)

        // Then
        coVerify { preferencesRepository.setAppTheme(isDark) }
    }

    @Test
    fun `getAppTheme should return expected theme`() = runTest {
        // Given
        val expected = true
       coEvery{ preferencesRepository.getAppTheme() } returns flowOf(expected)

        // When
        val themeFlow = manageAppThemeUseCase.getAppTheme()
        var actual: Boolean? = null
        themeFlow.collect { value ->
            actual = value
        }

        // Then
        assertThat(actual).isEqualTo(expected)
        coVerify { preferencesRepository.getAppTheme() }
    }
}
