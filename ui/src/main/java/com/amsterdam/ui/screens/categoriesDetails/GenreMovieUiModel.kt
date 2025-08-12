package com.amsterdam.ui.screens.categoriesDetails

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.amsterdam.ui.R

enum class GenreMovieUiModel (
    @StringRes val displayName: Int,
    @DrawableRes val imageRes: Int
){
    SCIENCE_FICTION( R.string.science_fiction, R.drawable.img_sciencefiction),
    FAMILY( R.string.family, R.drawable.img_family),
    MYSTERY( R.string.mystery, R.drawable.img_mystery),
    HISTORY( R.string.history, R.drawable.img_history),
    WAR( R.string.war, R.drawable.img_war),
    ACTION( R.string.action, R.drawable.img_action),
    CRIME( R.string.crime, R.drawable.img_crime),
    COMEDY( R.string.comedy, R.drawable.img_comedy),
    HORROR( R.string.horror, R.drawable.img_horror),
    WESTERN( R.string.western, R.drawable.img_western),
    ROMANCE( R.string.romance, R.drawable.img_romance),
    ADVENTURE( R.string.adventure, R.drawable.img_adventure),
    TV_MOVIE( R.string.tv_movie, R.drawable.img_tvmovie),
    FANTASY( R.string.fantasy, R.drawable.img_fantasy),
    THRILLER( R.string.thriller, R.drawable.img_thriller),
    DRAMA( R.string.drama, R.drawable.img_drama),
    ANIMATION( R.string.animation, R.drawable.igm_animation),
    MUSIC( R.string.music, R.drawable.img_music),
    DOCUMENTARY( R.string.documentary, R.drawable.img_documentary)
}