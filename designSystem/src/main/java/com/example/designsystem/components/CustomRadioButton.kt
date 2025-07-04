package com.example.designsystem.components

import android.annotation.SuppressLint
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.example.designsystem.theme.AflamiTheme
import com.example.designsystem.theme.AppTheme
import com.example.designsystem.utils.ThemeAndLocalePreviews

@SuppressLint("UnrememberedMutableState")
@Composable
fun CustomRadioButton(
    modifier: Modifier = Modifier,
    isEnable: Boolean,
    onClick: () -> Unit = {}
) {
    val color = if (isEnable) AppTheme.color.primary else AppTheme.color.disable
    Row(
        modifier = modifier
            .size(18.dp)
            .clip(CircleShape)
            .border(
                width = 6.dp, color = color, shape = CircleShape
            )
            .clickable(onClick = onClick)
    )
    {
    }
}
@ThemeAndLocalePreviews
@Composable
private fun CustomRadioButtonPreview() {
    AflamiTheme {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CustomRadioButton(modifier = Modifier.padding(bottom = 24.dp)
                , isEnable = true)
            CustomRadioButton(isEnable = false)
        }
    }
}
