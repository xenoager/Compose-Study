package com.example.study.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Shapes
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp

val Shapes = Shapes(
    small = RoundedCornerShape(4.dp),
    medium = RoundedCornerShape(4.dp),
    large = RoundedCornerShape(0.dp)
)

class TriangleArrowShapeForRight(private val offset: Int): Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        val trianglePath = Path().apply {
            this.moveTo(0f, size.height - offset)
            this.lineTo(0f, size.height)
            this.lineTo(0f + offset, size.height)
        }

        return Outline.Generic(trianglePath)
    }
}

class TriangleArrowShapeForLeft(private val offset: Int): Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        val trianglePath = Path().apply {
            this.moveTo(0f + offset, size.height - offset)
            this.lineTo(0f, size.height)
            this.lineTo(0f + offset, size.height)
        }

        return Outline.Generic(trianglePath)
    }
}