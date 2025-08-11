package com.amsterdam.ui.screens.letsPlay.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.amsterdam.designsystem.components.Text
import com.amsterdam.designsystem.theme.AppTheme
import com.amsterdam.ui.R

@Composable
fun CompletionCard(modifier: Modifier = Modifier) {
    val isDarkTheme = isSystemInDarkTheme()

    val beamImageRes = if (isDarkTheme) {
        R.drawable.beam_dark
    } else {
        R.drawable.beam_light
    }

    val backgroundColor = AppTheme.color.primary.copy(alpha = 0.1f)

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(backgroundColor)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxWidth()
                .height(176.dp)
        ) {
            Image(
                painter = painterResource(id = beamImageRes),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.img_cup),
                    contentDescription = "Trophy",
                    modifier = Modifier.size(95.dp)
                )
                Text(
                    text = stringResource(R.string.finish_game_message),
                    color = AppTheme.color.title,
                    style = AppTheme.textStyle.title.large,
                    fontSize = 18.sp
                )
            }
        }
    }
}