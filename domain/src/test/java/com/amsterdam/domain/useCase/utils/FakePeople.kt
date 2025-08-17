package com.amsterdam.domain.useCase.utils

import com.amsterdam.entity.Character


fun generateFakePeopleByCount(count: Int): List<Character>{
    val list = mutableListOf<Character>()
    repeat(count){ i ->
        list.add(
            Character(
                id = i.toLong(),
                name = "John",
                imageUrl = "",
            )
        )
    }
    return list
}