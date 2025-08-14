package com.amsterdam.localdatasource.daos

import androidx.room.Dao
import com.amsterdam.localdatasource.roomDataBase.daos.GamePointsDao
import com.amsterdam.repository.dto.local.GamePointsDto
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@Dao
class GamePointsDaoTest : BaseDaoTest() {
    private lateinit var gamePointsDao: GamePointsDao

    @BeforeEach
    fun setup() {
        gamePointsDao = aflamiDatabase.gamePointsDao()
    }

    @Test
    fun upsertPoints_shouldInsertGamePoints_whenGamePointsNotExist() = runTest {
        gamePointsDao.upsertPoints(gamePoints)
        val result = gamePointsDao.getPoints().firstOrNull()

        assertThat(result).isEqualTo(gamePoints.points)
    }

    @Test
    fun upsertPoints_shouldUpdateGamePoints_whenGamePointsExist() = runTest {
        gamePointsDao.upsertPoints(gamePoints)

        gamePointsDao.upsertPoints(updatedGamePoints)
        val result = gamePointsDao.getPoints().firstOrNull()

        assertThat(result).isEqualTo(updatedGamePoints.points)
    }

    @Test
    fun getPoints_shouldReturnGamePoints_whenGamePointsExist() = runTest {
        gamePointsDao.upsertPoints(gamePoints)

        val result = gamePointsDao.getPoints().firstOrNull()

        assertThat(result).isEqualTo(gamePoints.points)
    }

    @Test
    fun getPoints_shouldReturnNull_whenGamePointsNotExist() = runTest {
        val result = gamePointsDao.getPoints().firstOrNull()

        assertThat(result).isNull()
    }
}

private val gamePoints = GamePointsDto(points = 50)
private val updatedGamePoints = gamePoints.copy(points = 100)