package com.amsterdam.ui.screens.letsPlay

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.amsterdam.designsystem.theme.AppTheme
import com.amsterdam.entity.Game
import com.amsterdam.ui.R
import com.amsterdam.ui.screens.letsPlay.component.GameCardImageContentType

data class GameTypeData(
    val title: Int,
    val description: Int,
    val containerColor: Color,
    val borderColors: List<Color>,
    val shadowColor: Color,
    val gameCardImageContentType: GameCardImageContentType,
)
@Composable
fun Game.GameType.getGameTypeData(): GameTypeData {
    return when (this) {
        Game.GameType.GUESS_CHARACTER -> GameTypeData(
            R.string.guess_character_game_title,
            R.string.guess_character_game_description,
            AppTheme.color.primaryVariant,
            AppTheme.color.guessCharacterStrokeGradient,
            AppTheme.color.primary,
            GameCardImageContentType.FUN_CLOWN,
        )

        Game.GameType.GUESS_MOVIE_BY_POSTER -> GameTypeData(
            R.string.guess_movie_game_title,
            R.string.guess_movie_game_description,
            AppTheme.color.blueCard,
            AppTheme.color.guessMovieByPosterStrokeGradient,
            AppTheme.color.blueAccent,
            GameCardImageContentType.MANY_POSTERS,
        )
        Game.GameType.GUESS_RELEASE_YEAR -> GameTypeData(
            R.string.release_game_title,
            R.string.release_game_description,
            AppTheme.color.navyCard,
            AppTheme.color.guessMovieByReleaseStrokeGradient,
            AppTheme.color.darkBlue,
            GameCardImageContentType.CALENDER,
        )

        Game.GameType.GUESS_GENRE -> GameTypeData(
            R.string.genre_game_title,
            R.string.genre_game_description,
            AppTheme.color.yellowCard,
            AppTheme.color.guessMovieByGenreStrokeGradient,
            AppTheme.color.yellowAccent,
            GameCardImageContentType.LAWN_CHAIR,
        )
    }

}