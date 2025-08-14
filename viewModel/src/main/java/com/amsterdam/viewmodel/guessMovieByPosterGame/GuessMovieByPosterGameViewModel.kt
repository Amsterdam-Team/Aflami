package com.amsterdam.viewmodel.guessMovieByPosterGame

import androidx.lifecycle.viewModelScope
import com.amsterdam.domain.exceptions.AflamiException
import com.amsterdam.domain.exceptions.NotEnoughPointsException
import com.amsterdam.domain.timer.TimerHandler
import com.amsterdam.domain.useCase.game.guessByPoster.GuessMovieByPosterGameUseCase
import com.amsterdam.domain.useCase.game.guessByPoster.MoviePosterQuestion
import com.amsterdam.domain.useCase.game.guessByPoster.SubmitGuessMovieByPosterAnswerUseCase
import com.amsterdam.entity.GameDifficulty
import com.amsterdam.viewmodel.gameEnd.ResultScreenData
import com.amsterdam.viewmodel.gameEnd.ResultSideEffect
import com.amsterdam.viewmodel.shared.BaseViewModel
import com.amsterdam.viewmodel.sharedGame.TimerUiState
import com.amsterdam.viewmodel.utils.dispatcher.DispatcherProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GuessMovieByPosterGameViewModel @Inject constructor(
    private val guessMovieByPosterGameUseCase: GuessMovieByPosterGameUseCase,
    args: GuessMovieByPosterGameArgs,
    private val dispatcherProvider: DispatcherProvider,
    private val timerHandler: TimerHandler
) : BaseViewModel<GuessMovieByPosterUiState, GuessMovieByPosterGameEffect>(
    GuessMovieByPosterUiState(),
    dispatcherProvider
), GuessMovieByPosterInteractionListener {

    private val difficultyType = GameDifficulty.DifficultyType.valueOf(args.difficulty)
    private var spentTimeSeconds: Int = 0
    private var totalCollectedPoints: Int = 0

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

    private suspend fun startTheGame(): List<MoviePosterQuestion> {
        return guessMovieByPosterGameUseCase.startGame(difficultyType)
    }

    private fun onSuccessGetQuestions(questions: List<MoviePosterQuestion>) {
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
        val selectedAnswer = question.movieNameChoices[selectedAnswerIndex]

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
        answerResult: SubmitGuessMovieByPosterAnswerUseCase.AnswerResult,
        selectedAnswerIndex: Int
    ) {
        updateState {
            it.copy(
                isAnswerCorrect = answerResult.isCorrect,
                isNextEnabled = true,
                selectedAnswerIndex = selectedAnswerIndex
            )
        }
        totalCollectedPoints += answerResult.earnedPoints
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
            updateState {
                it.copy(
                    currentQuestionIndex = nextQuestionIndex,
                    selectedAnswerIndex = null,
                    isAnswerCorrect = null,
                    isNextEnabled = false,
                    isNotEnoughPointsDialogVisible = false
                )
            }
            startTheTimer()
        } else {
            val resultData = ResultScreenData(
                totalCollectedPoints = totalCollectedPoints,
                totalSpentSeconds = spentTimeSeconds,
                difficulty = difficultyType.name,
                gameType = ResultSideEffect.GameType.GUESS_MOVIE_BY_POSTER.name
            )
            sendNewNavigationEffect(
                GuessMovieByPosterGameEffect.NavigateToGameResult(resultData)
            )
        }
    }

    override fun dismissNotEnoughPointsDialog() {
        updateState { it.copy(isNotEnoughPointsDialogVisible = false) }
    }

    private fun increaseSpentTimeSecondsByOne() {
        spentTimeSeconds += 1
    }

    override fun onCloseButtonClicked() {
        timerHandler.stopTimer()
        sendNewNavigationEffect(GuessMovieByPosterGameEffect.NavigateBack)
    }

    private fun onError(error: AflamiException) {
        when(error){
            is NotEnoughPointsException -> updateState { it.copy(isNotEnoughPointsDialogVisible = true) }
        }
    }
}