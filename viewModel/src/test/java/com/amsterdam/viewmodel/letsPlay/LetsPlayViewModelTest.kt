package com.amsterdam.viewmodel.letsPlay

import app.cash.turbine.test
import com.amsterdam.domain.useCase.common.GetTotalUserPointsUseCase
import com.amsterdam.domain.useCase.game.GetAvailableGamesUseCase
import com.amsterdam.entity.Game
import com.amsterdam.entity.GameDifficulty
import com.amsterdam.viewmodel.letsPlay.LetsPlayUiState.GameDifficultyUiState
import com.amsterdam.viewmodel.letsPlay.LetsPlayUiState.GameUiState
import com.amsterdam.viewmodel.utils.TestDispatcherProvider
import com.amsterdam.viewmodel.utils.TestExtension
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@OptIn(ExperimentalCoroutinesApi::class)
@ExtendWith(TestExtension::class)
class LetsPlayViewModelTest {

    private val getTotalUserPointsUseCase: GetTotalUserPointsUseCase = mockk()
    private val getAvailableGamesUseCase: GetAvailableGamesUseCase = mockk()

    private val viewModel by lazy {
        LetsPlayViewModel(
            getTotalUserPointsUseCase = getTotalUserPointsUseCase,
            getAvailableGamesUseCase = getAvailableGamesUseCase,
            dispatcherProvider = TestDispatcherProvider()
        )
    }

    @BeforeEach
    fun setUp() {
        coEvery { getTotalUserPointsUseCase() } returns flowOf(1000)
        every { getAvailableGamesUseCase() } returns availableGamesResponse
    }

    @Test
    fun `init should update state with total user points on success`() = runTest {
        val expectedPoints = 2500
        coEvery { getTotalUserPointsUseCase() } returns flowOf(expectedPoints)

        viewModel
        advanceUntilIdle()

        assertThat(viewModel.state.value.totalUserPoint).isEqualTo(expectedPoints)
    }

    @Test
    fun `init should correctly map and update state with available games and difficulties`() =
        runTest {
            viewModel
            advanceUntilIdle()

            assertThat(viewModel.state.value.games).isEqualTo(testGamesUiState)
            assertThat(viewModel.state.value.difficulties).isEqualTo(testDifficultiesUiState)
        }

    @Test
    fun `init should handle exception from getTotalUserPointsUseCase`() = runTest {
        coEvery { getTotalUserPointsUseCase() } throws RuntimeException("Failed to fetch points")

        val initialPoints = viewModel.state.value.totalUserPoint
        viewModel
        advanceUntilIdle()
        assertThat(viewModel.state.value.totalUserPoint).isEqualTo(initialPoints)
    }

    @Test
    fun `onClickGameCard should update state with the selected game type`() = runTest {
        val selectedGameType = Game.GameType.GUESS_MOVIE_BY_POSTER

        viewModel.onClickGameCard(selectedGameType)
        advanceUntilIdle()

        assertThat(viewModel.state.value.selectedGameType).isEqualTo(selectedGameType)
    }

    @Test
    fun `onSelectDifficultyLevel should update state with selected difficulty and enable start button`() =
        runTest {
            val selectedDifficulty = hardDifficulty

            viewModel.onSelectDifficultyLevel(selectedDifficulty)
            advanceUntilIdle()

            assertThat(viewModel.state.value.selectedDifficultyLevel).isEqualTo(selectedDifficulty)
            assertThat(viewModel.state.value.isStartGameButtonEnable).isTrue()
        }

    @Test
    fun `onClickCloseDifficultyLevelDialog should reset selections and disable start button`() =
        runTest {
            viewModel.onClickGameCard(Game.GameType.GUESS_CHARACTER)
            viewModel.onSelectDifficultyLevel(easyDifficulty)
            advanceUntilIdle()

            viewModel.onClickCloseDifficultyLevelDialog()
            advanceUntilIdle()

            assertThat(viewModel.state.value.selectedGameType).isNull()
            assertThat(viewModel.state.value.selectedDifficultyLevel).isNull()
            assertThat(viewModel.state.value.isStartGameButtonEnable).isFalse()
        }

    @Test
    fun `onClickStartGame should send NavigateToGuessCharacterScreen effect when selected`() =
        runTest {
            viewModel.onClickGameCard(Game.GameType.GUESS_CHARACTER)
            viewModel.onSelectDifficultyLevel(mediumDifficulty)
            advanceUntilIdle()

            viewModel.effect.test {
                viewModel.onClickStartGame()
                val expectedEffect =
                    LetsPlayEffect.NavigateToGuessCharacterScreen(mediumDifficulty.difficultyLevel.name)
                assertThat(awaitItem()).isEqualTo(expectedEffect)
            }
        }

