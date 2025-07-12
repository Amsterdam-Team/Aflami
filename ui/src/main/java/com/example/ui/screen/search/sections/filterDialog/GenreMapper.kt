package com.example.ui.screen.search.sections.filterDialog

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.example.designsystem.R
import com.example.viewmodel.common.TabOption

data class Genre(
    val name: String,
    @DrawableRes val icon: Int,
    @StringRes val displayableName: Int
)

val movieGenres: List<Genre> = listOf(
    Genre("All", R.drawable.ic_nav_categories, R.string.all),
    Genre("Action", R.drawable.ic_cat_action, R.string.action),
    Genre("Adventure", R.drawable.ic_cat_adventure, R.string.adventure),
    Genre("Animation", R.drawable.ic_cat_animation, R.string.animation),
    Genre("Comedy", R.drawable.ic_cat_comedy, R.string.comedy),
    Genre("Crime", R.drawable.ic_cat_crime, R.string.crime),
    Genre("Documentary", R.drawable.ic_cat_documentary, R.string.documentary),
    Genre("Drama", R.drawable.ic_cat_drama, R.string.drama),
    Genre("Family", R.drawable.ic_cat_family, R.string.family),
    Genre("Fantasy", R.drawable.ic_cat_fantasy, R.string.fantasy),
    Genre("History", R.drawable.ic_cat_history, R.string.history),
    Genre("Horror", R.drawable.ic_cat_horror, R.string.horror),
    Genre("Music", R.drawable.ic_cat_music, R.string.music),
    Genre("Mystery", R.drawable.ic_cat_mystery, R.string.mystery),
    Genre("Romance", R.drawable.ic_cat_romance, R.string.romance),
    Genre("Science Fiction", R.drawable.ic_cat_sciencefiction, R.string.science_fiction),
    Genre("TV Movie", R.drawable.ic_cat_tvmovie, R.string.tv_movie),
    Genre("Thriller", R.drawable.ic_cat_thriller, R.string.thriller),
    Genre("War", R.drawable.ic_cat_war, R.string.war),
    Genre("Western", R.drawable.ic_cat_western, R.string.western)
)

val tvGenres: List<Genre> = listOf(
    Genre("All", R.drawable.ic_nav_categories, R.string.all),
    Genre("Action & Adventure", R.drawable.ic_cat_action, R.string.action_adventure),
    Genre("Animation", R.drawable.ic_cat_animation, R.string.animation),
    Genre("Comedy", R.drawable.ic_cat_comedy, R.string.comedy),
    Genre("Crime", R.drawable.ic_cat_crime, R.string.crime),
    Genre("Documentary", R.drawable.ic_cat_documentary, R.string.documentary),
    Genre("Drama", R.drawable.ic_cat_drama, R.string.drama),
    Genre("Family", R.drawable.ic_cat_family, R.string.family),
    Genre("Kids", R.drawable.ic_cat_kids, R.string.kids),
    Genre("Mystery", R.drawable.ic_cat_mystery, R.string.mystery),
    Genre("Romance", R.drawable.ic_cat_romance, R.string.romance),
    Genre("News", R.drawable.ic_cat_news, R.string.news),
    Genre("Reality", R.drawable.ic_cat_reality, R.string.reality),
    Genre("Sci-Fi & Fantasy", R.drawable.ic_cat_sciencefiction, R.string.science_fiction_fantasy),
    Genre("Soap", R.drawable.ic_cat_soap, R.string.romance),
    Genre("Talk", R.drawable.ic_cat_talk, R.string.science_fiction),
    Genre("War & Politics", R.drawable.ic_cat_war, R.string.tv_movie),
    Genre("Western", R.drawable.ic_cat_western, R.string.thriller)
)


fun getGenreLabel(genreName: String, tabOption: TabOption): Int {
    return if (tabOption == TabOption.MOVIES) {
        movieGenres.find { it.name == genreName }?.displayableName ?: R.string.all
    } else {
        tvGenres.find { it.name == genreName }?.displayableName ?: R.string.all
    }
}

fun getGenreIcon(genreName: String, tabOption: TabOption): Int {
    return if (tabOption == TabOption.TV_SHOWS) {
        movieGenres.find { it.name == genreName }?.icon ?: R.string.all
    } else {
        tvGenres.find { it.name == genreName }?.icon ?: R.string.all
    }
}