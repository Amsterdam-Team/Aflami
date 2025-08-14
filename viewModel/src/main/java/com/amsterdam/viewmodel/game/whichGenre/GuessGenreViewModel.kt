package com.amsterdam.viewmodel.game.whichGenre

import androidx.lifecycle.viewModelScope
import com.amsterdam.domain.exceptions.AflamiException
import com.amsterdam.domain.exceptions.NotEnoughPointsException
import com.amsterdam.domain.timer.TimerHandler
import com.amsterdam.domain.useCase.game.whichGenre.GenerateMovieGenreQuestionsUseCase.MovieGenreQuestion
import com.amsterdam.domain.useCase.game.whichGenre.GuessMovieGenreUseCase
import com.amsterdam.domain.useCase.game.whichGenre.SubmitGuessMovieGenreAnswerUseCase
import com.amsterdam.entity.GameDifficulty.DifficultyType
import com.amsterdam.viewmodel.gameEnd.ResultScreenData
import com.amsterdam.viewmodel.gameEnd.ResultSideEffect
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
            timerHandler.startTimer( currentQuestion.questionTime, onTimerFinish = ::onMoveToNextQuestion)
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

    private fun onCompletion() {
        updateState { it.copy(isLoading = false) }
    }

    override fun onCancelGameClick() {
        sendNewEffect(GenreGameEffect.CancelGame)
    }

    override fun onChooseAnswerClick(answerIndex: Int) {
        timerHandler.stopTimer()
        val currentQuestion =  state.value.questions[state.value.currentQuestionIndex]
        tryToExecute(
            action = { guessMovieGenreUseCase.checkAnswer(
                answer = currentQuestion.answers[answerIndex],
                question = currentQuestion.toQuestion(),
                difficultyType = difficultyType
            ) },
            onSuccess = { answer -> onAnswerCorrect(answer, answerIndex) },
           onCompletion = timerHandler::stopTimer

        )
    }

    private fun onAnswerCorrect(
        answerResult: SubmitGuessMovieGenreAnswerUseCase.AnswerResult,
        answerIndex: Int
    ) {
        updateState {
            it.copy(
                selectedAnswerIndex = answerIndex,
                isAnswerCorrect = answerResult.isCorrect,
                isNextEnabled = true,
                isNotEnoughPointsDialogVisible = false
            )
        }
        totalEarnedPoints += answerResult.earnedPoints
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
            val resultData = ResultScreenData(
                totalCollectedPoints = totalEarnedPoints,
                totalSpentSeconds = answersTotalTime,
                difficulty = difficultyType.name,
                gameType = ResultSideEffect.GameType.GUESS_GENRE.name
            )
            sendNewEffect(GenreGameEffect.GameOver(resultData))
        }
    }

    override fun dismissNotEnoughPointsDialog() {
        updateState { it.copy(isNotEnoughPointsDialogVisible = false) }
    }

    private fun onError(error: AflamiException) {
        when(error){
            is NotEnoughPointsException -> updateState { it.copy(isNotEnoughPointsDialogVisible = true) }
        }
    }
}
