package com.amsterdam.viewmodel.game

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.amsterdam.domain.repository.GamePointsRepository
import com.amsterdam.domain.useCase.game.DoGuessReleaseGameHintUseCase
import com.amsterdam.domain.useCase.game.GetGameDifficultyByDifficultyTypeUseCase
import com.amsterdam.domain.useCase.game.GuessPosterForMovieGameEngine
import com.amsterdam.domain.useCase.game.GuessReleaseYearForMovieGameEngine
import com.amsterdam.entity.Game
import com.amsterdam.entity.GameDifficulty
import com.amsterdam.viewmodel.shared.BaseViewModel
import com.amsterdam.viewmodel.sharedGame.TimerUiState.TimerColor
import com.amsterdam.viewmodel.utils.dispatcher.DispatcherProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GameViewModel @Inject constructor(
    private val guessPosterForMovieGameEngine: GuessPosterForMovieGameEngine,
    private val guessReleaseYearForMovieGameEngine: GuessReleaseYearForMovieGameEngine,
    private val doGuessReleaseGameHintUseCase: DoGuessReleaseGameHintUseCase,
    private val getGameDifficultyByDifficultyTypeUseCase: GetGameDifficultyByDifficultyTypeUseCase,
    private val gamePointsRepository: GamePointsRepository,
    private val savedStateHandle: SavedStateHandle,
    dispatcherProvider: DispatcherProvider,
) : BaseViewModel<GameUiState, GameEffect>(
    GameUiState(),
    dispatcherProvider,
), GameInteractionListener {

    private val gameType =
        savedStateHandle.get<Game.GameType>("gameType") ?: Game.GameType.GUESS_MOVIE_BY_POSTER

    private val difficultyType =
        savedStateHandle.get<String>("difficulty")
            ?.let { difficultyString ->
                try {
                    GameDifficulty.DifficultyType.valueOf(difficultyString.uppercase())
                } catch (e: IllegalArgumentException) {
                    GameDifficulty.DifficultyType.MEDIUM
                }
            } ?: GameDifficulty.DifficultyType.MEDIUM

    private lateinit var gameDifficulty: GameDifficulty

    init {
        startGame()
    }

    private fun startGame() {
        viewModelScope.launch {
            updateState { it.copy(isLoading = true, gameType = gameType) }
            try {
                gameDifficulty = getGameDifficultyByDifficultyTypeUseCase(difficultyType)

                val questions = when (gameType) {
                    Game.GameType.GUESS_MOVIE_BY_POSTER -> guessPosterForMovieGameEngine.startGame(
                        difficultyType = difficultyType
                    )
                        .map { it.toGameQuestionUiState() }

                    Game.GameType.GUESS_MOVIE_BY_RELEASE -> guessReleaseYearForMovieGameEngine.startGame(
                        difficultyType = difficultyType,
                        onTimerUpdate = { remainingSeconds ->
                            val progress =
                                (remainingSeconds.toFloat() / gameDifficulty.timeLimitSeconds)
                            val timerColor =
                                if (progress > 0.3f) TimerColor.GREEN else TimerColor.RED
                            updateState {
                                it.copy(
                                    timerUiState = it.timerUiState.copy(
                                        currentTimerCount = remainingSeconds,
                                        progress = progress,
                                        currentTimerColor = timerColor
                                    )
                                )
                            }
                        },
                        onTimeFinish = {
                            sendNewEffect(GameEffect.GameOver)
                        }
                    ).map { it.toGameQuestionUiState() }

                    else -> emptyList()
                }

                updateState {
                    it.copy(
                        questions = questions,
                        isLoading = false,
                        currentQuestionIndex = 0,
                        isHintEnabled = true,
                        totalPointsPerQuestion = gameDifficulty.pointsPerQuestion
                    )
                }

                startQuestionTimer()

            } catch (e: Exception) {
                updateState { it.copy(isLoading = false) }
            }
        }
    }

    private fun startQuestionTimer() {
        viewModelScope.launch {
            when (gameType) {
                Game.GameType.GUESS_MOVIE_BY_POSTER -> guessPosterForMovieGameEngine.startQuestionTimer(
                    totalSeconds = gameDifficulty.timeLimitSeconds,
                    onTimerUpdate = { remainingSeconds ->
                        val progress =
                            (remainingSeconds.toFloat() / gameDifficulty.timeLimitSeconds)
                        val timerColor = if (progress > 0.3f) TimerColor.GREEN else TimerColor.RED
                        updateState {
                            it.copy(
                                timerUiState = it.timerUiState.copy(
                                    currentTimerCount = remainingSeconds,
                                    progress = progress,
                                    currentTimerColor = timerColor
                                )
                            )
                        }
                    },
                    onTimeFinish = {
                        sendNewEffect(GameEffect.GameOver)
                    }
                )

                else -> {}
            }
        }
    }

    override fun onChooseAnswerClick(answerIndex: Int) {
        val currentQuestion = state.value.questions[state.value.currentQuestionIndex]
        val selectedAnswer = currentQuestion.answers[answerIndex]
        val isCorrect = selectedAnswer == currentQuestion.correctAnswer

        updateState {
            it.copy(
                selectedAnswerIndex = answerIndex,
                isAnswerCorrect = isCorrect,
                isNextEnabled = true,
                score = if (isCorrect) it.score + gameDifficulty.pointsPerQuestion else it.score
            )
        }
    }

    override fun onUseHint() {
        when (state.value.gameType) {
            Game.GameType.GUESS_MOVIE_BY_POSTER -> {
                updateState { it.copy(isHintEnabled = false) }
            }

            Game.GameType.GUESS_MOVIE_BY_RELEASE -> {
                val currentQuestion = state.value.questions[state.value.currentQuestionIndex]
                val updatedQuestion =
                    doGuessReleaseGameHintUseCase(currentQuestion.toMovieReleasedDateQuestion())
                updateState {
                    it.copy(
                        questions =
                            it.questions
                                .toMutableList()
                                .apply {
                                    set(
                                        state.value.currentQuestionIndex,
                                        updatedQuestion.toGameQuestionUiState()
                                    )
                                },
                        isHintEnabled = false,
                    )
                }
            }

            else -> {
                updateState { it.copy(isHintEnabled = false) }
            }
        }
    }

    override fun onMoveToNextQuestion() {
        val currentQuestionIndex = state.value.currentQuestionIndex
        val nextQuestionIndex = currentQuestionIndex + 1

        if (nextQuestionIndex < state.value.questions.size) {
            updateState {
                it.copy(
                    currentQuestionIndex = nextQuestionIndex,
                    selectedAnswerIndex = null,
                    isAnswerCorrect = null,
                    isNextEnabled = false,
                    isHintEnabled = true
                )
            }
            startQuestionTimer()
        } else {
            viewModelScope.launch {
                gamePointsRepository.updatePoints(state.value.score)
            }
            sendNewEffect(GameEffect.GameOver)
        }
    }

    override fun onCancelGameClick() {
        sendNewEffect(GameEffect.CancelGame)
    }
}