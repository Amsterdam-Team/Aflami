package com.amsterdam.repository.mapper

import com.amsterdam.domain.utils.RestrictionLevel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow


fun RestrictionLevel.toLocalDto(): String {
    return when (this) {
        RestrictionLevel.STRICT -> "STRICT"
        RestrictionLevel.MODERATE -> "MODERATE"
        RestrictionLevel.OFF -> "OFF"
    }
}

fun stringToRestrictionLevelEntity(restrictionLevel: Flow<String>): Flow<RestrictionLevel> {
    return flow {
        restrictionLevel.collect {
            emit(
                when (it) {
                    "STRICT" -> RestrictionLevel.STRICT
                    "MODERATE" -> RestrictionLevel.MODERATE
                    else -> RestrictionLevel.OFF
                }
            )
        }
    }
}
