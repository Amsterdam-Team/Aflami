package com.amsterdam.domain.useCase.utils

import com.amsterdam.entity.People


fun generateFakePeopleByCount(count: Int): List<People>{
    val list = mutableListOf<People>()
    repeat(count){ i ->
        list.add(
            People(
                id = i.toLong(),
                name = "John",
                imageUrl = "",
            )
        )
    }
    return list
}