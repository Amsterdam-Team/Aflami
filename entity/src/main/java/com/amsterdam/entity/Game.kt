package com.amsterdam.entity

data class Game(
    val gameType: GameType,
    val requiredPoints: Int
) {
    enum class GameType() {
        GUESS_CHARACTER,
        GUESS_MOVIE_BY_POSTER,
        GUESS_MOVIE_BY_RELEASE,
        GUESS_MOVIE_BY_GENRE
    }
}
