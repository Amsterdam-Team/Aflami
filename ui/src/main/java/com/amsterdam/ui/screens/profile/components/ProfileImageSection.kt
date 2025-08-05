package com.amsterdam.ui.screens.profile.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.amsterdam.designsystem.components.ImageLoadingIndicator
import com.amsterdam.designsystem.theme.AflamiTheme
import com.amsterdam.designsystem.theme.AppTheme
import com.amsterdam.designsystem.utils.ThemeAndLocalePreviews
import com.amsterdam.designsystem.utils.modifierExtensions.dropShadow
import com.amsterdam.imageviewer.ui.SafeImageView
import com.amsterdam.ui.R

@Composable
fun ProfileImageSection(userAvatarUrl: String) {
    Box {
        Image(
            painter = painterResource(R.drawable.profile_background_image),
            contentDescription = stringResource(R.string.profile),
            modifier = Modifier
                .padding(bottom = 27.dp)
                .fillMaxWidth()
                .height(211.dp)
                .dropShadow(
                    blur = 4.dp,
                    shape = RoundedCornerShape(24.dp),
                    color = AppTheme.color.profileOverlay
                )
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            AppTheme.color.surface,
                            AppTheme.color.surface
                        )
                    )
                ),
            contentScale = ContentScale.Crop
        )
        SafeImageView(
            model = userAvatarUrl,
            contentDescription = stringResource(R.string.profile),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(top = 46.dp)
                .size(96.dp)
                .dropShadow(
                    blur = 12.dp,
                    shape = RoundedCornerShape(24.dp),
                    color = AppTheme.color.droppedShadowColor
                )
                .clip(shape = RoundedCornerShape(24.dp))
                .border(
                    width = 1.dp, AppTheme.color.stroke,
                    shape = RoundedCornerShape(24.dp)
                ),
            onLoading = { ImageLoadingIndicator() },
            onError = { ProfileImagePlaceholder() }
        )
    }
}

@Composable
private fun ProfileImagePlaceholder() {
    Image(
        painter = painterResource(R.drawable.img_empty_user_pic),
        contentDescription = stringResource(R.string.profile),
    )
}

@ThemeAndLocalePreviews
@Composable
private fun ProfileImageSectionPreview() {
    AflamiTheme {
        ProfileImageSection("")
    }
}