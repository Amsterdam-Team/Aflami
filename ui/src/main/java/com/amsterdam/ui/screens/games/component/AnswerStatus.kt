package com.amsterdam.ui.screens.games.component

import androidx.annotation.DrawableRes
import com.amsterdam.designsystem.R
import com.amsterdam.designsystem.theme.AppTheme
import com.amsterdam.designsystem.theme.ColorType

enum class AnswerStatus(
    @DrawableRes val icon: Int,
    val backgroundColor: ColorType,
    val borderColor: ColorType
) {
    Unselected(
        icon = R.drawable.ic_add_circle,
        backgroundColor = { AppTheme.color.surface },
        borderColor = { AppTheme.color.stroke }
    ),
    Correct(
        icon = R.drawable.ic_checkmark_circle,
        backgroundColor = { AppTheme.color.greenVariant },
        borderColor = { AppTheme.color.greenAccent }
    ),
    Wrong(
        icon = R.drawable.ic_cancel_circle,
        backgroundColor = { AppTheme.color.redVariant },
        borderColor = { AppTheme.color.redAccent }
    )


}

