package com.amsterdam.ui.screens.home.component

import android.annotation.SuppressLint
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.amsterdam.designsystem.components.Icon
import com.amsterdam.designsystem.components.Text
import com.amsterdam.designsystem.components.buttons.PlainTextButton
import com.amsterdam.designsystem.theme.AflamiTheme
import com.amsterdam.designsystem.theme.AppTheme
import com.amsterdam.designsystem.utils.ThemeAndLocalePreviews
import com.amsterdam.ui.R
import com.amsterdam.ui.screens.home.model.CardMood
import io.sifr.shaded.modifiers.blur

@Composable
fun MoodPickerCard(
    cardMoods: List<CardMood>,
    selectedMood: CardMood?,
    isButtonEnabled: Boolean,
    isLoading: Boolean,
    modifier: Modifier = Modifier,
    onSelectMood: (CardMood) -> Unit = {},
    onClickGetNow: () -> Unit = {}
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
                    alpha = 0.7f
                )
                .border(1.dp, AppTheme.color.stroke, RoundedCornerShape(24.dp))
    ) {
        Image(
            modifier =
                Modifier
                    .align(Alignment.TopEnd)
                    .padding(top = 8.dp)
                    .height(121.dp),
            painter = painterResource(R.drawable.img_mood_fun_clown),
            contentDescription = null,
            contentScale = ContentScale.FillHeight
        )

        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            BlurredBoxWithIcon()
            Text(
                modifier = Modifier
                    .padding(start = 12.dp)
                    .fillMaxWidth(0.6f),
                text = stringResource(R.string.mood_picker_title),
                color = AppTheme.color.onPrimary,
                style = AppTheme.textStyle.label.medium
            )
            MoodOptionsSection(
                modifier = Modifier.padding(top = 10.dp, start = 2.dp, end = 2.dp, bottom = 2.dp),
                cardMoods = cardMoods,
                selectedMood = selectedMood,
                isButtonEnabled = isButtonEnabled,
                isLoading = isLoading,
                onSelectMood = onSelectMood,
                onClickGetNow = onClickGetNow
            )
        }
    }
}

@SuppressLint("UnrememberedMutableInteractionSource")
@Composable
private fun MoodOptionsSection(
    cardMoods: List<CardMood>,
    selectedMood: CardMood?,
    isButtonEnabled: Boolean,
    isLoading: Boolean,
    modifier: Modifier = Modifier,
    onSelectMood: (CardMood) -> Unit = {},
    onClickGetNow: () -> Unit = {},
) {
    val selectedIndex = cardMoods.indexOfFirst { it.name == selectedMood?.name }

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
            textAlign = TextAlign.Center
        )

        LazyRow(
            contentPadding = PaddingValues(horizontal = 24.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            itemsIndexed(items = cardMoods) { index, mood ->
                MoodIcon(
                    iconRes = mood.iconResourceId,
                    isSelected = selectedIndex == index,
                    onClick = { onSelectMood(mood) },
                )
            }
        }

        PlainTextButton(
            title = stringResource(R.string.get_now),
            style = AppTheme.textStyle.label.medium,
            onClick = onClickGetNow,
            isEnabled = isButtonEnabled,
            isLoading = isLoading,
            isNegative = false,
            modifier = Modifier.padding(vertical = 12.dp)
        )
    }
}

@SuppressLint("UnrememberedMutableInteractionSource")
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
        modifier = Modifier
            .size(32.dp)
            .clickable(
                interactionSource = MutableInteractionSource(),
                indication = null,
                onClick = onClick,
            ),
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
                    .blur(8f),
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
            isButtonEnabled = false,
            isLoading = false,
            cardMoods = listOf(
                CardMood.SAD,
                CardMood.ANGRY,
                CardMood.ROMANTIC,
                CardMood.ANGRY,
                CardMood.DEPRESSED,
                CardMood.SAD_DIZZY
            ),
            onSelectMood = {},
            onClickGetNow = {},
            selectedMood = CardMood.ANGRY
        )
    }
}
