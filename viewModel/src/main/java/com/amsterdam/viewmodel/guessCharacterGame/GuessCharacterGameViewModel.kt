package com.amsterdam.viewmodel.guessCharacterGame

import androidx.lifecycle.viewModelScope
import com.amsterdam.domain.exceptions.AflamiException
import com.amsterdam.domain.exceptions.NotEnoughPointsException
import com.amsterdam.domain.timer.TimerHandler
import com.amsterdam.domain.useCase.game.character.GenerateCharacterQuestionsUseCase.CharacterDataQuestion
import com.amsterdam.domain.useCase.game.character.GuessCharacterGameUseCase
import com.amsterdam.domain.useCase.game.character.SubmitCharacterAnswerUseCase.AnswerResult
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
class GuessCharacterGameViewModel @Inject constructor(
    private val guessCharacterGameUseCase: GuessCharacterGameUseCase,
    args: GuessCharacterGameArgs,
    private val dispatcherProvider: DispatcherProvider,
    private val timerHandler: TimerHandler
) : BaseViewModel<GuessCharacterUiState, GuessCharacterGameEffect>(
    GuessCharacterUiState(),
    dispatcherProvider
), GuessCharacterInteractionListener {
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

    private suspend fun startTheGame(): List<CharacterDataQuestion> {
        return guessCharacterGameUseCase.startGame(difficultyType)
    }

    private fun onSuccessGetQuestions(questions: List<CharacterDataQuestion>) {
        viewModelScope.launch {
            updateState { it.copy(questions = questions.toQuestionsUiState()) }
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
                guessCharacterGameUseCase.giveHint(currentQuestion.toCharacterDataQuestion())
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
            state.value.questions[state.value.currentQuestionIndex].toCharacterDataQuestion()
        val selectedAnswer = question.choices[selectedAnswerIndex]
        tryToExecute(
            action = {
                guessCharacterGameUseCase.answer(
                    question,
                    selectedAnswer,
                    difficultyType
                )
            },
            onSuccess = { onSuccessSubmitAnswer(it, selectedAnswerIndex) },
            onCompletion = ::onSubmitTheAnswerComplete
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
                gameType = ResultSideEffect.GameType.GUESS_CHARACTER.name
            )
            sendNewNavigationEffect(
                GuessCharacterGameEffect.NavigateToGameResult(
                    resultData
                )
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
        sendNewNavigationEffect(GuessCharacterGameEffect.NavigateBack)
    }

    private fun onError(error: AflamiException) {
        when(error){
            is NotEnoughPointsException -> updateState { it.copy(isNotEnoughPointsDialogVisible = true) }

        }
    }

}