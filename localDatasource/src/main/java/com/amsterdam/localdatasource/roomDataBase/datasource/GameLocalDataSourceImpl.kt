package com.amsterdam.localdatasource.roomDataBase.datasource

import com.amsterdam.repository.datasource.local.GameLocalDataSource
import javax.inject.Inject

class GameLocalDataSourceImpl @Inject constructor() :GameLocalDataSource {
    override suspend fun addGamePoints(points: Int) {

    }

    override suspend fun getTotalGamePoints(): Int {
        // this is temp value until I discuess with you to take descition if we will let the guest use play the game and
        // if it is . what we will do when when he login we will migrate his score or what we do
        val tempValue = 200
        return tempValue
    }

}