package com.example.krishimitra.presentation.canvas

import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection


class WaveShape : Shape {

    override fun createOutline(
        size: androidx.compose.ui.geometry.Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        val path = Path().apply {
            reset()
            moveTo(0f, 0f)
            lineTo(0f, size.height * 0.75f)
            this.quadraticTo(
                (size.width / 2).toFloat(), size.height.toFloat(),
                size.width.toFloat(), size.height * 0.75f
            )
            lineTo(size.width.toFloat(), 0f)
            close()
        }
        return Outline.Generic(path)
    }
}