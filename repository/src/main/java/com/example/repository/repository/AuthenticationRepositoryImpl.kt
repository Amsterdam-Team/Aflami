package com.example.repository.repository

import com.example.domain.repository.AuthenticationRepository
import com.example.domain.utils.SessionType
import com.example.repository.datasource.local.AuthenticationLocalSource
import com.example.repository.mapper.local.SessionTypeMapper

class AuthenticationRepositoryImpl(
    private val authenticationLocalSource: AuthenticationLocalSource,
    private val sessionTypeMapper: SessionTypeMapper
): AuthenticationRepository {
    override suspend fun loginWithPassword(username: String, password: String) {

    }

    override suspend fun setSessionType(sessionType: SessionType) {
        authenticationLocalSource.setSessionType(sessionTypeMapper.toLocalSessionType(sessionType))
    }

    override suspend fun getSessionType(): SessionType {
        return sessionTypeMapper.fromLocalSessionType(authenticationLocalSource.getSessionType())
    }
}