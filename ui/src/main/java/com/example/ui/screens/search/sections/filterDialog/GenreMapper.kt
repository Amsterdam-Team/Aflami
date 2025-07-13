package com.example.ui.screens.search.sections.filterDialog

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.example.designsystem.R
import com.example.viewmodel.search.mapper.CategoryType

data class Genre(
    val type: CategoryType,
    @DrawableRes val icon: Int,
    @StringRes val displayableName: Int
)

val mediaCategories: Map<CategoryType, Genre> = mapOf(
    CategoryType.ALL to Genre(CategoryType.ALL, R.drawable.ic_nav_categories, R.string.all),
    CategoryType.ACTION to Genre(CategoryType.ACTION, R.drawable.ic_cat_action, R.string.action),
    CategoryType.ADVENTURE to Genre(CategoryType.ADVENTURE, R.drawable.ic_cat_adventure, R.string.adventure),
    CategoryType.ANIMATION to Genre(CategoryType.ANIMATION, R.drawable.ic_cat_animation, R.string.animation),
    CategoryType.COMEDY to Genre(CategoryType.COMEDY, R.drawable.ic_cat_comedy, R.string.comedy),
    CategoryType.CRIME to Genre(CategoryType.CRIME, R.drawable.ic_cat_crime, R.string.crime),
    CategoryType.DOCUMENTARY to Genre(CategoryType.DOCUMENTARY, R.drawable.ic_cat_documentary, R.string.documentary),
    CategoryType.DRAMA to Genre(CategoryType.DRAMA, R.drawable.ic_cat_drama, R.string.drama),
    CategoryType.FAMILY to Genre(CategoryType.FAMILY, R.drawable.ic_cat_family, R.string.family),
    CategoryType.FANTASY to Genre(CategoryType.FANTASY, R.drawable.ic_cat_fantasy, R.string.fantasy),
    CategoryType.HISTORY to Genre(CategoryType.HISTORY, R.drawable.ic_cat_history, R.string.history),
    CategoryType.HORROR to Genre(CategoryType.HORROR, R.drawable.ic_cat_horror, R.string.horror),
    CategoryType.MUSIC to Genre(CategoryType.MUSIC, R.drawable.ic_cat_music, R.string.music),
    CategoryType.MYSTERY to Genre(CategoryType.MYSTERY, R.drawable.ic_cat_mystery, R.string.mystery),
    CategoryType.ROMANCE to Genre(CategoryType.ROMANCE, R.drawable.ic_cat_romance, R.string.romance),
    CategoryType.SCIENCE_FICTION to Genre(CategoryType.SCIENCE_FICTION, R.drawable.ic_cat_sciencefiction, R.string.science_fiction),
    CategoryType.TV_MOVIE to Genre(CategoryType.TV_MOVIE, R.drawable.ic_cat_tvmovie, R.string.tv_movie),
    CategoryType.THRILLER to Genre(CategoryType.THRILLER, R.drawable.ic_cat_thriller, R.string.thriller),
    CategoryType.WAR to Genre(CategoryType.WAR, R.drawable.ic_cat_war, R.string.war),
    CategoryType.WESTERN to Genre(CategoryType.WESTERN, R.drawable.ic_cat_western, R.string.western),
)


@Composable
fun getGenreLabel(categoryType: CategoryType): String {
    return stringResource(mediaCategories[categoryType]?.displayableName ?: R.string.all)
}

@Composable
fun getGenreIcon(categoryType: CategoryType): Painter {
    return painterResource(mediaCategories[categoryType]?.icon ?: R.drawable.ic_nav_categories)
}