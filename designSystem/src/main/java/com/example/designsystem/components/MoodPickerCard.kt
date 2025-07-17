package com.example.designsystem.components

import androidx.compose.animation.animateColorAsState
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.designsystem.R
import com.example.designsystem.theme.AflamiTheme
import com.example.designsystem.theme.AppTheme
import com.example.designsystem.utils.ThemeAndLocalePreviews

@Composable
fun MoodPickerCard(
    modifier: Modifier = Modifier,
    onMoodSelected: (Int) -> Unit = {},
) {
    Box(
        modifier =
            modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(24.dp))
                .background(
                    Brush.horizontalGradient(
                        colors =
                            listOf(
                                AppTheme.color.primary,
                                AppTheme.color.redAccent,
                                AppTheme.color.yellowAccent,
                            ),
                    ),
                )
                .border(1.dp, AppTheme.color.stroke, RoundedCornerShape(24.dp)),
    ) {
        Image(
            painter = painterResource(R.drawable.img_mood_fun_clown),
            contentDescription = null,
            contentScale = ContentScale.FillHeight,
            modifier =
                Modifier
                    .padding(top = 8.dp)
                    .align(Alignment.TopEnd)
                    .height(121.dp),
        )

        Column {
            BlurredBoxWithIcon()
            Text(
                text = stringResource(R.string.mood_picker_title),
                color = AppTheme.color.onPrimary,
                style = AppTheme.textStyle.label.medium,
                modifier = Modifier.padding(start = 12.dp),
            )
        }

        MoodOptionsSection(
            modifier =
                Modifier
                    .padding(top = 76.dp, start = 2.dp, end = 2.dp, bottom = 2.dp),
            onMoodSelected = onMoodSelected,
        )
    }
}

@Composable
private fun MoodOptionsSection(
    modifier: Modifier = Modifier,
    onMoodSelected: (Int) -> Unit = {},
) {
    var selectedIndex by remember { mutableStateOf(-1) }

    val moodIcons =
        listOf(
            R.drawable.ic_mood_sad,
            R.drawable.ic_mood_lookup,
            R.drawable.ic_mood_inlove,
            R.drawable.ic_mood_angry,
            R.drawable.ic_mood_unhappy,
            R.drawable.ic_mood_saddizzy,
        )

    val buttonColor by animateColorAsState(
        if (selectedIndex != -1) AppTheme.color.primary else AppTheme.color.disable,
    )

    Column(
        modifier =
            modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(22.dp))
                .background(AppTheme.color.surface),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            text = stringResource(R.string.mood_picker_question),
            color = AppTheme.color.body,
            style = AppTheme.textStyle.body.small,
            modifier = Modifier.padding(12.dp),
        )

        Row(
            modifier = Modifier.padding(horizontal = 24.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            moodIcons.forEachIndexed { index, iconRes ->
                MoodIcon(
                    iconRes = iconRes,
                    isSelected = selectedIndex == index,
                    onClick = {
                        selectedIndex = index
                        onMoodSelected(index)
                    },
                )
            }
        }

        Text(
            text = stringResource(R.string.get_now),
            style = AppTheme.textStyle.body.medium,
            color = buttonColor,
            modifier = Modifier.padding(vertical = 12.dp),
        )
    }
}

@Composable
private fun MoodIcon(
    iconRes: Int,
    isSelected: Boolean,
    onClick: () -> Unit,
) {
    val tint by animateColorAsState(
        if (isSelected) AppTheme.color.primary else AppTheme.color.body,
    )

    Icon(
        painter = painterResource(iconRes),
        contentDescription = null,
        tint = tint,
        modifier =
            Modifier
                .padding(4.dp)
                .size(24.dp)
                .clickable(onClick = onClick),
    )
}

@Composable
private fun BlurredBoxWithIcon(modifier: Modifier = Modifier) {
    Box(
        modifier =
            modifier
                .padding(start = 12.dp, top = 12.dp, bottom = 8.dp)
                .size(24.dp),
    ) {
        Box(
            modifier =
                Modifier
                    .matchParentSize()
                    .background(AppTheme.color.onPrimaryButton, CircleShape)
                    .border(
                        width = 0.5.dp,
                        brush = Brush.linearGradient(AppTheme.color.borderLinearGradient),
                        shape = CircleShape,
                    )
                    .blur(8.dp),
        )
        Icon(
            painter = painterResource(R.drawable.ic_filled_favourite),
            contentDescription = null,
            tint = AppTheme.color.onPrimary,
            modifier =
                Modifier
                    .padding(4.dp)
                    .size(16.dp),
        )
    }
}

@ThemeAndLocalePreviews
@Composable
private fun CustomMoodPickerCardPreview() {
    AflamiTheme {
        MoodPickerCard(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 100.dp),
        )
    }
}
