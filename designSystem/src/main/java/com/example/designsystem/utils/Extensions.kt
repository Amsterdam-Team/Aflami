package com.example.designsystem.utils

import android.annotation.SuppressLint
import android.graphics.BlurMaskFilter
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.drawOutline
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp

@SuppressLint("SuspiciousModifierThen")
fun Modifier.dashedBorder(
    color: Color,
    strokeWidth: Dp = 1.dp,
    phase: Float = 0f,
    dashLength: Dp = 6.dp,
    gapLength: Dp = 6.dp,
    cornerRadius: Dp = 8.dp,
) = this.then(
    drawBehind {
        val path =
            Path().apply {
                addRoundRect(
                    RoundRect(
                        rect =
                            Rect(
                                left = 0f,
                                top = 0f,
                                right = size.width,
                                bottom = size.height,
                            ),
                        cornerRadius = CornerRadius(cornerRadius.toPx()),
                    ),
                )
            }

        val dashPath =
            PathEffect.dashPathEffect(
                intervals = floatArrayOf(dashLength.toPx(), gapLength.toPx()),
                phase = phase,
            )

        drawPath(
            path = path,
            color = color,
            style =
                Stroke(
                    width = strokeWidth.toPx(),
                    pathEffect = dashPath,
                ),
        )
    },
)

fun Modifier.dropShadow(
    shape: Shape,
    color: Color,
    blur: Dp = 4.dp,
    offsetY: Dp = 4.dp,
    offsetX: Dp = 0.dp,
    spread: Dp = 0.dp,
) = this.drawBehind {
    val shadowSize = Size(size.width + spread.toPx(), size.height + spread.toPx())
    val shadowOutline = shape.createOutline(shadowSize, layoutDirection, this)

    val paint = Paint()

    paint.color = color

    if (blur.toPx() > 0) {
        paint.asFrameworkPaint().apply {
            maskFilter = BlurMaskFilter(blur.toPx(), BlurMaskFilter.Blur.NORMAL)
        }
    }

    drawIntoCanvas { canvas ->
        canvas.save()
        canvas.translate(offsetX.toPx(), offsetY.toPx())
        canvas.drawOutline(shadowOutline, paint)
        canvas.restore()
    }
}

fun Modifier.mirroredContent(layoutDirection: LayoutDirection): Modifier {
    val isRtl = layoutDirection == LayoutDirection.Rtl
    return this.scale(scaleX = if (isRtl) -1f else 1f, scaleY = 1f)
}

fun Modifier.autoMirroredContent(): Modifier = composed {
    val isRtl = LocalLayoutDirection.current == LayoutDirection.Rtl
    this.scale(scaleX = if (isRtl) -1f else 1f, scaleY = 1f)
}