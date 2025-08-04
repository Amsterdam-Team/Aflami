package com.amsterdam.domain.useCase.preferences

import com.amsterdam.domain.repository.AppPreferencesRepository
import com.amsterdam.domain.utils.RestrictionLevel
import kotlinx.coroutines.flow.Flow

class ManageRestrictionLevelUseCase(
    private val preferencesRepository: AppPreferencesRepository
) {
    suspend fun setRestrictionLevel(restrictionLevel: RestrictionLevel){
        preferencesRepository.setRestrictionLevel(restrictionLevel)
    }

    fun getRestrictionLevel(): Flow<RestrictionLevel> {
        return preferencesRepository.getRestrictionLevel()
    }
}