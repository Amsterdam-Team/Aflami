package com.amsterdam.ui.screens.profile.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.amsterdam.designsystem.TopAppBar
import com.amsterdam.designsystem.components.CenterOfScreenContainer
import com.amsterdam.designsystem.components.Text
import com.amsterdam.designsystem.components.buttons.OutlinedButton
import com.amsterdam.designsystem.theme.AflamiTheme
import com.amsterdam.designsystem.theme.AppTheme
import com.amsterdam.designsystem.utils.ThemeAndLocalePreviews
import com.amsterdam.designsystem.utils.modifierExtensions.dropShadow
import com.amsterdam.ui.R

@Composable
fun NotLoggedInContent(onClickLogin: () -> Unit) {
    var topBarHeight by remember { mutableIntStateOf(0) }
    Column(
        modifier = Modifier.statusBarsPadding()
    ) {
        TopAppBar(
            title = {
                Text(
                    text = stringResource(R.string.profile),
                    style = AppTheme.textStyle.title.large,
                    color = AppTheme.color.title,
                    modifier = Modifier
                        .padding(horizontal = 16.dp, vertical = 13.dp)
                        .onSizeChanged {
                            topBarHeight = it.height
                        }
                )
            },
        )
        LoginSection(
            modifier = Modifier
                .fillMaxSize()
                .background(AppTheme.color.surface)
                .padding(horizontal = 48.dp),
            unneededSpace = topBarHeight.dp,
            onClickLogin = onClickLogin
        )
    }
}

@Composable
private fun LoginSection(
    modifier: Modifier = Modifier,
    unneededSpace: Dp,
    onClickLogin: () -> Unit
) {
    CenterOfScreenContainer(
        unneededSpace = unneededSpace,
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(R.drawable.img_empty_user_pic),
                contentDescription = stringResource(R.string.profile),
                modifier = Modifier
                    .size(80.dp)
                    .dropShadow(
                        blur = 12.dp,
                        shape = RoundedCornerShape(24.dp),
                        color = AppTheme.color.droppedShadowColor
                    )
                    .clip(shape = RoundedCornerShape(24.dp))
                    .border(
                        width = 1.dp, AppTheme.color.stroke,
                        shape = RoundedCornerShape(24.dp)
                    )
            )

            Text(
                text = stringResource(R.string.login_message),
                style = AppTheme.textStyle.body.small,
                color = AppTheme.color.body,
                modifier = Modifier.padding(top = 12.dp),
                textAlign = TextAlign.Center
            )

            OutlinedButton(
                modifier = Modifier
                    .padding(top = 24.dp)
                    .width(95.dp),
                title = stringResource(R.string.login),
                onClick = onClickLogin,
                isEnabled = true,
                isLoading = false,
                isNegative = false
            )
        }
    }
}

@ThemeAndLocalePreviews
@Composable
private fun LoginSectionPreview() {
    AflamiTheme {
        NotLoggedInContent {}
    }
}