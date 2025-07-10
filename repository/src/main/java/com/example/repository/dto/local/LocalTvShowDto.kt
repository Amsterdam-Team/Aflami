package com.example.repository.dto.local

import com.example.entity.Category

data class LocalTvShowDto (
    val id : Long,
    val name : String ,
    val description : String,
    val poster : String,
    val productionYear : Int,
    val categories : List<Category>,
    val rating : Float
)
