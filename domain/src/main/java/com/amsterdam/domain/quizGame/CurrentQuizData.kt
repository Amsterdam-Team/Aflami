package com.amsterdam.domain.quizGame

import com.amsterdam.entity.game.Question

data class CurrentQuizData<T>(
    val questions: List<Question<T>> = emptyList(),
    val numberOfQuestions: Int = 0,
    val currentQuestion: QuizQuestionState<T> = QuizQuestionState(),
    val currentQuestionIndex: Int = 0,
    val totalTimeSpent: Int = 0,
    val totalScore: Int = 0,
    val correctQuestionsAnswered: Int = 0
) {
    data class QuizQuestionState<T>(
//        val question: Question<T> =,
        val isHintUsed: Boolean = false,
        val timeUsed: Int = 0
    )
}
