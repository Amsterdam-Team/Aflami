package com.example.ui.screens.search.sections.filterDialog

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.example.designsystem.R
import com.example.viewmodel.common.categories.MovieCategoryType

val movieCategories: Map<MovieCategoryType, Genre<MovieCategoryType>> = mapOf(
    MovieCategoryType.ALL to Genre(MovieCategoryType.ALL, R.drawable.ic_nav_categories, R.string.all),
    MovieCategoryType.ACTION to Genre(MovieCategoryType.ACTION, R.drawable.ic_cat_action, R.string.action),
    MovieCategoryType.ADVENTURE to Genre(MovieCategoryType.ADVENTURE, R.drawable.ic_cat_adventure, R.string.adventure),
    MovieCategoryType.ANIMATION to Genre(MovieCategoryType.ANIMATION, R.drawable.ic_cat_animation, R.string.animation),
    MovieCategoryType.COMEDY to Genre(MovieCategoryType.COMEDY, R.drawable.ic_cat_comedy, R.string.comedy),
    MovieCategoryType.CRIME to Genre(MovieCategoryType.CRIME, R.drawable.ic_cat_crime, R.string.crime),
    MovieCategoryType.DOCUMENTARY to Genre(MovieCategoryType.DOCUMENTARY, R.drawable.ic_cat_documentary, R.string.documentary),
    MovieCategoryType.DRAMA to Genre(MovieCategoryType.DRAMA, R.drawable.ic_cat_drama, R.string.drama),
    MovieCategoryType.FAMILY to Genre(MovieCategoryType.FAMILY, R.drawable.ic_cat_family, R.string.family),
    MovieCategoryType.FANTASY to Genre(MovieCategoryType.FANTASY, R.drawable.ic_cat_fantasy, R.string.fantasy),
    MovieCategoryType.HISTORY to Genre(MovieCategoryType.HISTORY, R.drawable.ic_cat_history, R.string.history),
    MovieCategoryType.HORROR to Genre(MovieCategoryType.HORROR, R.drawable.ic_cat_horror, R.string.horror),
    MovieCategoryType.MUSIC to Genre(MovieCategoryType.MUSIC, R.drawable.ic_cat_music, R.string.music),
    MovieCategoryType.MYSTERY to Genre(MovieCategoryType.MYSTERY, R.drawable.ic_cat_mystery, R.string.mystery),
    MovieCategoryType.ROMANCE to Genre(MovieCategoryType.ROMANCE, R.drawable.ic_cat_romance, R.string.romance),
    MovieCategoryType.SCIENCE_FICTION to Genre(MovieCategoryType.SCIENCE_FICTION, R.drawable.ic_cat_sciencefiction, R.string.science_fiction),
    MovieCategoryType.TV_MOVIE to Genre(MovieCategoryType.TV_MOVIE, R.drawable.ic_cat_tvmovie, R.string.tv_movie),
    MovieCategoryType.THRILLER to Genre(MovieCategoryType.THRILLER, R.drawable.ic_cat_thriller, R.string.thriller),
    MovieCategoryType.WAR to Genre(MovieCategoryType.WAR, R.drawable.ic_cat_war, R.string.war),
    MovieCategoryType.WESTERN to Genre(MovieCategoryType.WESTERN, R.drawable.ic_cat_western, R.string.western),
)

@Composable
fun getMovieGenreLabel(type: MovieCategoryType): String {
    return stringResource(movieCategories[type]?.displayableName ?: R.string.all)
}

@Composable
fun getMovieGenreIcon(type: MovieCategoryType): Painter {
    return painterResource(movieCategories[type]?.icon ?: R.drawable.ic_nav_categories)
}
