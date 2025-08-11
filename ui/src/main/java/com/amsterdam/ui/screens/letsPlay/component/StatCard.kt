package com.amsterdam.ui.screens.letsPlay.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.amsterdam.designsystem.components.Text
import com.amsterdam.designsystem.theme.AflamiTheme
import com.amsterdam.designsystem.theme.AppTheme
import com.amsterdam.designsystem.utils.ThemeAndLocalePreviews
import com.amsterdam.ui.R

@Composable
fun StatCard(
    modifier: Modifier = Modifier,
    iconRes: Int,
    label: String,
    value: String
) {
    val isDarkTheme = isSystemInDarkTheme()
    val beamImageRes = if (isDarkTheme) {
        R.drawable.beam_dark
    } else {
        R.drawable.beam_light
    }
    val backgroundColor = AppTheme.color.primary.copy(alpha = 0.1f)

    Box(
        modifier = modifier,
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            modifier = Modifier
                .padding(top = 20.dp)
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(backgroundColor),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier.padding(top = 32.dp),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = beamImageRes),
                    contentDescription = null,
                    modifier = Modifier.height(92.dp),
                    contentScale = ContentScale.FillHeight
                )

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = label,
                        color = AppTheme.color.hint,
                        style = AppTheme.textStyle.body.medium,
                        fontSize = 14.sp
                    )
                    Text(
                        text = value,
                        color = AppTheme.color.title,
                        style = AppTheme.textStyle.title.medium,
                        fontWeight = FontWeight.Bold,
                        fontSize = 24.sp
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
        }

        Image(
            painter = painterResource(id = iconRes),
            contentDescription = label,
            modifier = Modifier.size(70.dp)
        )
    }
}

@ThemeAndLocalePreviews
@Composable
private fun StatCardPreview() {
    AflamiTheme {
        Row(
            modifier = Modifier
                .padding(16.dp) 
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            StatCard(
                modifier = Modifier.weight(1f),
                iconRes = com.amsterdam.designsystem.R.drawable.img_user_rating,
                label = "Points Achieved",
                value = "110 Pts."
            )
            StatCard(
                modifier = Modifier.weight(1f),
                iconRes = R.drawable.img_user_history,
                label = "Total time",
                value = "110 Sec."
            )
        }
    }
}