package com.example.repository.dto.local

data class LocalTvShowDto (
    val id : Long,
    val name : String ,
    val description : String,
    val poster : String,
    val productionYear : Int,
    val rating : Float
)
