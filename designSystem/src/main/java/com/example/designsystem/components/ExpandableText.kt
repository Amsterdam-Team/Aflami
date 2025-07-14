package com.example.designsystem.components

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.foundation.text.TextAutoSize
import androidx.compose.material3.LocalTextStyle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorProducer
import androidx.compose.ui.layout.SubcomposeLayout
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withLink
import androidx.compose.ui.text.withStyle
import com.example.designsystem.R
import com.example.designsystem.theme.AppTheme

@Composable
fun ExpandableText(
    text: String,
    modifier: Modifier = Modifier,
    minimizedMaxLines: Int = 5,
    style: TextStyle = AppTheme.textStyle.body.small,
    textColor: Color = AppTheme.color.hint,
    showMoreText: String = stringResource(R.string.read_more),
    showMoreStyle: TextStyle = AppTheme.textStyle.label.medium,
    showMoreColor: Color = AppTheme.color.primary,
) {
    var isExpanded by remember { mutableStateOf(false) }
    var isClickable by remember { mutableStateOf(false) }
    var lastCharacterIndex by remember { mutableIntStateOf(0) }

    val textSpanStyle = SpanStyle(
        color = textColor,
        fontSize = style.fontSize,
        fontWeight = style.fontWeight,
        fontStyle = style.fontStyle,
        letterSpacing = style.letterSpacing
    )
    val showMoreSpanStyle = SpanStyle(
        color = showMoreColor,
        fontSize = showMoreStyle.fontSize,
        fontWeight = showMoreStyle.fontWeight,
        fontStyle = showMoreStyle.fontStyle,
        letterSpacing = showMoreStyle.letterSpacing
    )



    val annotatedText = buildAnnotatedString {
        if (isClickable && !isExpanded) {
            val adjustedText = text.substring(0, lastCharacterIndex-4)
                .dropLast(showMoreText.length)
                .dropLastWhile { it.isWhitespace() || it == '.' }
            withStyle(textSpanStyle) {
                append(adjustedText)
                append(" ")
            }

            withLink(
                link = LinkAnnotation.Clickable(
                    tag = showMoreText,
                    linkInteractionListener = { isExpanded = true }
                )
            ) {
                withStyle(showMoreSpanStyle) {
                    append(showMoreText)
                }
            }
        } else {
            withStyle(textSpanStyle) {
                append(text)
            }
        }
    }

    BasicText(
        text = annotatedText,
        modifier = modifier.fillMaxWidth().animateContentSize(),
        maxLines = if (isExpanded) Int.MAX_VALUE else minimizedMaxLines,
        overflow = TextOverflow.Ellipsis,
        onTextLayout = { textLayoutResult ->
            if (!isExpanded && textLayoutResult.hasVisualOverflow) {
                isClickable = true
                lastCharacterIndex = textLayoutResult.getLineEnd(minimizedMaxLines - 1)
            }
        }
    )
}

//@Composable
//fun BasicText(
//    text: AnnotatedString,
//    modifier: Modifier = Modifier,
//    style: TextStyle = TextStyle.Default,
//    onTextLayout: ((TextLayoutResult) -> Unit)? = null,
//    overflow: TextOverflow = TextOverflow.Clip,
//    softWrap: Boolean = true,
//    maxLines: Int = Int.MAX_VALUE,
//    minLines: Int = 1,
//    inlineContent: Map<String, InlineTextContent> = mapOf(),
//    color: ColorProducer? = null,
//    autoSize: TextAutoSize? = null
//)
//
