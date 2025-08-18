package com.amsterdam.ui.screens.categoriesDetails

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.amsterdam.ui.R

enum class GenreTvShowUiModel (
    @StringRes val displayName: Int,
    @DrawableRes val imageRes: Int

){
    SCIENCE_FICTION_FANTASY( R.string.science_fiction, R.drawable.img_sciencefiction),
    CRIME( R.string.crime, R.drawable.img_crime),
    FAMILY( R.string.family, R.drawable.img_family),
    MYSTERY( R.string.mystery, R.drawable.img_mystery),
    WAR_POLITICS( R.string.war, R.drawable.img_war),
    ANIMATION( R.string.animation, R.drawable.igm_animation),
    COMEDY( R.string.comedy, R.drawable.img_comedy),
    DRAMA( R.string.drama, R.drawable.img_drama),
    KIDS( R.string.kids, R.drawable.img_kids),
    ACTION_ADVENTURE( R.string.action_adventure, R.drawable.img_adventure),
    REALITY( R.string.reality, R.drawable.img_reality),
    SOAP( R.string.soap, R.drawable.img_soap),
    TALK( R.string.talk, R.drawable.img_talk),
    WESTERN( R.string.western, R.drawable.img_western),
    DOCUMENTARY( R.string.documentary, R.drawable.img_documentary)
}