package com.example.designsystem.theme.shapes

import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection

class SpeechBubbleShape(
    private val cornerRadius: Dp,
    private val tailWidth: Dp,
    private val tailHeight: Dp,
    private val isRtl: Boolean = false,
    private val tailOffsetRatio: Float? = null,
    private val tailOffsetDp: Dp? = null,
) : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        val cornerRadiusPx = with(density) { cornerRadius.toPx() }
        val tailWidthPx = with(density) { tailWidth.toPx() }
        val tailHeightPx = with(density) { tailHeight.toPx() }

        return Outline.Generic(Path().apply {
            val rectWidth = size.width
            val rectHeight = size.height - tailHeightPx

            addRoundRect(
                roundRect = RoundRect(
                    rect = Rect(0f, 0f, rectWidth, rectHeight),
                    cornerRadius = CornerRadius(cornerRadiusPx, cornerRadiusPx)
                )
            )
            if (tailOffsetRatio != null) {
                val tailBaseLeft = rectWidth * tailOffsetRatio - tailWidthPx / 2
                val tailBaseRight = rectWidth * tailOffsetRatio + tailWidthPx / 2
                val tailBaseStart = if (isRtl) tailBaseRight else tailBaseLeft
                val tailBaseEnd = if (isRtl) tailBaseLeft else tailBaseRight
                val tailTipX = rectWidth * tailOffsetRatio
                val tailTipY = rectHeight + tailHeightPx
                moveTo(tailBaseStart, rectHeight)
                lineTo(tailTipX, tailTipY)
                lineTo(tailBaseEnd, rectHeight)
            } else if (tailOffsetDp != null) {
                val tailOffsetPx = with(density) { tailOffsetDp.toPx() }
                val tailBaseStart = if (isRtl) rectWidth - tailOffsetPx - tailWidthPx else tailOffsetPx
                val tailBaseEnd = if (isRtl) rectWidth - tailOffsetPx else tailOffsetPx + tailWidthPx
                val tailTipX = tailBaseStart + tailWidthPx / 2
                val tailTipY = rectHeight + tailHeightPx
                moveTo(tailBaseStart, rectHeight)
                lineTo(tailTipX, tailTipY)
                lineTo(tailBaseEnd, rectHeight)
            }
            close()
        })
    }
}