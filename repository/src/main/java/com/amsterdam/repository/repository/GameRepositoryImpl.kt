package com.amsterdam.repository.repository

import com.amsterdam.domain.repository.GameRepository
import com.amsterdam.entity.Movie
import com.amsterdam.entity.People
import com.amsterdam.repository.datasource.local.GameLocalDataSource
import com.amsterdam.repository.datasource.remote.MovieRemoteSource
import com.amsterdam.repository.datasource.remote.PeopleRemoteSource
import com.amsterdam.repository.mapper.remote.toEntityList
import com.amsterdam.repository.mapper.remote.toMovieEntityList
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GameRepositoryImpl @Inject constructor(
    private val gameLocalDataSource: GameLocalDataSource,
    private val movieRemoteSource: MovieRemoteSource,
    private val peopleRemoteSource: PeopleRemoteSource
) : GameRepository {

    override suspend fun getRandomMoviesWithNotNullDate(requiredMoviesNumber: Int): List<Movie> {
        return movieRemoteSource.getRandomMoviesWithNotNullDate(requiredMoviesNumber)
            .toMovieEntityList()
    }

    override suspend fun updatePoints(points: Int) {
        gameLocalDataSource.upsertPoints(points)
    }

    override fun getUserPoints(): Flow<Int> {
        return gameLocalDataSource.getUserPoints()
    }

    override suspend fun getCharacterDataQuestions(
        requiredNumber: Int,
    ): List<People> {
        return peopleRemoteSource.getRandomizedTrendingPeople(requiredNumber = requiredNumber)
            .toEntityList()
    }
}