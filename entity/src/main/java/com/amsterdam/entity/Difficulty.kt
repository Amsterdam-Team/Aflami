package com.amsterdam.entity

data class GameDifficulty(
    val totalQuestions: Int,
    val timeLimitSeconds: Int,
    val pointsPerQuestion: Int,
    val difficultyType: DifficultyType
) {
    enum class DifficultyType {
        EASY,
        MEDIUM,
        HARD
    }
}