package com.amsterdam.ui.screens.home.model

import androidx.annotation.DrawableRes
import com.amsterdam.ui.R

enum class CardMood(@DrawableRes val iconResourceId: Int) {
    SAD(R.drawable.ic_mood_sad),
    NEUTRAL(R.drawable.ic_mood_lookup),
    ROMANTIC(R.drawable.ic_mood_inlove),
    ANGRY(R.drawable.ic_mood_angry),
    DEPRESSED(R.drawable.ic_mood_unhappy),
    SAD_DIZZY(R.drawable.ic_mood_saddizzy);
}