package com.amsterdam.domain.repository

interface GameRepository {
    suspend fun getTotalUserPoints(): Int
}