package com.amsterdam.designsystem.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withLink
import androidx.compose.ui.text.withStyle
import com.amsterdam.designsystem.theme.AflamiTheme
import com.amsterdam.designsystem.theme.AppTheme
import com.amsterdam.designsystem.utils.ThemeAndLocalePreviews

@Composable
fun TextWithClickablePart(
    nonClickableText: String,
    clickableText: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    nonClickableTextColor: Color = AppTheme.color.hint,
    nonClickableTextStyle: TextStyle = AppTheme.textStyle.label.medium,
    clickableTextColor: Color = AppTheme.color.primary,
    clickableTextStyle: TextStyle = AppTheme.textStyle.label.medium,
) {


    val nonClickableTextSpanStyle = SpanStyle(
        color = nonClickableTextColor,
        fontSize = nonClickableTextStyle.fontSize,
        fontWeight = nonClickableTextStyle.fontWeight,
        fontStyle = nonClickableTextStyle.fontStyle,
        letterSpacing = nonClickableTextStyle.letterSpacing
    )

    val clickableTextSpanStyle = SpanStyle(
        color = clickableTextColor,
        fontSize = clickableTextStyle.fontSize,
        fontWeight = clickableTextStyle.fontWeight,
        fontStyle = clickableTextStyle.fontStyle,
        letterSpacing = clickableTextStyle.letterSpacing
    )



    val annotatedText = buildAnnotatedString {
            withStyle(nonClickableTextSpanStyle) {
                append(nonClickableText)
                append(" ")
            }

            withLink(
                link = LinkAnnotation.Clickable(
                    tag = clickableText,
                    linkInteractionListener = { onClick() }
                )
            ) {
                withStyle(clickableTextSpanStyle) {
                    append(clickableText)
                }
            }

    }

    Text(
        text = annotatedText,
        modifier = modifier.fillMaxWidth(),
        textAlign = TextAlign.Center

    )
}

@ThemeAndLocalePreviews
@Composable
private fun ExpandableTextPreview() {
    AflamiTheme {
        TextWithClickablePart(
            nonClickableText = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Quisque eget odio ac lectus vestibulum faucibus eget in metus. In pellentesque faucibus vestibulum. Nulla at nulla justo, eget luctus tortor.",
            clickableText = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Quisque eget odio ac lectus vestibulum faucibus eget in metus. In pellentesque faucibus vestibulum. Nulla at nulla justo, eget luctus tortor.",
            onClick = {}
        )
    }
}