package com.example.ui.screens.search.sections.filterDialog.genre


import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.example.designsystem.R
import com.example.entity.category.MovieGenre

internal val MovieGenre.uiModel: GenreUiModel
    get() = when (this) {
        MovieGenre.ALL -> GenreUiModel(R.drawable.ic_nav_categories, R.string.all)
        MovieGenre.ACTION -> GenreUiModel(R.drawable.ic_cat_action, R.string.action)
        MovieGenre.ADVENTURE -> GenreUiModel(R.drawable.ic_cat_adventure, R.string.adventure)
        MovieGenre.ANIMATION -> GenreUiModel(R.drawable.ic_cat_animation, R.string.animation)
        MovieGenre.COMEDY -> GenreUiModel(R.drawable.ic_cat_comedy, R.string.comedy)
        MovieGenre.CRIME -> GenreUiModel(R.drawable.ic_cat_crime, R.string.crime)
        MovieGenre.DOCUMENTARY -> GenreUiModel(R.drawable.ic_cat_documentary, R.string.documentary)
        MovieGenre.DRAMA -> GenreUiModel(R.drawable.ic_cat_drama, R.string.drama)
        MovieGenre.FAMILY -> GenreUiModel(R.drawable.ic_cat_family, R.string.family)
        MovieGenre.FANTASY -> GenreUiModel(R.drawable.ic_cat_fantasy, R.string.fantasy)
        MovieGenre.HISTORY -> GenreUiModel(R.drawable.ic_cat_history, R.string.history)
        MovieGenre.HORROR -> GenreUiModel(R.drawable.ic_cat_horror, R.string.horror)
        MovieGenre.MUSIC -> GenreUiModel(R.drawable.ic_cat_music, R.string.music)
        MovieGenre.MYSTERY -> GenreUiModel(R.drawable.ic_cat_mystery, R.string.mystery)
        MovieGenre.ROMANCE -> GenreUiModel(R.drawable.ic_cat_romance, R.string.romance)
        MovieGenre.SCIENCE_FICTION -> GenreUiModel(
            R.drawable.ic_cat_sciencefiction,
            R.string.science_fiction
        )

        MovieGenre.TV_MOVIE -> GenreUiModel(R.drawable.ic_cat_tvmovie, R.string.tv_movie)
        MovieGenre.THRILLER -> GenreUiModel(R.drawable.ic_cat_thriller, R.string.thriller)
        MovieGenre.WAR -> GenreUiModel(R.drawable.ic_cat_war, R.string.war)
        MovieGenre.WESTERN -> GenreUiModel(R.drawable.ic_cat_western, R.string.western)
    }

@Composable
internal fun getMovieGenreLabel(type: MovieGenre): String {
    return stringResource(id = type.uiModel.displayableName)
}

@Composable
internal fun getMovieGenreIcon(type: MovieGenre): Painter {
    return painterResource(id = type.uiModel.icon)
}