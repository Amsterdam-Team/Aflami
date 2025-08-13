@file:OptIn(ExperimentalUuidApi::class)

package com.amsterdam.domain.useCase.game.character

import com.amsterdam.domain.repository.GameRepository
import com.amsterdam.domain.useCase.game.GetGameDifficultyByDifficultyTypeUseCase
import com.amsterdam.entity.GameDifficulty.DifficultyType
import com.amsterdam.entity.People
import kotlin.uuid.ExperimentalUuidApi

class GenerateCharacterQuestionsUseCase(
    private val gameRepository: GameRepository,
    private val getGameDifficultyByDifficultyTypeUseCase: GetGameDifficultyByDifficultyTypeUseCase
) {
    suspend operator fun invoke(difficultyType: DifficultyType): List<CharacterDataQuestion> {
        val gameDifficulty = getGameDifficultyByDifficultyTypeUseCase(difficultyType)
        val peoples = gameRepository.getCharacterDataQuestions(gameDifficulty.totalQuestions)

        return peoples.map { people ->
            val correctAnswer = people
            val choices = generateCharacterChoices(correctAnswer)
            CharacterDataQuestion(
                questionAsPosterUrl = correctAnswer.name,
                answers = choices.map(People::name),
                correctAnswer = correctAnswer.name,
                questionTimeSeconds = gameDifficulty.timeLimitSeconds
            )
        }
    }

    private fun generateCharacterChoices(
        correctAnswer: People,
        numberOfChoices: Int = 4
    ): List<People> {
        val choices = mutableSetOf(correctAnswer)

        while (choices.size < numberOfChoices) {
            val randomCharacter = choices.random()
            choices.add(randomCharacter)
        }

        return choices.shuffled()
    }

    data class CharacterDataQuestion(
        val questionAsPosterUrl: String,
        val answers: List<String>,
        val correctAnswer: String,
        val questionTimeSeconds: Int,
    )
}