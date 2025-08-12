package com.amsterdam.entity.game


sealed class GameDifficulty(
    open val totalQuestions: Int,
    open val timeLimitSeconds: Int,
    open val pointsPerQuestion: Int,
) {
    data class Easy(
        override val totalQuestions: Int,
        override val timeLimitSeconds: Int,
        override val pointsPerQuestion: Int,
    ) : GameDifficulty( totalQuestions, timeLimitSeconds, pointsPerQuestion)

    data class Medium(
        override val totalQuestions: Int,
        override val timeLimitSeconds: Int,
        override val pointsPerQuestion: Int,
    ) : GameDifficulty( totalQuestions, timeLimitSeconds, pointsPerQuestion)

    data class Hard(
        override val totalQuestions: Int,
        override val timeLimitSeconds: Int,
        override val pointsPerQuestion: Int,
    ) : GameDifficulty( totalQuestions, timeLimitSeconds, pointsPerQuestion)
}