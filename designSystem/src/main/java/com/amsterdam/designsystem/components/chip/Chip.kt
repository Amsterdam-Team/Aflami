package com.amsterdam.designsystem.components.chip

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.amsterdam.designsystem.R
import com.amsterdam.designsystem.theme.AflamiTheme
import com.amsterdam.designsystem.theme.AppTheme
import com.amsterdam.designsystem.utils.ThemeAndLocalePreviews

@Composable
fun Chip(
    icon: Painter,
    label: String,
    isSelected: Boolean,
    modifier: Modifier = Modifier,
    colors: ChipColors = ChipDefaults.chipColors(),
    onClick: () -> Unit = {},
) {
    val backgroundColor by animateColorAsState(
        targetValue = if (isSelected) colors.backgroundSelectedColor else colors.backgroundUnselectedColor,
        animationSpec = tween(durationMillis = 500),
        label = "BackgroundColorAnimation",
    )

    val iconColor = if (isSelected) colors.iconSelectedColor else colors.iconUnselectedColor
    val labelColor = if (isSelected) colors.labelSelectedColor else colors.labelUnselectedColor
    val borderColor = if (isSelected) colors.borderSelectedColor else colors.borderUnselectedColor

    val formattedLabel = remember(label) {
        val words = label.split(" ")
        if (words.size >= 2) {
            "${words.first()}\n${words.drop(1).joinToString(" ")}"
        } else {
            label
        }
    }

    val containsLongWord = remember(label) {
        label.split(" ").any { it.length > 7 }
    }

    val baseTextStyle = AppTheme.textStyle.label.small

    val currentTextStyle: TextStyle = remember(containsLongWord, baseTextStyle) {
        if (containsLongWord) {
            baseTextStyle.copy(fontSize = (9.25).sp)
        } else {
            baseTextStyle
        }
    }


    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .heightIn(min = 96.dp)
    ) {
        Box(
            modifier =
                Modifier
                    .size(56.dp)
                    .background(
                        backgroundColor,
                        RoundedCornerShape(16.dp),
                    )
                    .border(1.dp, borderColor, RoundedCornerShape(16.dp))
                    .clip(RoundedCornerShape(16.dp))
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = ripple(color = iconColor, bounded = true),
                        onClick = onClick,
                    ),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                painter = icon,
                contentDescription = label,
                tint = iconColor,
                modifier = Modifier.size(24.dp),
            )
        }
        Text(
            text = formattedLabel,
            color = labelColor,
            style = currentTextStyle,
            textAlign = TextAlign.Center,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.widthIn(max = 90.dp)
        )
    }
}

@ThemeAndLocalePreviews
@Composable
private fun ChipPreview() {
    AflamiTheme {
        Row(horizontalArrangement = Arrangement.spacedBy(18.dp)) {
            Chip(
                icon = painterResource(R.drawable.ic_menu_square),
                label = "Science Fiction",
                isSelected = true,
            )
            Chip(
                icon = painterResource(R.drawable.ic_menu_square),
                label = "Action",
                isSelected = false,
            )
            Chip(
                icon = painterResource(R.drawable.ic_menu_square),
                label = "Horror Thriller",
                isSelected = false,
            )
            Chip(
                icon = painterResource(R.drawable.ic_menu_square),
                label = "FantasyAdventureMovieGenre",
                isSelected = false,
            )
            Chip(
                icon = painterResource(R.drawable.ic_menu_square),
                label = "Short",
                isSelected = true,
            )
        }
    }
}