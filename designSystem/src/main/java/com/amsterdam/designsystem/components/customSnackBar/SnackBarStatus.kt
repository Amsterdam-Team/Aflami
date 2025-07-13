package com.amsterdam.designsystem.components.customSnackBar

import androidx.annotation.DrawableRes
import com.amsterdam.designsystem.R
import com.amsterdam.designsystem.theme.AppTheme
import com.amsterdam.designsystem.theme.ColorType

enum class SnackBarStatus(
    @DrawableRes val icon: Int,
    val iconTintColor: ColorType,
    val dropShadowColor: ColorType,
) {
    Success(
        R.drawable.ic_thumbs_up,
        iconTintColor = { AppTheme.color.greenAccent },
        dropShadowColor = { AppTheme.color.successSnackBarShadow }
    ),
    Failure(
        R.drawable.ic_thumbs_down,
        iconTintColor = { AppTheme.color.redAccent },
        dropShadowColor = { AppTheme.color.failureSnackBarShadow }
    );
}