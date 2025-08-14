@file:OptIn(ExperimentalUuidApi::class)

package com.amsterdam.domain.useCase.game.character

import com.amsterdam.domain.repository.GameRepository
import com.amsterdam.domain.useCase.game.GetGameDifficultyByDifficultyTypeUseCase
import com.amsterdam.entity.GameDifficulty.DifficultyType
import kotlin.uuid.ExperimentalUuidApi

class GenerateCharacterQuestionsUseCase(
    private val gameRepository: GameRepository,
    private val getGameDifficultyByDifficultyTypeUseCase: GetGameDifficultyByDifficultyTypeUseCase,
) {
    suspend operator fun invoke(difficultyType: DifficultyType): List<CharacterDataQuestion> {
        val gameDifficulty = getGameDifficultyByDifficultyTypeUseCase(difficultyType)

        val peoples = gameRepository.getCharacterDataQuestions(gameDifficulty.totalQuestions * 4)

        return peoples.chunked(4).map { chunk ->
            val correctPeople = chunk.random()
            val wrongAnswers = chunk.filter { it != correctPeople }.map { it.name }
            val choices = (wrongAnswers + correctPeople.name).shuffled()

            CharacterDataQuestion(
                questionAsPosterUrl = correctPeople.imageUrl,
                choices = choices,
                correctAnswer = correctPeople.name,
                questionTimeSeconds = gameDifficulty.timeLimitSeconds
            )
        }
    }

    data class CharacterDataQuestion(
        val questionAsPosterUrl: String,
        val choices: List<String>,
        val correctAnswer: String,
        val questionTimeSeconds: Int,
    )
}