package com.amsterdam.viewmodel.guessReleseDateGame

import androidx.lifecycle.viewModelScope
import com.amsterdam.domain.exceptions.AflamiException
import com.amsterdam.domain.exceptions.NotEnoughPointsException
import com.amsterdam.domain.useCase.game.AddPointsToGameUseCase
import com.amsterdam.domain.useCase.game.AddSecondToGameTimeUseCase
import com.amsterdam.domain.useCase.game.CreateGameSessionIdUseCase
import com.amsterdam.domain.useCase.game.releaseYear.GuessReleaseYearGameUseCase
import com.amsterdam.domain.utils.AnswerResult
import com.amsterdam.domain.utils.GameQuestion
import com.amsterdam.entity.Game
import com.amsterdam.entity.GameDifficulty.DifficultyType
import com.amsterdam.viewmodel.gameResult.ResultScreenData
import com.amsterdam.viewmodel.shared.BaseViewModel
import com.amsterdam.viewmodel.sharedGame.TimerUiState
import com.amsterdam.viewmodel.utils.dispatcher.DispatcherProvider
import com.amsterdam.viewmodel.utils.timer.TimerHandler
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GuessReleaseYearGameViewModel @Inject constructor(
    private val guessReleaseYearForMovieGameUseCase: GuessReleaseYearGameUseCase,
    private val createGameSessionIdUseCase: CreateGameSessionIdUseCase,
    private val addPointsToGameUseCase: AddPointsToGameUseCase,
    private val addSecondToGameTimeUseCase: AddSecondToGameTimeUseCase,
    private val timerHandler: TimerHandler,
    private val dispatcherProvider: DispatcherProvider,
    args: GuessReleaseYearGameArgs
) : BaseViewModel<GuessReleaseYearUiState, GuessReleaseYearGameEffect>(
    GuessReleaseYearUiState(),
    dispatcherProvider
), GuessReleaseYearInteractionListener {
    private val difficultyType = DifficultyType.valueOf(args.difficulty)

    init {
        fetchQuestions()
    }

    private fun fetchQuestions() {
        updateState { it.copy(isLoading = true) }
        tryToExecute(
            action = ::startTheGame,
            onSuccess = ::onSuccessGetQuestions,
            onError = ::onError,
            onCompletion = ::onCompletion
        )
    }

    private suspend fun startTheGame(): List<GameQuestion<Int>> {
        updateState { it.copy(gameSessionId = createGameSessionIdUseCase()) }
        return guessReleaseYearForMovieGameUseCase.startGame(difficultyType)
    }

    private fun onSuccessGetQuestions(questions: List<GameQuestion<Int>>) {
        viewModelScope.launch {
            updateState { it.copy(questions = questions.toQuestionsUiStateUiState()) }
            startTheTimer()
        }
    }

    private fun startTheTimer() {
        val currentQuestion =
            state.value.questions[state.value.currentQuestionIndex]
        viewModelScope.launch(dispatcherProvider.Default) {
            timerHandler.startTimer(
                currentQuestion.questionTimeSeconds,
                onTimerFinish = ::onMoveToNextQuestion
            )
                    .collect(::onTimerUpdate)
        }
    }

    private fun onTimerUpdate(remainingSeconds: Int) {
        val currentQuestion =
            state.value.questions[state.value.currentQuestionIndex]
        updateState {
            it.copy(
                timerUiState = TimerUiState(
                    currentTimerCount = remainingSeconds,
                    currentTimerColor = if (remainingSeconds > 5) TimerUiState.TimerColor.GREEN else TimerUiState.TimerColor.RED,
                    progress = remainingSeconds.toFloat() / currentQuestion.questionTimeSeconds
                )
            )
        }
        increaseSpentTimeSecondsByOne()
    }

    private fun onCompletion() {
        updateState { it.copy(isLoading = false) }
    }

    override fun onHintClicked() {
        if (state.value.isNextEnabled) return
        tryToExecute(
            action = {
                val currentQuestion = state.value.questions[state.value.currentQuestionIndex]
                guessReleaseYearForMovieGameUseCase.giveHint(currentQuestion.toMovieReleasedDateQuestion())
            },
            onSuccess = { newQuestion ->
                updateState {
                    it.copy(
                        questions = it.questions.toMutableList().apply {
                            set(
                                state.value.currentQuestionIndex,
                                newQuestion.toQuestionUiStateUiState()
                            )
                        }, isHintEnabled = false
                    )
                }
            },
            onError = ::onError
        )

    }

    override fun onSelectAnswer(selectedAnswerIndex: Int) {
        val question =
            state.value.questions[state.value.currentQuestionIndex].toMovieReleasedDateQuestion()
        val selectedAnswer = question.choices[selectedAnswerIndex]
        tryToExecute(
            action = {
                guessReleaseYearForMovieGameUseCase.answer(
                    question,
                    selectedAnswer,
                    difficultyType
                )
            },
            onSuccess = { onSuccessSubmitAnswer(it, selectedAnswerIndex) },
            onCompletion = timerHandler::stopTimer
        )
    }

    private fun onSuccessSubmitAnswer(answerResult: AnswerResult, selectedAnswerIndex: Int) {
        updateState {
            it.copy(
                isAnswerCorrect = answerResult.isCorrect,
                isNextEnabled = true,
                selectedAnswerIndex = selectedAnswerIndex,
                earnedPoints = answerResult.earnedPoints
            )
        }
        addPointsToGameUseCase(answerResult.earnedPoints, state.value.gameSessionId)

    }

    override fun onMoveToNextQuestion() {
        val currentQuestionIndex = state.value.currentQuestionIndex
        val nextQuestionIndex = currentQuestionIndex + 1
        if (nextQuestionIndex < state.value.questions.size) {
            handleMoveToNextQuestion(nextQuestionIndex)
        } else {
            handleGameFinished()
        }
    }

    private fun handleMoveToNextQuestion(nextQuestionIndex: Int) {
        updateState {
            it.copy(
                currentQuestionIndex = nextQuestionIndex,
                selectedAnswerIndex = null,
                isAnswerCorrect = null,
                isNextEnabled = false,
                isNotEnoughPointsDialogVisible = false,
                earnedPoints = null
            )
        }
        startTheTimer()
    }

    private fun handleGameFinished() {
        val resultData = ResultScreenData(
            difficulty = difficultyType.name,
            gameType = Game.GameType.GUESS_RELEASE_YEAR.name,
            gameSessionId = state.value.gameSessionId
        )
        sendNewNavigationEffect(
            GuessReleaseYearGameEffect.NavigateToGameResult(resultData)
        )
    }

    private fun increaseSpentTimeSecondsByOne() {
        addSecondToGameTimeUseCase(state.value.gameSessionId)
    }

    override fun onCloseButtonClicked() {
        sendNewNavigationEffect(GuessReleaseYearGameEffect.NavigateBack)
    }

    override fun dismissNotEnoughPointsDialog() {
        updateState { it.copy(isNotEnoughPointsDialogVisible = false) }
    }

    override fun onClickClose() {
        sendNewNavigationEffect(GuessReleaseYearGameEffect.NavigateBack)
    }

    private fun onError(error: AflamiException) {
        when (error) {
            is NotEnoughPointsException -> updateState { it.copy(isNotEnoughPointsDialogVisible = true) }
        }
    }
}