package com.amsterdam.localdatasource.roomDataBase.datasource

import com.amsterdam.localdatasource.roomDataBase.daos.GamePointsDao
import com.amsterdam.repository.dto.local.GamePointsDto
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

class GameLocalDataSourceImplTest {
    private val gamePointsDao by lazy { mockk<GamePointsDao>(relaxed = true) }
    private val gameLocalDataSource by lazy { GameLocalDataSourceImpl(gamePointsDao) }

    @Test
    fun `upsertPoints should call upsertPoints in dao when called`() = runTest {
        gameLocalDataSource.upsertPoints(1)

        coVerify(exactly = 1) { gamePointsDao.upsertPoints(userPoints) }
    }

    @Test
    fun `getUserPoints should return points when dao returns points`() = runTest {
        coEvery { gamePointsDao.getPoints() } returns flowOf(1)

        val result = gameLocalDataSource.getUserPoints()

        assertThat(result.first()).isEqualTo(1)
    }

    @Test
    fun `getUserPoints should call getPoints in dao when called`() = runTest {
        gameLocalDataSource.getUserPoints()

        coVerify(exactly = 1) { gamePointsDao.getPoints() }
    }
}

private val userPoints = GamePointsDto(points = 1)