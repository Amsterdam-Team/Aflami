package com.example.ui.screens.search.sections.filterDialog

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.example.designsystem.R
import com.example.viewmodel.common.categories.TvShowCategoryType

val tvShowCategories: Map<TvShowCategoryType, Genre<TvShowCategoryType>> = mapOf(
    TvShowCategoryType.ALL to Genre(TvShowCategoryType.ALL, R.drawable.ic_nav_categories, R.string.all),
    TvShowCategoryType.ACTION_ADVENTURE to Genre(TvShowCategoryType.ACTION_ADVENTURE, R.drawable.ic_cat_action, R.string.action_adventure),
    TvShowCategoryType.ANIMATION to Genre(TvShowCategoryType.ANIMATION, R.drawable.ic_cat_animation, R.string.animation),
    TvShowCategoryType.COMEDY to Genre(TvShowCategoryType.COMEDY, R.drawable.ic_cat_comedy, R.string.comedy),
    TvShowCategoryType.CRIME to Genre(TvShowCategoryType.CRIME, R.drawable.ic_cat_crime, R.string.crime),
    TvShowCategoryType.DOCUMENTARY to Genre(TvShowCategoryType.DOCUMENTARY, R.drawable.ic_cat_documentary, R.string.documentary),
    TvShowCategoryType.DRAMA to Genre(TvShowCategoryType.DRAMA, R.drawable.ic_cat_drama, R.string.drama),
    TvShowCategoryType.FAMILY to Genre(TvShowCategoryType.FAMILY, R.drawable.ic_cat_family, R.string.family),
    TvShowCategoryType.FANTASY to Genre(TvShowCategoryType.FANTASY, R.drawable.ic_cat_fantasy, R.string.fantasy),
    TvShowCategoryType.MYSTERY to Genre(TvShowCategoryType.MYSTERY, R.drawable.ic_cat_mystery, R.string.mystery),
    TvShowCategoryType.ROMANCE to Genre(TvShowCategoryType.ROMANCE, R.drawable.ic_cat_romance, R.string.romance),
    TvShowCategoryType.SCIENCE_FICTION to Genre(TvShowCategoryType.SCIENCE_FICTION, R.drawable.ic_cat_sciencefiction, R.string.science_fiction),
    TvShowCategoryType.KIDS to Genre(TvShowCategoryType.KIDS, R.drawable.ic_cat_kids, R.string.kids),
    TvShowCategoryType.NEWS to Genre(TvShowCategoryType.NEWS, R.drawable.ic_cat_news, R.string.news),
    TvShowCategoryType.REALITY to Genre(TvShowCategoryType.REALITY, R.drawable.ic_cat_reality, R.string.reality),
    TvShowCategoryType.SOAP to Genre(TvShowCategoryType.SOAP, R.drawable.ic_cat_soap, R.string.soap),
    TvShowCategoryType.TALK to Genre(TvShowCategoryType.TALK, R.drawable.ic_cat_talk, R.string.talk),
    TvShowCategoryType.WAR_POLITICS to Genre(TvShowCategoryType.WAR_POLITICS, R.drawable.ic_cat_war, R.string.war),
    TvShowCategoryType.WESTERN to Genre(TvShowCategoryType.WESTERN, R.drawable.ic_cat_western, R.string.western),
)

@Composable
fun getTvShowGenreLabel(type: TvShowCategoryType): String {
    return stringResource(tvShowCategories[type]?.displayableName ?: R.string.all)
}

@Composable
fun getTvShowGenreIcon(type: TvShowCategoryType): Painter {
    return painterResource(tvShowCategories[type]?.icon ?: R.drawable.ic_nav_categories)
}
