package no.uio.ifi.in2000.andrklae.andrklae.team13.ui.home

import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection


class SpeechBubblen(private val cornerRadius: Float = 5f) : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        return Outline.Generic(Path().apply {
            val width = size.width
            val height = size.height
            val triangleHeight = height / 4
            val triangleWidth = width / 10

            val roundRect = RoundRect(
                left = 0f,
                top = triangleHeight,
                right = width,
                bottom = height,
                radiusX = cornerRadius,
                radiusY = cornerRadius
            )

            addRoundRect(roundRect)

            // Draw the triangle
            moveTo(width / 2 - triangleWidth / 2, triangleHeight)
            lineTo(width / 2, 0f)
            lineTo(width / 2 + triangleWidth / 2, triangleHeight)

            close()
        })
    }
}
