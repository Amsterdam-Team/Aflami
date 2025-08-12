package com.amsterdam.domain.quizGame.strategies

import com.amsterdam.entity.game.Question

interface QuestionProvider {
    suspend fun <T> generateQuestions(config: GameConfig): List<Question<T>>
}

