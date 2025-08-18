package com.amsterdam.viewmodel.guessMovieByPosterGame

import androidx.lifecycle.viewModelScope
import com.amsterdam.domain.exceptions.AflamiException
import com.amsterdam.domain.exceptions.NotEnoughPointsException
import com.amsterdam.domain.useCase.game.AddPointsToGameUseCase
import com.amsterdam.domain.useCase.game.AddSecondToGameTimeUseCase
import com.amsterdam.domain.useCase.game.CreateGameSessionIdUseCase
import com.amsterdam.domain.useCase.game.guessByPoster.GuessMovieByPosterGameUseCase
import com.amsterdam.domain.utils.AnswerResult
import com.amsterdam.domain.utils.GameQuestion
import com.amsterdam.entity.Game
import com.amsterdam.entity.GameDifficulty
import com.amsterdam.viewmodel.gameResult.ResultScreenData
import com.amsterdam.viewmodel.shared.BaseViewModel
import com.amsterdam.viewmodel.sharedGame.TimerUiState
import com.amsterdam.viewmodel.utils.dispatcher.DispatcherProvider
import com.amsterdam.viewmodel.utils.timer.TimerHandler
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GuessMovieByPosterGameViewModel @Inject constructor(
    private val guessMovieByPosterGameUseCase: GuessMovieByPosterGameUseCase,
    private val createGameSessionIdUseCase: CreateGameSessionIdUseCase,
    private val addPointsToGameUseCase: AddPointsToGameUseCase,
    private val addSecondToGameTimeUseCase: AddSecondToGameTimeUseCase,
    private val timerHandler: TimerHandler,
    private val dispatcherProvider: DispatcherProvider,
    args: GuessMovieByPosterGameArgs
) : BaseViewModel<GuessMovieByPosterUiState, GuessMovieByPosterGameEffect>(
    GuessMovieByPosterUiState(),
    dispatcherProvider
), GuessMovieByPosterInteractionListener {

    private val difficultyType = GameDifficulty.DifficultyType.valueOf(args.difficulty)

    init {
        getQuestions()
    }

    private fun getQuestions() {
        updateState { it.copy(isLoading = true, isNetworkError = false) }
        tryToExecute(
            action = ::startTheGame,
            onSuccess = ::onSuccessGetQuestions,
            onError = ::onError,
            onCompletion = ::onCompletion
        )
    }

    private suspend fun startTheGame(): List<GameQuestion<String>> {
        updateState { it.copy(gameSessionId = createGameSessionIdUseCase()) }
        return guessMovieByPosterGameUseCase.startGame(difficultyType)
    }

    private fun onSuccessGetQuestions(questions: List<GameQuestion<String>>) {
        viewModelScope.launch {
            updateState { it.copy(questions = questions.toQuestionsUiState()) }
            startTheTimer()
        }
    }

    private fun startTheTimer() {
        val currentQuestion = state.value.questions[state.value.currentQuestionIndex]
        viewModelScope.launch(dispatcherProvider.Default) {
            timerHandler.startTimer(
                currentQuestion.questionTimeSeconds,
                onTimerFinish = ::onMoveToNextQuestion
            )
                    .collect(::onTimerUpdate)
        }
    }

    private fun onTimerUpdate(remainingSeconds: Int) {
        val currentQuestion = state.value.questions[state.value.currentQuestionIndex]
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
                guessMovieByPosterGameUseCase.giveHint(currentQuestion.toMoviePosterQuestion())
            },
            onSuccess = { newQuestion ->
                updateState {
                    it.copy(
                        questions = it.questions.toMutableList().apply {
                            set(
                                state.value.currentQuestionIndex,
                                newQuestion.toQuestionUiState().copy(blurRadius = 5)
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
            state.value.questions[state.value.currentQuestionIndex].toMoviePosterQuestion()
        val selectedAnswer = question.choices[selectedAnswerIndex]

        tryToExecute(
            action = {
                guessMovieByPosterGameUseCase.answer(
                    question,
                    selectedAnswer,
                    difficultyType
                )
            },
            onSuccess = { onSuccessSubmitAnswer(it, selectedAnswerIndex) },
            onCompletion = ::onSubmitTheAnswerComplete
        )
    }

    private fun onSuccessSubmitAnswer(
        answerResult: AnswerResult,
        selectedAnswerIndex: Int
    ) {
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

    private fun onSubmitTheAnswerComplete() {
        updateState {
            it.copy(
                questions = it.questions.toMutableList().apply {
                    set(
                        state.value.currentQuestionIndex,
                        it.questions[state.value.currentQuestionIndex].copy(blurRadius = 0)
                    )
                }
            )
        }
        timerHandler.stopTimer()
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
            gameType = Game.GameType.GUESS_MOVIE_BY_POSTER.name,
            gameSessionId = state.value.gameSessionId
        )
        sendNewNavigationEffect(
            GuessMovieByPosterGameEffect.NavigateToGameResult(resultData)
        )
    }

    override fun dismissNotEnoughPointsDialog() {
        updateState { it.copy(isNotEnoughPointsDialogVisible = false) }
    }

    private fun increaseSpentTimeSecondsByOne() {
        addSecondToGameTimeUseCase(state.value.gameSessionId)
    }

    override fun onCloseButtonClicked() {
        timerHandler.stopTimer()
        sendNewNavigationEffect(GuessMovieByPosterGameEffect.NavigateBack)
    }

    override fun onClickRetryLoading() {
        getQuestions()
    }

    private fun onError(error: AflamiException) {
        when (error) {
            is NotEnoughPointsException -> updateState { it.copy(isNotEnoughPointsDialogVisible = true) }
            else -> updateState { it.copy(isNetworkError = true) }
        }
    }
}