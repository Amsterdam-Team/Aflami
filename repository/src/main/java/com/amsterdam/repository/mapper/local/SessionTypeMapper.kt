package com.amsterdam.repository.mapper.local

import com.amsterdam.domain.utils.SessionType
import javax.inject.Inject

class SessionTypeMapper @Inject constructor(){
    fun toLocalSessionType(sessionType: SessionType): String{
        return when(sessionType){
            SessionType.NOT_LOGGED_IN -> NOT_LOGGED_IN
            SessionType.LOGGED_IN -> LOGGED_IN
            SessionType.GUEST -> GUEST
        }
    }

    fun fromLocalSessionType(sessionType: String): SessionType{
        return when(sessionType){
            LOGGED_IN -> SessionType.LOGGED_IN
            GUEST -> SessionType.GUEST
            else -> SessionType.NOT_LOGGED_IN
        }
    }

    private companion object{
        private const val GUEST = "GUEST"
        private const val LOGGED_IN = "LOGGED_IN"
        private const val NOT_LOGGED_IN = "NOT_LOGGED_IN"
    }
}