package com.amsterdam.viewmodel.guessWhichGenre

import androidx.lifecycle.viewModelScope
import com.amsterdam.domain.exceptions.AflamiException
import com.amsterdam.domain.exceptions.NotEnoughPointsException
import com.amsterdam.domain.useCase.game.AddPointsToGameUseCase
import com.amsterdam.domain.useCase.game.AddSecondToGameTimeUseCase
import com.amsterdam.domain.useCase.game.CreateGameSessionIdUseCase
import com.amsterdam.domain.useCase.game.whichGenre.GuessMovieGenreUseCase
import com.amsterdam.domain.utils.AnswerResult
import com.amsterdam.domain.utils.GameQuestion
import com.amsterdam.entity.Game
import com.amsterdam.entity.GameDifficulty.DifficultyType
import com.amsterdam.entity.category.MovieGenre
import com.amsterdam.viewmodel.gameResult.ResultScreenData
import com.amsterdam.viewmodel.shared.BaseViewModel
import com.amsterdam.viewmodel.sharedGame.TimerUiState
import com.amsterdam.viewmodel.utils.dispatcher.DispatcherProvider
import com.amsterdam.viewmodel.utils.timer.TimerHandler
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GuessGenreViewModel @Inject constructor(
    private val guessMovieGenreUseCase: GuessMovieGenreUseCase,
    private val createGameSessionIdUseCase: CreateGameSessionIdUseCase,
    private val addPointsToGameUseCase: AddPointsToGameUseCase,
    private val addSecondToGameTimeUseCase: AddSecondToGameTimeUseCase,
    private val timerHandler: TimerHandler,
    private val dispatcherProvider: DispatcherProvider,
    args: GameGenreArgs,
) : BaseViewModel<GenreGameUiState, GenreGameEffect>(
    GenreGameUiState(),
    dispatcherProvider
), GenreGameInteractionListener {

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

    private suspend fun startTheGame(): List<GameQuestion<MovieGenre>> {
        updateState { it.copy(gameSessionId = createGameSessionIdUseCase()) }
        return guessMovieGenreUseCase.startGame(difficultyType)
    }

    private fun onSuccessGetQuestions(questions: List<GameQuestion<MovieGenre>>) {
        viewModelScope.launch {
            updateState { it.copy(questions = questions.map { it.toUiState() }) }
            startTheTimer()
        }
    }

    private fun startTheTimer() {
        val currentQuestion =
            state.value.questions[state.value.currentQuestionIndex]
        viewModelScope.launch(dispatcherProvider.Default) {
            timerHandler.startTimer(
                currentQuestion.questionTime,
                onTimerFinish = ::onMoveToNextQuestion
            )
                    .collect(::onTimerUpdate)
        }
    }

    private fun onTimerUpdate(remainingSeconds: Int) {
        updateState {
            it.copy(
                timerUiState = state.value.timerUiState.copy(
                    currentTimerCount = remainingSeconds,
                    currentTimerColor = if (remainingSeconds > 5) TimerUiState.TimerColor.GREEN
                    else TimerUiState.TimerColor.RED,
                    progress = remainingSeconds.toFloat() / it.questions[it.currentQuestionIndex].questionTime
                )
            )
        }
        increaseSpentTimeSecondsByOne()
    }

    private fun onCompletion() {
        updateState { it.copy(isLoading = false) }
    }

    override fun onCancelGameClick() {
        sendNewEffect(GenreGameEffect.CancelGame)
    }

    override fun onChooseAnswerClick(answerIndex: Int) {
        timerHandler.stopTimer()
        val currentQuestion = state.value.questions[state.value.currentQuestionIndex]
        tryToExecute(
            action = {
                guessMovieGenreUseCase.checkAnswer(
                    answer = currentQuestion.answers[answerIndex],
                    question = currentQuestion.toQuestion(),
                    difficultyType = difficultyType
                )
            },
            onSuccess = { answer -> onAnswerCorrect(answer, answerIndex) },
            onCompletion = timerHandler::stopTimer

        )
    }

    private fun onAnswerCorrect(
        answerResult: AnswerResult,
        answerIndex: Int
    ) {
        updateState {
            it.copy(
                selectedAnswerIndex = answerIndex,
                isAnswerCorrect = answerResult.isCorrect,
                isNextEnabled = true,
                isNotEnoughPointsDialogVisible = false,
                earnedPoints = answerResult.earnedPoints
            )
        }
        addPointsToGameUseCase(answerResult.earnedPoints, state.value.gameSessionId)
    }

    override fun onUseHint() {
        if (state.value.isNextEnabled) return
        val currentQuestion = state.value.questions[state.value.currentQuestionIndex]
        tryToExecute(
            action = { guessMovieGenreUseCase.giveHint(currentQuestion.toQuestion()) },
            onSuccess = ::onUseHintSuccess,
            onError = ::onError,
            onCompletion = ::onUseHintCompletion
        )
    }

    private fun onUseHintSuccess(newQuestions: GameQuestion<MovieGenre>) {
        val updatedQuestions = state.value.questions.toMutableList().apply {
            set(state.value.currentQuestionIndex, newQuestions.toUiState())
        }
        updateState {
            it.copy(
                questions = updatedQuestions,
                isHintEnabled = false
            )
        }
    }

    private fun onUseHintCompletion() {
        updateState { it.copy(isHintEnabled = false) }
    }

    override fun onMoveToNextQuestion() {
        val nextQuestionIndex = state.value.currentQuestionIndex + 1
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
                earnedPoints = null
            )
        }
        startTheTimer()
    }

    private fun handleGameFinished() {
        val resultData = ResultScreenData(
            difficulty = difficultyType.name,
            gameType = Game.GameType.GUESS_GENRE.name,
            gameSessionId = state.value.gameSessionId
        )
        sendNewNavigationEffect(GenreGameEffect.GameOver(resultData))
    }

    override fun dismissNotEnoughPointsDialog() {
        updateState { it.copy(isNotEnoughPointsDialogVisible = false) }
    }

    private fun increaseSpentTimeSecondsByOne() {
        addSecondToGameTimeUseCase(state.value.gameSessionId)
    }

    private fun onError(error: AflamiException) {
        when (error) {
            is NotEnoughPointsException -> updateState { it.copy(isNotEnoughPointsDialogVisible = true) }
        }
    }
}
