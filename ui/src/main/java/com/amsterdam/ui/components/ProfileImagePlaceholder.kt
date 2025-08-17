package com.amsterdam.ui.components

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.amsterdam.ui.R
@Composable
fun ProfileImagePlaceholder() {
    Image(
        painter = painterResource(R.drawable.img_empty_user_pic),
        contentDescription = stringResource(R.string.profile),
    )
}