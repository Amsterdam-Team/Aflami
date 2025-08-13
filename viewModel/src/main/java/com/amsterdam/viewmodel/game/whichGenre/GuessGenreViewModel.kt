package com.amsterdam.viewmodel.game.whichGenre

import androidx.lifecycle.viewModelScope
import com.amsterdam.domain.exceptions.AflamiException
import com.amsterdam.domain.timer.TimerHandler
import com.amsterdam.domain.useCase.game.whichGenre.GenerateMovieGenreQuestionsUseCase.MovieGenreQuestion
import com.amsterdam.domain.useCase.game.whichGenre.GuessMovieGenreUseCase
import com.amsterdam.domain.useCase.game.whichGenre.SubmitGuessMovieGenreAnswerUseCase
import com.amsterdam.entity.GameDifficulty.DifficultyType
import com.amsterdam.entity.category.MovieGenre
import com.amsterdam.viewmodel.shared.BaseViewModel
import com.amsterdam.viewmodel.sharedGame.TimerUiState
import com.amsterdam.viewmodel.utils.dispatcher.DispatcherProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GuessGenreViewModel @Inject constructor(
    private val guessMovieGenreUseCase: GuessMovieGenreUseCase,
    private val timerHandler: TimerHandler,
    private val dispatcherProvider: DispatcherProvider,
    args: GameGenreArgs,
): BaseViewModel<GenreGameUiState, GenreGameEffect>(
    GenreGameUiState(),
    dispatcherProvider
), GenreGameInteractionListener {

    private val difficultyType = DifficultyType.valueOf(args.difficulty)
    private var answersTotalTime: Int = 0
    private var totalEarnedPoints: Int = 0

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

    private suspend fun startTheGame(): List<MovieGenreQuestion> {
        return guessMovieGenreUseCase.startGame(difficultyType)
    }

    private fun onSuccessGetQuestions(questions: List<MovieGenreQuestion>) {
        viewModelScope.launch {
            updateState { it.copy(questions = questions.map { it.toUiState() }) }
            startTheTimer()
        }
    }

    private fun startTheTimer(){
        val currentQuestion =
            state.value.questions[state.value.currentQuestionIndex]
        viewModelScope.launch(dispatcherProvider.Default) {
            timerHandler.startTimer( currentQuestion.questionTime, onTimerFinish = ::onTimeFinish)
                .collect(::onTimerUpdate)
        }
    }

    private fun onTimerUpdate(remainingSeconds: Int) {
        answersTotalTime++
        updateState {
            it.copy(
                timerUiState = state.value.timerUiState.copy(
                    currentTimerCount = remainingSeconds,
                    currentTimerColor = if(remainingSeconds > 5) TimerUiState.TimerColor.GREEN
                    else TimerUiState.TimerColor.RED,
                    progress = remainingSeconds.toFloat() / it.questions[it.currentQuestionIndex].questionTime
                )
            )
        }
    }

    private fun onTimeFinish() {
        if (state.value.currentQuestionIndex < state.value.questions.lastIndex) {
            onMoveToNextQuestion()
        } else {
            sendNewEffect(GenreGameEffect.GameOver(totalEarnedPoints, answersTotalTime))
        }
    }

    private fun onError(error: Exception) {

    }

    private fun onCompletion() {
        updateState { it.copy(isLoading = false) }
    }

    override fun onCancelGameClick() {
        sendNewEffect(GenreGameEffect.CancelGame)
    }

    override fun onChooseAnswerClick(answer: MovieGenre, answerIndex: Int) {
        timerHandler.stopTimer()
        val currentQuestion =  state.value.questions[state.value.currentQuestionIndex]
        tryToExecute(
            action = { guessMovieGenreUseCase.checkAnswer(
                answer = answer,
                question = currentQuestion.toQuestion(),
                difficultyType = difficultyType
            ) },
            onSuccess = { answer -> onAnswerCorrect(answer, answerIndex) }
        )
    }

    private fun onAnswerCorrect(
        answerResult: SubmitGuessMovieGenreAnswerUseCase.AnswerResult,
        answerIndex: Int
    ) {
        if (answerResult.isCorrect) {
            totalEarnedPoints += answerResult.earnedPoints
        }
        updateState {
            it.copy(
                selectedAnswerIndex = answerIndex,
                isAnswerCorrect = answerResult.isCorrect,
                isNextEnabled = true
            )
        }
    }

    override fun onUseHint() {
        val currentQuestion = state.value.questions[state.value.currentQuestionIndex]
        tryToExecute(
            action = { guessMovieGenreUseCase.giveHint(currentQuestion.toQuestion()) },
            onSuccess = ::onUseHintSuccess,
            onError = ::onUseHintError,
            onCompletion = ::onUseHintCompletion
        )
    }

    private fun onUseHintSuccess(newQuestions: MovieGenreQuestion) {
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

    private fun onUseHintError(exception: AflamiException) {
        sendNewEffect(GenreGameEffect.ShowNotEnoughPointsSnackBar)
    }

    private fun onUseHintCompletion() {
        updateState { it.copy(isHintEnabled = false) }
    }

    override fun onMoveToNextQuestion() {
        val nextQuestionIndex = state.value.currentQuestionIndex + 1
        if (nextQuestionIndex < state.value.questions.size) {
            updateState {
                it.copy(
                    currentQuestionIndex = nextQuestionIndex,
                    selectedAnswerIndex = null,
                    isAnswerCorrect = null,
                    isNextEnabled = false
                )
            }
            startTheTimer()
        } else {
            sendNewEffect(GenreGameEffect.GameOver(totalEarnedPoints, answersTotalTime))
        }
    }
}
