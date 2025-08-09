package com.amsterdam.ui.screens.letsPlay

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.amsterdam.designsystem.theme.AppTheme
import com.amsterdam.ui.R
import com.amsterdam.ui.components.GameCardImageContentType
import com.amsterdam.viewmodel.letsPlay.LetsPlayUiState.GameUiState.GameTypeUiState

data class GameTypeData(
    val title: Int,
    val description: Int,
    val containerColor: Color,
    val borderColors: List<Color>,
    val gameCardImageContentType: GameCardImageContentType,
)
@Composable
fun GameTypeUiState.getGameTypeData(): GameTypeData {
    return when (this) {
        GameTypeUiState.GUESS_CHARACTER -> GameTypeData(
            R.string.guess_character_game_title,
            R.string.guess_character_game_description,
            AppTheme.color.primaryVariant,
            AppTheme.color.guessCharacterStrokeGradient,
            GameCardImageContentType.FUN_CLOWN,
        )

        GameTypeUiState.GUESS_MOVIE_BY_POSTER -> GameTypeData(
            R.string.guess_movie_game_title,
            R.string.guess_movie_game_description,
            AppTheme.color.blueCard,
            AppTheme.color.guessMovieByPosterStrokeGradient,
            GameCardImageContentType.MANY_POSTERS,
        )
        GameTypeUiState.GUESS_MOVIE_BY_RELEASE -> GameTypeData(
            R.string.release_game_title,
            R.string.release_game_description,
            AppTheme.color.navyCard,
            AppTheme.color.guessMovieByReleaseStrokeGradient,
            GameCardImageContentType.CALENDER,
        )

        GameTypeUiState.GUESS_MOVIE_BY_GENRE -> GameTypeData(
            R.string.genre_game_title,
            R.string.genre_game_description,
            AppTheme.color.yellowCard,
            AppTheme.color.guessMovieByGenreStrokeGradient,
            GameCardImageContentType.LAWN_CHAIR,
        )
    }

}