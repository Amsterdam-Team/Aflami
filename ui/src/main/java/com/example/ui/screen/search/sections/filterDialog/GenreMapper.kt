package com.example.ui.screen.search.sections.filterDialog

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.example.designsystem.R
import com.example.viewmodel.common.MovieGenreType

fun getGenreIcon(movieGenreType: MovieGenreType): Int {
    return when (movieGenreType) {
        MovieGenreType.ALL -> R.drawable.ic_nav_categories
        MovieGenreType.ROMANCE -> R.drawable.ic_cat_romance
        MovieGenreType.SCIENCE_FICTION -> R.drawable.ic_cat_sciencefiction
        MovieGenreType.FAMILY -> R.drawable.ic_cat_family
        MovieGenreType.MYSTERY -> R.drawable.ic_cat_mystery
        MovieGenreType.HISTORY -> R.drawable.ic_cat_history
        MovieGenreType.WAR -> R.drawable.ic_cat_war
        MovieGenreType.ACTION -> R.drawable.ic_cat_action
        MovieGenreType.CRIME -> R.drawable.ic_cat_crime
        MovieGenreType.COMEDY -> R.drawable.ic_cat_comedy
        MovieGenreType.HORROR -> R.drawable.ic_cat_horror
        MovieGenreType.WESTERN -> R.drawable.ic_cat_western
        MovieGenreType.MUSIC -> R.drawable.ic_cat_music
        MovieGenreType.ADVENTURE -> R.drawable.ic_cat_adventure
        MovieGenreType.TV_MOVIE -> R.drawable.ic_cat_tvmovie
        MovieGenreType.FANTASY -> R.drawable.ic_cat_fantasy
        MovieGenreType.THRILLER -> R.drawable.ic_cat_thriller
        MovieGenreType.DRAMA -> R.drawable.ic_cat_drama
        MovieGenreType.DOCUMENTARY -> R.drawable.ic_cat_documentary
        MovieGenreType.ANIMATION -> R.drawable.ic_cat_animation
    }
}



data class Genre(
    val name: String,
    @DrawableRes val icon: Int,
)

val movieGenres: List<Genre> = listOf(
    Genre("All", R.drawable.ic_nav_categories),
    Genre("Action", R.drawable.ic_cat_action),
    Genre("Adventure", R.drawable.ic_cat_adventure),
    Genre("Animation", R.drawable.ic_cat_animation),
    Genre("Comedy", R.drawable.ic_cat_comedy),
    Genre("Crime", R.drawable.ic_cat_crime),
    Genre("Documentary", R.drawable.ic_cat_documentary),
    Genre("Drama", R.drawable.ic_cat_drama),
    Genre("Family", R.drawable.ic_cat_family),
    Genre("Fantasy", R.drawable.ic_cat_fantasy),
    Genre("History", R.drawable.ic_cat_history),
    Genre("Horror", R.drawable.ic_cat_horror),
    Genre("Music", R.drawable.ic_cat_music),
    Genre("Mystery", R.drawable.ic_cat_mystery),
    Genre("Romance", R.drawable.ic_cat_romance),
    Genre("Science Fiction", R.drawable.ic_cat_sciencefiction),
    Genre("TV Movie", R.drawable.ic_cat_tvmovie),
    Genre("Thriller", R.drawable.ic_cat_thriller),
    Genre("War", R.drawable.ic_cat_war),
    Genre("Western", R.drawable.ic_cat_western)
)

val tvGenres: List<Genre> = listOf(
    Genre("Romance", R.drawable.ic_cat_romance),
    Genre("All", R.drawable.ic_nav_categories),
    Genre("Action & Adventure", R.drawable.ic_cat_action),
    Genre("Animation", R.drawable.ic_cat_animation),
    Genre("Comedy", R.drawable.ic_cat_comedy),
    Genre("Crime", R.drawable.ic_cat_crime),
    Genre("Documentary", R.drawable.ic_cat_documentary),
    Genre("Drama", R.drawable.ic_cat_drama),
    Genre("Family", R.drawable.ic_cat_family),
    Genre("Kids", R.drawable.ic_cat_kids),
    Genre("Mystery", R.drawable.ic_cat_mystery),
    Genre("News", R.drawable.ic_cat_news),
    Genre("Reality", R.drawable.ic_cat_reality),
    Genre("Sci-Fi & Fantasy", R.drawable.ic_cat_sciencefiction),
    Genre("Soap", R.drawable.ic_cat_soap),
    Genre("Talk", R.drawable.ic_cat_talk),
    Genre("War & Politics", R.drawable.ic_cat_war),
    Genre("Western", R.drawable.ic_cat_western)
)