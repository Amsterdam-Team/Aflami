package com.amsterdam.repository.repository

import com.amsterdam.repository.datasource.local.GameLocalDataSource
import javax.inject.Inject

class GameRepositoryImpl @Inject constructor(private val gameLocalDataSource : GameLocalDataSource) {

}