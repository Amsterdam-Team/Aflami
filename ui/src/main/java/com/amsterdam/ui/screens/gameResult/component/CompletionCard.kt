package com.amsterdam.ui.screens.gameResult.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.amsterdam.designsystem.components.Text
import com.amsterdam.designsystem.theme.AppTheme
import com.amsterdam.designsystem.theme.LocalIsDarkTheme
import com.amsterdam.ui.R

@Composable
fun CompletionCard(
    isWin: Boolean,
    modifier: Modifier = Modifier
) {
    val isDarkTheme = LocalIsDarkTheme.current

    val beamImageRes = if (isDarkTheme) {
        R.drawable.beem_dark
    } else {
        R.drawable.beem_light
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .fillMaxWidth()
            .height(178.dp)
            .border(
                border = BorderStroke(width = 1.dp, color = AppTheme.color.stroke),
                shape = RoundedCornerShape(24.dp)
            )
            .padding(1.dp)
            .background(
                color = AppTheme.color.primaryVariant,
                shape = RoundedCornerShape(24.dp)
            )

            .border(
                border = BorderStroke(width = 8.dp, color = AppTheme.color.surface),
                shape = RoundedCornerShape(24.dp)
            )
            .clip(RoundedCornerShape(24.dp))
    ) {
        Image(
            painter = painterResource(id = beamImageRes),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Column(
            modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 4.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            val resultImage = if (isWin) {
                R.drawable.img_cup
            } else {
                R.drawable.alert
            }

            val resultMessage = if (isWin) {
                stringResource(R.string.finish_game_message)
            } else {
                stringResource(R.string.lose_the_game_message)
            }
            Image(
                painter = painterResource(id = resultImage),
                contentDescription = "Trophy",
                modifier = Modifier.height(91.dp),
                contentScale = ContentScale.FillHeight
            )
            Text(
                modifier = Modifier.padding(top = 12.dp),
                text = resultMessage,
                color = AppTheme.color.title,
                style = AppTheme.textStyle.title.medium,
                textAlign = TextAlign.Center
            )
        }
    }
}