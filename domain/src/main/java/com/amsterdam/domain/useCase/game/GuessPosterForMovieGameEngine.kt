package com.amsterdam.domain.useCase.game

import com.amsterdam.domain.timer.TimerHandler
import com.amsterdam.entity.GameDifficulty

class GuessPosterForMovieGameEngine(
    private val generateMoviePosterQuestionsUseCase: GenerateMoviePosterQuestionsUseCase,
    private val getGameDifficultyByDifficultyTypeUseCase: GetGameDifficultyByDifficultyTypeUseCase,
    private val timerHandler: TimerHandler
) {
    suspend fun startGame(
        difficultyType: GameDifficulty.DifficultyType
    ): List<PosterGuessingGameQuestion> {
        val gameDifficulty = getGameDifficultyByDifficultyTypeUseCase(difficultyType)
        return generateMoviePosterQuestionsUseCase(
            questionCount = gameDifficulty.totalQuestions
        )
    }

    fun startQuestionTimer(
        totalSeconds: Int,
        onTimerUpdate: (remainingSeconds: Int) -> Unit,
        onTimeFinish: () -> Unit
    ) {
        timerHandler.startTimer(
            totalSeconds = totalSeconds,
            onTimerUpdate = onTimerUpdate,
            onTimerFinish = onTimeFinish
        )
    }
}
data class PosterGuessingGameQuestion(
    val posterUrl: String,
    val movieChoices: List<String>,
    val correctMovieName: String
)