package com.example.entity

data class TvShow (
    val id : Long,
    val name : String ,
    val description : String,
    val boaster : String,
    val productionYear : Int,
    val categories : List<Category>,
    val rating : Float
    )
