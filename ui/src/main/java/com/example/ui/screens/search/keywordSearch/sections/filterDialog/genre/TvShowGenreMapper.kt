package com.example.ui.screens.search.keywordSearch.sections.filterDialog.genre

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.example.designsystem.R
import com.example.entity.category.TvShowGenre

internal val TvShowGenre.uiModel: GenreUiModel
    get() = when (this) {
        TvShowGenre.ALL -> GenreUiModel(R.drawable.ic_nav_categories, R.string.all)
        TvShowGenre.ACTION_ADVENTURE -> GenreUiModel(
            R.drawable.ic_cat_action,
            R.string.action_adventure
        )

        TvShowGenre.ANIMATION -> GenreUiModel(R.drawable.ic_cat_animation, R.string.animation)
        TvShowGenre.COMEDY -> GenreUiModel(R.drawable.ic_cat_comedy, R.string.comedy)
        TvShowGenre.CRIME -> GenreUiModel(R.drawable.ic_cat_crime, R.string.crime)
        TvShowGenre.DOCUMENTARY -> GenreUiModel(R.drawable.ic_cat_documentary, R.string.documentary)
        TvShowGenre.DRAMA -> GenreUiModel(R.drawable.ic_cat_drama, R.string.drama)
        TvShowGenre.FAMILY -> GenreUiModel(R.drawable.ic_cat_family, R.string.family)
        TvShowGenre.KIDS -> GenreUiModel(R.drawable.ic_cat_kids, R.string.kids)
        TvShowGenre.MYSTERY -> GenreUiModel(R.drawable.ic_cat_mystery, R.string.mystery)
        TvShowGenre.NEWS -> GenreUiModel(R.drawable.ic_cat_news, R.string.news)
        TvShowGenre.REALITY -> GenreUiModel(R.drawable.ic_cat_reality, R.string.reality)
        TvShowGenre.SCIENCE_FICTION_FANTASY -> GenreUiModel(
            R.drawable.ic_cat_sciencefiction,
            R.string.science_fiction
        )

        TvShowGenre.SOAP -> GenreUiModel(R.drawable.ic_cat_soap, R.string.soap)
        TvShowGenre.TALK -> GenreUiModel(R.drawable.ic_cat_talk, R.string.talk)
        TvShowGenre.WAR_POLITICS -> GenreUiModel(R.drawable.ic_cat_war, R.string.war)
        TvShowGenre.WESTERN -> GenreUiModel(R.drawable.ic_cat_western, R.string.western)
    }

@Composable
internal fun getTvShowGenreLabel(type: TvShowGenre): String {
    return stringResource(id = type.uiModel.displayableName)
}

@Composable
internal fun getTvShowGenreIcon(type: TvShowGenre): Painter {
    return painterResource(id = type.uiModel.icon)
}