package com.amsterdam.viewmodel.game

import com.amsterdam.entity.Game
import com.amsterdam.viewmodel.shared.BaseViewModel
import com.amsterdam.viewmodel.utils.dispatcher.DispatcherProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class GameViewModel
    @Inject
    constructor(
        dispatcherProvider: DispatcherProvider,
    ) : BaseViewModel<GameUiState, GameEffect>(
            GameUiState(),
            dispatcherProvider,
        ),
        GameInteractionListener {
        init {
            updateState {
                it.copy(
                    questions =
                        listOf(
                            GameQuestionUiState(
                                questionData = "A",
                                answers =
                                    listOf(
                                        "A",
                                        "B",
                                        "C",
                                        "D",
                                    ),
                            ),
                            GameQuestionUiState(
                                questionData = "B",
                                answers =
                                    listOf(
                                        "A",
                                        "B",
                                        "C",
                                        "D",
                                    ),
                            ),
                            GameQuestionUiState(
                                questionData = "C",
                                answers =
                                    listOf(
                                        "A",
                                        "B",
                                        "C",
                                        "D",
                                    ),
                            ),
                            GameQuestionUiState(
                                questionData = "D",
                                answers =
                                    listOf(
                                        "A",
                                        "B",
                                        "C",
                                    "D",
                                    ),
                            ),
                        ),
                )
            }
        }

        override fun onCancelGameClick() {
            sendNewEffect(GameEffect.CancelGame)
        }

        override fun onChooseAnswerClick(answerIndex: Int) {
            updateState {
                it.copy(
                    selectedAnswerIndex = answerIndex,
                    isAnswerCorrect = true,
                    isNextEnabled = true,
                )
            }
        }

        override fun onUseHint() {
            when (state.value.gameType) {
                Game.GameType.GUESS_CHARACTER, Game.GameType.GUESS_MOVIE_BY_POSTER -> {
                    updateState { it.copy(isHintEnabled = false) }
                }

                Game.GameType.GUESS_MOVIE_BY_RELEASE, Game.GameType.GUESS_MOVIE_BY_GENRE -> {
                    val currentQuestion = state.value.questions[state.value.currentQuestionIndex]
                    val updatedQuestion = currentQuestion.copy(answers = currentQuestion.answers.shuffled().drop(1))
                    updateState {
                        it.copy(
                            questions =
                                it.questions
                                    .toMutableList()
                                    .apply { set(state.value.currentQuestionIndex, updatedQuestion) },
                            isHintEnabled = false,
                        )
                    }
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
                )
            }
        } else {
            sendNewEffect(GameEffect.GameOver)
        }
        }
    }