    @Test
    fun `onClickStartGame should send NavigateToGuessMovieByReleaseScreen effect when selected`() =
        runTest {
            viewModel.onClickGameCard(Game.GameType.GUESS_RELEASE_YEAR)
            viewModel.onSelectDifficultyLevel(hardDifficulty)
            advanceUntilIdle()

            viewModel.effect.test {
                viewModel.onClickStartGame()
                val expectedEffect =
                    LetsPlayEffect.NavigateToGuessMovieByReleaseScreen(hardDifficulty.difficultyLevel.name)
                assertThat(awaitItem()).isEqualTo(expectedEffect)
            }
        }

    @Test
    fun `onClickStartGame should send NavigateToGuessMovieByPosterScreen effect when selected`() =
        runTest {
            viewModel.onClickGameCard(Game.GameType.GUESS_MOVIE_BY_POSTER)

            viewModel.onSelectDifficultyLevel(easyDifficulty)
            advanceUntilIdle()

            viewModel.effect.test {
                viewModel.onClickStartGame()
                val expectedEffect =
                    LetsPlayEffect.NavigateToGuessMovieByPosterScreen(easyDifficulty.difficultyLevel.name)
                assertThat(awaitItem()).isEqualTo(expectedEffect)
            }
        }

    @Test
    fun `onClickStartGame should send NavigateToGuessMovieByGenreScreen effect when selected`() =
        runTest {
            viewModel.onClickGameCard(Game.GameType.GUESS_GENRE)

            viewModel.onSelectDifficultyLevel(mediumDifficulty)
            advanceUntilIdle()

            viewModel.effect.test {
                viewModel.onClickStartGame()
                val expectedEffect =
                    LetsPlayEffect.NavigateToGuessMovieByGenreScreen(mediumDifficulty.difficultyLevel.name)
                assertThat(awaitItem()).isEqualTo(expectedEffect)
            }
        }

    @Test
    fun `onClickStartGame should do nothing if difficulty level is not selected`() = runTest {
        viewModel.onClickGameCard(Game.GameType.GUESS_CHARACTER)
        advanceUntilIdle()

        viewModel.effect.test {
            viewModel.onClickStartGame()
            expectNoEvents()
        }
    }

    @Test
    fun `onClickStartGame should do nothing if game type is not selected`() = runTest {
        viewModel.onSelectDifficultyLevel(easyDifficulty)
        advanceUntilIdle()

        viewModel.effect.test {
            viewModel.onClickStartGame()
            expectNoEvents()
        }
    }


    private val availableGamesResponse = GetAvailableGamesUseCase.AvailableGames(
        games = listOf(
            Game(gameType = Game.GameType.GUESS_CHARACTER, requiredPoints = 0),
            Game(gameType = Game.GameType.GUESS_MOVIE_BY_POSTER, requiredPoints = 0),
            Game(gameType = Game.GameType.GUESS_RELEASE_YEAR, requiredPoints = 400),
            Game(gameType = Game.GameType.GUESS_GENRE, requiredPoints = 400)
        ),
        difficultyLevels = listOf(
            GameDifficulty(5, 45, 5, GameDifficulty.DifficultyType.EASY),
            GameDifficulty(10, 30, 10, GameDifficulty.DifficultyType.MEDIUM),
            GameDifficulty(20, 10, 20, GameDifficulty.DifficultyType.HARD)
        )
    )

    private val easyDifficulty =
        GameDifficultyUiState(5, 45, 5, GameDifficultyUiState.DifficultyLevelUiState.EASY)
    private val mediumDifficulty =
        GameDifficultyUiState(10, 30, 10, GameDifficultyUiState.DifficultyLevelUiState.MEDIUM)
    private val hardDifficulty =
        GameDifficultyUiState(20, 10, 20, GameDifficultyUiState.DifficultyLevelUiState.HARD)

    private val testDifficultiesUiState = listOf(easyDifficulty, mediumDifficulty, hardDifficulty)
    private val testGamesUiState = listOf(
        GameUiState(Game.GameType.GUESS_CHARACTER, 0),
        GameUiState(Game.GameType.GUESS_MOVIE_BY_POSTER, 0),
        GameUiState(Game.GameType.GUESS_RELEASE_YEAR, 400),
        GameUiState(Game.GameType.GUESS_GENRE, 400)
    )
}