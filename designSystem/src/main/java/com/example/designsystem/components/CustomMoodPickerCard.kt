package com.example.designsystem.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.designsystem.AflamiTheme
import com.example.designsystem.R

@Composable
fun CustomMoodPickerCard(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(
                brush = Brush.horizontalGradient(
                    colors = listOf(
                        AflamiTheme.color.primary,
                        AflamiTheme.color.redAccent,
                        AflamiTheme.color.yellowAccent,
                    )
                )
            )
            .border(
                width = 1.dp, color = AflamiTheme.color.stroke, shape = RoundedCornerShape(24.dp)
            ),
    ) {
        Image(
            painter = painterResource(R.drawable.img_game_funclown),
            contentDescription = null,
            contentScale = ContentScale.FillHeight,
            modifier = Modifier
                .padding(top = 8.dp)
                .align(Alignment.TopEnd)
                .height(121.dp)
        )
        Column(
        ) {
            BlurredBoxWithIcon()
            Text(
                text = "Mood Picker (Get a Movie)",
                color = AflamiTheme.color.onPrimary,
                style = AflamiTheme.textStyle.label.medium,
                modifier = Modifier.padding(start = 12.dp)
            )
        }
        ParentComponent(
            modifier = Modifier.padding(top = 76.dp, start = 2.dp, end = 2.dp, bottom = 2.dp)
        )
    }
}

@Composable
private fun ParentComponent(
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(AflamiTheme.color.surface),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        var isClicked by remember { mutableStateOf(false) }
        val color= if (isClicked) AflamiTheme.color.primary else AflamiTheme.color.disable
        Text(
            text = "What’s your vibe today? ",
             color = AflamiTheme.color.body,
            style = AflamiTheme.textStyle.body.small, modifier = Modifier.padding(12.dp)
        )
        Row(
            modifier = Modifier.padding(horizontal = 24.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)

        ) {
            ChildComponent(iconRes = R.drawable.ic_mood_sad)
            ChildComponent(iconRes = R.drawable.ic_mood_lookup)
            ChildComponent(iconRes = R.drawable.ic_mood_inlove)
            ChildComponent(iconRes = R.drawable.ic_mood_angry)
            ChildComponent(iconRes = R.drawable.ic_mood_unhappy)
            ChildComponent(iconRes = R.drawable.ic_mood_saddizzy)

        }
        Text(
            text = "Get now", style = AflamiTheme.textStyle.body.medium,
            color = color,
            modifier = Modifier.padding(top = 12.dp, bottom = 4.dp)
        )

    }
}

@Composable
private fun ChildComponent(
    modifier: Modifier = Modifier, iconRes: Int
) {
    var isSelected by remember { mutableStateOf(false) }
    val color = if (isSelected) AflamiTheme.color.primary else AflamiTheme.color.body
    Icon(
        painter = painterResource(iconRes),
        contentDescription = null,
        tint = color,
        modifier = Modifier
            .padding(4.dp)
            .size(24.dp)
            .clickable(onClick = { isSelected = !isSelected })
    )
}

@Composable
private fun BlurredBoxWithIcon(
    modifier: Modifier=Modifier
) {
    Box(
        modifier = modifier
    ) {
        Box(
            modifier = Modifier
                .matchParentSize()
                .padding(start = 12.dp, top = 12.dp, bottom = 8.dp)
                .background(color = AflamiTheme.color.onPrimaryButton, shape = CircleShape)
                .border(
                    width = 0.5.dp, brush = Brush.linearGradient(
                        colors = AflamiTheme.color.overlayGradient // this will be change
                    ), shape = CircleShape
                )
                .blur(8.dp)
        )
        Icon(
           painter = painterResource(R.drawable.ic_filled_favourite),
            contentDescription = null,
            tint = AflamiTheme.color.onPrimary,
            modifier = Modifier
                .padding(4.dp)
                .size(16.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun CustomMoodPickerCardPreview() {
    CustomMoodPickerCard(modifier = Modifier.padding(horizontal = 16.dp, vertical = 150.dp))
}
