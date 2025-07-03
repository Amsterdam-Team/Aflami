package com.example.designsystem.components

import android.annotation.SuppressLint
import androidx.annotation.DrawableRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.designsystem.R
import com.example.designsystem.theme.AflamiTheme
import com.example.designsystem.theme.AppTheme
import com.example.designsystem.utils.SpeechBubbleShape
import com.example.designsystem.utils.ThemeAndLocalePreviews

@Composable
fun TextField(
    text: String,
    modifier: Modifier = Modifier,
    style: TextStyle = AppTheme.textStyle.label.medium,
    hintText: String = "",
    isEnabled: Boolean = true,
    isObscured: Boolean = false,
    isError: Boolean = false,
    errorMessage: String = "",
    maxCharacters: Int = Int.MAX_VALUE,
    @DrawableRes leadingIcon: Int? = null,
    @DrawableRes trailingIcon: Int? = null,
    onTrailingClick: (() -> Unit)? = null,
    borderColor: Color = AppTheme.color.stroke,
    borderErrorColor: Color = AppTheme.color.redAccent,
    borderFocusedColor: Color = AppTheme.color.primary,
    onValueChange: (String) -> Unit = {}
) {
    var isFocused by remember { mutableStateOf(false) }
    val canShowMaxCharacters = maxCharacters - text.length < 5

    val currentBorderColor by animateColorAsState(
        if (isError) borderErrorColor
        else if (isFocused) borderFocusedColor
        else borderColor
    )

    Column {
        AnimatedErrorBox(isError, errorMessage, style)
        Row(
            modifier = Modifier
                .border(
                    width = 1.dp,
                    color = currentBorderColor,
                    shape = RoundedCornerShape(16.dp)
                )
                .clip(shape = RoundedCornerShape(16.dp))
                .clipToBounds()
                .background(AppTheme.color.surfaceHigh, shape = RoundedCornerShape(16.dp))
                .defaultMinSize(minHeight = 56.dp)
                .then(
                    if (leadingIcon == null) Modifier.padding(start = 4.dp) else Modifier
                )
                .then(
                    if (trailingIcon == null) Modifier.padding(end = 4.dp) else Modifier
                ),
            verticalAlignment = Alignment.Top
        ) {
            if (leadingIcon != null) {
                val imageColor by animateColorAsState(
                    targetValue = if (text.isEmpty()) AppTheme.color.hint else AppTheme.color.body
                )
                LeadingIcon(leadingIcon, imageColor)
                VerticalDivider()
            }
            BasicTextField(
                value = text,
                onValueChange = {
                    if (it.length <= maxCharacters)
                        onValueChange(it)
                    else if (it.length > text.length + 1)
                        onValueChange(it.substring(0, maxCharacters))
                },
                maxLines = 1,
                enabled = isEnabled,
                modifier = modifier
                    .padding(horizontal = 12.dp)
                    .weight(1f)
                    .defaultMinSize(minHeight = 56.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .onFocusChanged { focusState -> isFocused = focusState.isFocused },
                textStyle = style.copy(color = AppTheme.color.title),
                singleLine = true,
                visualTransformation = if (isObscured) PasswordVisualTransformation() else VisualTransformation.None,
                decorationBox = { innerTextField ->
                    InnerTextFieldWithHint(innerTextField, text, hintText, style)
                }
            )
            if (trailingIcon != null) {
                val imageColor by animateColorAsState(
                    targetValue = if (text.isEmpty()) AppTheme.color.hint else AppTheme.color.title
                )
                VerticalDivider()
                TrailingIcon(trailingIcon, imageColor, onTrailingClick)
            }
        }
        AnimatedMaxCharacters(
            canShowMaxCharacters,
            "${text.length}/$maxCharacters",
            style,
        )
    }
}

@Composable
private fun RowScope.InnerTextFieldWithHint(
    innerTextField: @Composable (() -> Unit),
    text: String,
    hintText: String,
    style: TextStyle
) {
    Box(
        modifier = Modifier
            .padding(vertical = 5.dp)
            .padding(top = (if (LocalLayoutDirection.current == LayoutDirection.Rtl) 0 else 3).dp),
        contentAlignment = Alignment.CenterStart,
    ) {
        innerTextField()
        if (text.isEmpty()) {
            Text(
                text = hintText,
                style = style,
                fontSize = 16.sp,
                lineHeight = 20.sp,
                color = AppTheme.color.hint,
                modifier = Modifier
            )
        }
    }
}

@Composable
private fun VerticalDivider() {
    Box(
        Modifier
            .padding(vertical = 13.dp)
            .size(1.dp, 30.dp)
            .background(AppTheme.color.stroke)
    )
}

@Composable
private fun LeadingIcon(leadingIcon: Int, imageColor: Color) {
    Image(
        imageVector = ImageVector.vectorResource(id = leadingIcon),
        contentDescription = null,
        colorFilter = ColorFilter.tint(imageColor),
        contentScale = ContentScale.Fit,
        modifier = Modifier
            .padding(vertical = 16.dp)
            .padding(start = 16.dp, end = 12.dp)
            .size(24.dp)
    )
}

@SuppressLint("UnrememberedMutableInteractionSource")
@Composable
private fun TrailingIcon(leadingIcon: Int, imageColor: Color, onClick: (() -> Unit)? = null) {
    Crossfade(targetState = leadingIcon) { state ->
        Image(
            painter = painterResource(id = state),
            contentDescription = null,
            colorFilter = ColorFilter.tint(imageColor),
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .then(
                    if (onClick != null)
                        Modifier.clickable(
                            interactionSource = MutableInteractionSource(),
                            indication = ripple(color = AppTheme.color.hint),
                            onClick = onClick
                        )
                    else
                        Modifier
                )
                .padding(vertical = 16.dp)
                .padding(start = 12.dp, end = 16.dp)
                .size(24.dp)
        )
    }
}

@Composable
private fun ColumnScope.AnimatedErrorBox(
    isError: Boolean,
    message: String,
    style: TextStyle,
) {
    AnimatedVisibility(visible = isError) {
        Box(
            modifier = Modifier
                .padding(bottom = 4.dp)
                .wrapContentSize()
                .background(
                    color = AppTheme.color.redAccent,
                    shape = SpeechBubbleShape(
                        cornerRadius = 8.dp,
                        tailWidth = 8.dp,
                        tailHeight = 4.dp,
                        tailOffsetDp = 8.dp,
                        isRtl = LocalLayoutDirection.current == LayoutDirection.Rtl
                    )
                )
                .padding(bottom = 4.dp)
        ) {
            Text(
                text = message,
                style = style,
                color = AppTheme.color.onPrimary,
                modifier = Modifier
                    .padding(horizontal = 12.dp)
                    .padding(vertical = 6.dp)
                    .animateContentSize()
            )
        }
    }
}

@Composable
private fun ColumnScope.AnimatedMaxCharacters(
    canShowMaxCharacters: Boolean,
    message: String,
    style: TextStyle,
) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(top = 4.dp),
        horizontalArrangement = Arrangement.End
    ) {
        AnimatedVisibility(visible = canShowMaxCharacters) {
            Text(
                text = message,
                style = style,
                fontSize = 12.sp,
                color = AppTheme.color.title,
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .padding(top = 4.dp)
                    .animateContentSize()
            )
        }
    }
}

@ThemeAndLocalePreviews
@Composable
private fun CustomTextFieldPreview() {
    AflamiTheme {
        Column(
            Modifier
                .background(AppTheme.color.surface)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            TextField(
                "",
                hintText = stringResource(R.string.user_name_hint),
                leadingIcon = R.drawable.ic_user,
            )
            TextField(
                "",
                hintText = stringResource(R.string.password_hint),
                leadingIcon = R.drawable.ic_user,
                trailingIcon = R.drawable.ic_password_hide,
                isError = true,
                errorMessage = stringResource(R.string.general_error_message)
            )
            TextField(
                stringResource(R.string.action_adventure),
                hintText = stringResource(R.string.hint),
                trailingIcon = R.drawable.ic_filter_vertical,
                maxCharacters = 20
            )
            TextField(
                "",
                hintText = stringResource(R.string.country_name_hint),
            )
        }
    }
}