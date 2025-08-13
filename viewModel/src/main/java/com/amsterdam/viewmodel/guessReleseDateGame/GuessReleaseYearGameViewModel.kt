package com.amsterdam.viewmodel.guessReleseDateGame

import androidx.lifecycle.viewModelScope
import com.amsterdam.domain.timer.TimerHandler
import com.amsterdam.domain.useCase.game.releaseYear.GenerateMovieReleaseYearQuestionsUseCase.MovieReleasedDateQuestion
import com.amsterdam.domain.useCase.game.releaseYear.GuessReleaseYearGameUseCase
import com.amsterdam.domain.useCase.game.releaseYear.SubmitGuessReleaseYearAnswerUseCase.AnswerResult
import com.amsterdam.entity.GameDifficulty.DifficultyType
import com.amsterdam.viewmodel.shared.BaseViewModel
import com.amsterdam.viewmodel.sharedGame.TimerUiState
import com.amsterdam.viewmodel.utils.dispatcher.DispatcherProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GuessReleaseYearGameViewModel @Inject constructor(
    private val guessReleaseYearForMovieGameUseCase: GuessReleaseYearGameUseCase,
    args: GuessReleaseYearGameArgs,
    private val dispatcherProvider: DispatcherProvider,
    private val timerHandler: TimerHandler
) : BaseViewModel<GuessReleaseYearUiState, GuessReleaseYearGameEffect>(
    GuessReleaseYearUiState(),
    dispatcherProvider
), GuessReleaseYearInteractionListener {
    private val difficultyType = DifficultyType.valueOf(args.difficulty)
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

    private suspend fun startTheGame(): List<MovieReleasedDateQuestion> {
        return guessReleaseYearForMovieGameUseCase.startGame(difficultyType)
    }

    private fun onSuccessGetQuestions(questions: List<MovieReleasedDateQuestion>) {
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
                onTimerFinish = ::onTimeFinish
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

    private fun onTimeFinish() {
        updateState { it.copy(isNextEnabled = true) }
    }

    fun onError(error: Exception) {

    }

    fun onCompletion() {
        updateState { it.copy(isLoading = false) }
    }

    override fun onHintClicked() {
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
        val selectedAnswer = question.releaseYearChoices[selectedAnswerIndex]
        tryToExecute(
            action = {
                guessReleaseYearForMovieGameUseCase.answer(
                    question,
                    selectedAnswer,
                    difficultyType
                )
            },
            onSuccess = { onSuccessSubmitAnswer(it, selectedAnswerIndex) }
        )
    }

    private fun onSuccessSubmitAnswer(answerResult: AnswerResult, selectedAnswerIndex: Int) {
        updateState {
            it.copy(
                isAnswerCorrect = answerResult.isCorrect,
                isNextEnabled = true,
                selectedAnswerIndex = selectedAnswerIndex
            )
        }
        if (answerResult.isCorrect) {
            totalCollectedPoints += answerResult.earnedPoints
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
                )
            }
            startTheTimer()
        } else {
            sendNewNavigationEffect(
                GuessReleaseYearGameEffect.NavigateToGameResult(
                    totalCollectedPoints,
                    spentTimeSeconds
                )
            )
        }
    }

    private fun increaseSpentTimeSecondsByOne() {
        spentTimeSeconds += 1
    }

    override fun onCloseButtonClicked() {
        sendNewNavigationEffect(GuessReleaseYearGameEffect.NavigateBack)
    }

    override fun onClickClose() {
        sendNewNavigationEffect(GuessReleaseYearGameEffect.NavigateBack)
    }

}