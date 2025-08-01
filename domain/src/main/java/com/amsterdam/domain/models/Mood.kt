package com.amsterdam.domain.models

import com.amsterdam.entity.category.MovieGenre

enum class Mood(val movieGenres: List<MovieGenre>) {
    SAD(listOf(MovieGenre.COMEDY)),
    NEUTRAL(listOf(MovieGenre.DOCUMENTARY, MovieGenre.MYSTERY)),
    ROMANTIC(listOf(MovieGenre.ROMANCE, MovieGenre.MUSIC)),
    ANGRY(listOf(MovieGenre.COMEDY, MovieGenre.ANIMATION, MovieGenre.FAMILY)),
    DEPRESSED(listOf(MovieGenre.DRAMA, MovieGenre.ANIMATION)),
    SAD_DIZZY(listOf(MovieGenre.ADVENTURE, MovieGenre.FANTASY, MovieGenre.SCIENCE_FICTION));

    companion object {
        fun getMoodByName(moodName: String): Mood {
            entries.forEach { mood ->
                if (mood.name == moodName)
                    return mood
            }
            return SAD
        }
    }
}