package com.amsterdam.ui.screens.home.mapper

import com.amsterdam.domain.utils.Mood
import com.amsterdam.domain.utils.Mood.SAD
import com.amsterdam.domain.utils.Mood.entries
import com.amsterdam.ui.screens.home.model.CardMood

fun CardMood.toMood(): Mood {
    entries.forEach { mood ->
        if (mood.name == this.name)
            return mood
    }
    return SAD
}

fun Mood.toCardMood(): CardMood {
    CardMood.entries.forEach { mood ->
        if (mood.name == this.name) return mood
    }
    return CardMood.SAD
}