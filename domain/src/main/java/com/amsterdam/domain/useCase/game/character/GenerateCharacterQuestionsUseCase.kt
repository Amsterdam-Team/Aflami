@file:OptIn(ExperimentalUuidApi::class)

package com.amsterdam.domain.useCase.game.character

import com.amsterdam.domain.repository.GameRepository
import com.amsterdam.domain.useCase.game.GetGameDifficultyByDifficultyTypeUseCase
import com.amsterdam.domain.utils.GameQuestion
import com.amsterdam.entity.GameDifficulty.DifficultyType
import kotlin.uuid.ExperimentalUuidApi

class GenerateCharacterQuestionsUseCase(
    private val gameRepository: GameRepository,
    private val getGameDifficultyByDifficultyTypeUseCase: GetGameDifficultyByDifficultyTypeUseCase,
) {
    suspend operator fun invoke(difficultyType: DifficultyType): List<GameQuestion<String>> {
        val gameDifficulty = getGameDifficultyByDifficultyTypeUseCase(difficultyType)

        val characters = gameRepository.getCharacterDataQuestions(gameDifficulty.totalQuestions * 4)

        return characters.chunked(4).map { chunk ->
            val correctCharacter = chunk.random()
            val wrongAnswers = chunk.filter { it != correctCharacter }.map { it.name }
            val choices = (wrongAnswers + correctCharacter.name).shuffled()

            GameQuestion(
                question = correctCharacter.imageUrl,
                choices = choices,
                correctChoice = correctCharacter.name,
                questionTime = gameDifficulty.timeLimitSeconds
            )
        }
    }
}