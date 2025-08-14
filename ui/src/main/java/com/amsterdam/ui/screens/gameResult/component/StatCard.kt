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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.amsterdam.designsystem.components.Text
import com.amsterdam.designsystem.theme.AppTheme
import com.amsterdam.designsystem.theme.LocalIsDarkTheme
import com.amsterdam.ui.R

@Composable
fun StatCard(
    modifier: Modifier = Modifier,
    iconRes: Int,
    label: String,
    value: String
) {
    val isDarkTheme = LocalIsDarkTheme.current
    val beamImageRes = if (isDarkTheme) {
        R.drawable.beem_dark
    } else {
        R.drawable.beem_light
    }

    Box(
        modifier = modifier,
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            modifier = Modifier
                .padding(top = 35.dp)
                .fillMaxWidth()
                .clip(RoundedCornerShape(24.dp))
                .background(AppTheme.color.surface),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(92.dp)
                    .padding(vertical = 2.dp, horizontal = 2.dp)
                    .border(
                        BorderStroke(1.dp, AppTheme.color.stroke),
                        RoundedCornerShape(24.dp)
                    )
                    .clip(RoundedCornerShape(16.dp))
                    .padding(horizontal = 16.dp)
            ) {
                Image(
                    painter = painterResource(id = beamImageRes),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }

            Column(
                modifier = Modifier.padding(vertical = 8.dp, horizontal = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = label,
                    color = AppTheme.color.hint,
                    style = AppTheme.textStyle.label.medium,
                )
                Text(
                    text = value,
                    color = AppTheme.color.title,
                    style = AppTheme.textStyle.headline.medium,
                )
            }
        }

        Image(
            painter = painterResource(id = iconRes),
            contentDescription = label,
            modifier = Modifier
                .size(70.dp)
                .offset(y = 15.dp)
        )
    }
}