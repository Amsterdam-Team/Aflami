@file:OptIn(ExperimentalUuidApi::class)

package com.amsterdam.domain.useCase.game.character

import com.amsterdam.domain.repository.GameRepository
import com.amsterdam.domain.useCase.game.GetGameDifficultyByDifficultyTypeUseCase
import com.amsterdam.entity.GameDifficulty.DifficultyType
import com.amsterdam.entity.People
import kotlin.uuid.ExperimentalUuidApi

class GenerateCharacterQuestionsUseCase(
    private val gameRepository: GameRepository,
    private val getGameDifficultyByDifficultyTypeUseCase: GetGameDifficultyByDifficultyTypeUseCase,
) {
    suspend operator fun invoke(difficultyType: DifficultyType): List<CharacterDataQuestion> {
        val gameDifficulty = getGameDifficultyByDifficultyTypeUseCase(difficultyType)
        val peoples = gameRepository.getCharacterDataQuestions(gameDifficulty.totalQuestions)

        return peoples.map { people ->
            val correctPeople = people
            val choices = peoples
                .filter { it.name != correctPeople.name }
                .shuffled()
                .take(3)
                .map(People::name)
                .plus(correctPeople.name)
                .shuffled()

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