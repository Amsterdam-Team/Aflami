package com.amsterdam.designsystem.theme.shapes

import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp

class FolderShape(
    private val bottomLeftRadius: Float = 24f,
    private val bottomRightRadius: Float = 24f,
    private val topLeftRadius: Float = 24f,
    private val topRightRadius: Float = 16f,
    private val middleRadius: Float = 16f
) : Shape {

    private val originalHeight = 147f
    private val originalWidth = 160f

    fun getBottomPadding(size: Size): Dp {
        val scaleY = size.height / originalHeight
        val bottomLeftRadiusY = bottomLeftRadius * scaleY
        return bottomLeftRadiusY.dp - 4.dp
    }

    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {



        val scaleX = size.width / originalWidth
        val scaleY = size.height / originalHeight

        val clipPath = if (layoutDirection == LayoutDirection.Rtl) {
            createRtlPath(size = size, scaleX = scaleX, scaleY = scaleY)
        } else {
            createLtrPath(size = size, scaleX = scaleX, scaleY = scaleY)
        }
        return Outline.Generic(clipPath)
    }

    private fun createLtrPath(
        size: Size,
        scaleX: Float,
        scaleY: Float
    ): Path {
        val topOffset = 14f * scaleY
        val middleOffset = size.width * 0.475f

        val bottomLeftDiameterX = (bottomLeftRadius * 2 * scaleX)
        val bottomLeftDiameterY = (bottomLeftRadius * 2 * scaleY)

        val bottomRightDiameterX = (bottomRightRadius * 2 * scaleX)
        val bottomRightDiameterY = (bottomRightRadius * 2 * scaleY)

        val topRightDiameterX = (topRightRadius * 2 * scaleX)
        val topRightDiameterY = (topRightRadius * 2 * scaleY)

        val topLeftDiameterX = (topLeftRadius * 2 * scaleX)
        val topLeftDiameterY = (topLeftRadius * 2 * scaleY)

        val middleDiameterX = (middleRadius * 2 * scaleX)
        val middleDiameterY = (middleRadius * 2 * scaleY)

        return Path().apply {
            // Start Position
            moveTo(middleOffset, topOffset)

            // Top Right Corner
            arcTo(
                rect = Rect(
                    left = size.width - topRightDiameterX,
                    top = topOffset,
                    right = size.width,
                    bottom = topRightDiameterY + topOffset
                ),
                startAngleDegrees = 270f,
                sweepAngleDegrees = 90f,
                forceMoveTo = false
            )

            // Bottom Right Corner
            arcTo(
                rect = Rect(
                    left = size.width - bottomRightDiameterX,
                    top = size.height - bottomRightDiameterY,
                    right = size.width,
                    bottom = size.height
                ),
                startAngleDegrees = 0f,
                sweepAngleDegrees = 90f,
                forceMoveTo = false
            )

            // Bottom Left Corner
            arcTo(
                rect = Rect(
                    left = 0f,
                    top = size.height - bottomLeftDiameterY,
                    right = bottomLeftDiameterX,
                    bottom = size.height
                ),
                startAngleDegrees = 90f,
                sweepAngleDegrees = 90f,
                forceMoveTo = false
            )

            // Top Left Corner
            arcTo(
                rect = Rect(
                    left = 0f,
                    top = 0f,
                    right = topLeftDiameterX,
                    bottom = topLeftDiameterY
                ),
                startAngleDegrees = 180f,
                sweepAngleDegrees = 90f,
                forceMoveTo = false
            )

            // Middle connecting arc
            arcTo(
                rect = Rect(
                    left = middleOffset - middleDiameterX,
                    top = 0f,
                    right = middleOffset,
                    bottom = middleDiameterY
                ),
                startAngleDegrees = 270f,
                sweepAngleDegrees = 90f,
                forceMoveTo = false
            )
        }
    }
    private fun createRtlPath(
        size: Size,
        scaleX: Float,
        scaleY: Float
    ): Path {
        val topOffset = 14f * scaleY
        val middleOffset = size.width - (size.width * 0.475f)

        val bottomRightDiameterX = (bottomLeftRadius * 2 * scaleX)
        val bottomRightDiameterY = (bottomLeftRadius * 2 * scaleY)

        val bottomLeftDiameterX = (bottomRightRadius * 2 * scaleX)
        val bottomLeftDiameterY = (bottomRightRadius * 2 * scaleY)

        val topLeftDiameterX = (topRightRadius * 2 * scaleX)
        val topLeftDiameterY = (topRightRadius * 2 * scaleY)

        val topRightDiameterX = (topLeftRadius * 2 * scaleX)
        val topRightDiameterY = (topLeftRadius * 2 * scaleY)

        val middleDiameterX = (middleRadius * 2 * scaleX)
        val middleDiameterY = (middleRadius * 2 * scaleY)

        return Path().apply {
            // Start Position
            moveTo(middleOffset, topOffset)

            // Middle connecting arc
            arcTo(
                rect = Rect(
                    left = middleOffset,
                    top = 0f,
                    right = middleOffset + middleDiameterX,
                    bottom = middleDiameterY
                ),
                startAngleDegrees = 180f,
                sweepAngleDegrees = 90f,
                forceMoveTo = false
            )

            // Top Right Corner
            arcTo(
                rect = Rect(
                    left = size.width - topRightDiameterX,
                    top = 0f,
                    right = size.width,
                    bottom = topRightDiameterY
                ),
                startAngleDegrees = 270f,
                sweepAngleDegrees = 90f,
                forceMoveTo = false
            )

            // Bottom Right Corner
            arcTo(
                rect = Rect(
                    left = size.width - bottomRightDiameterX,
                    top = size.height - bottomRightDiameterY,
                    right = size.width,
                    bottom = size.height
                ),
                startAngleDegrees = 0f,
                sweepAngleDegrees = 90f,
                forceMoveTo = false
            )

            // Bottom Left Corner
            arcTo(
                rect = Rect(
                    left = 0f,
                    top = size.height - bottomLeftDiameterY,
                    right = bottomLeftDiameterX,
                    bottom = size.height
                ),
                startAngleDegrees = 90f,
                sweepAngleDegrees = 90f,
                forceMoveTo = false
            )

            // Top Left Corner
            arcTo(
                rect = Rect(
                    left = 0f,
                    top = topOffset,
                    right = topLeftDiameterX,
                    bottom = topLeftDiameterY + topOffset
                ),
                startAngleDegrees = 180f,
                sweepAngleDegrees = 90f,
                forceMoveTo = false
            )
        }
    }
}