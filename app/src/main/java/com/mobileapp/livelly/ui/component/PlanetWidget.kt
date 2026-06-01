package com.mobileapp.livelly.ui.component

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun PlanetWidget(
    progress: Float
) {

    val infiniteTransition =
        rememberInfiniteTransition(label = "")

    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 18000,
                easing = LinearEasing
            )
        ),
        label = ""
    )

    val pulse by infiniteTransition.animateFloat(
        initialValue = 0.95f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(3000),
            repeatMode = RepeatMode.Reverse
        ),
        label = ""
    )

    val primaryColor =
        MaterialTheme.colorScheme.primary

    Canvas(
        modifier = Modifier.size(280.dp)
    ) {

        val center = center

        val stage = when {

            progress < 0.2f -> 1

            progress < 0.4f -> 2

            progress < 0.6f -> 3

            progress < 0.8f -> 4

            else -> 5
        }

        val planetColor = when {

            progress < 0.2f ->
                Color(0xFF4B5563)

            progress < 0.4f ->
                Color(0xFF2563EB)

            progress < 0.6f ->
                Color(0xFF7C3AED)

            progress < 0.8f ->
                Color(0xFF9333EA)

            else ->
                Color(0xFFFFB84D)
        }

        val radius =
            size.minDimension * 0.22f

        // nebula glow

        drawCircle(
            color = Color.White.copy(
                alpha = progress * 0.3f
            ),
            radius = radius * progress,
            center = center
        )

        drawCircle(
            color = planetColor.copy(alpha = 0.25f),
            radius = radius * 1.8f * pulse,
            center = center
        )

        // main planet

        drawCircle(
            color = planetColor,
            radius = radius,
            center = center
        )

        // continents

        drawCircle(
            color = Color.White.copy(alpha = 0.15f),
            radius = radius * 0.32f,
            center = Offset(
                center.x - radius * 0.28f,
                center.y - radius * 0.18f
            )
        )

        drawCircle(
            color = Color.White.copy(alpha = 0.12f),
            radius = radius * 0.22f,
            center = Offset(
                center.x + radius * 0.18f,
                center.y + radius * 0.10f
            )
        )

        drawCircle(
            color = Color.White.copy(alpha = 0.08f),
            radius = radius * 0.15f,
            center = Offset(
                center.x + radius * 0.05f,
                center.y - radius * 0.25f
            )
        )

        // atmosphere

        if (stage >= 2) {

            drawCircle(
                color = Color(0x5563B3FF),
                radius = radius * 1.15f,
                center = center,
                style = Stroke(5f)
            )
        }

        // saturn rings

        if (stage >= 3) {

            drawCircle(
                color =
                    primaryColor,
                radius = radius * 1.45f,
                center = center,
                style = Stroke(
                    width = 8f
                )
            )
        }

        val orbitRadius =
            radius * 2.3f

        val angle =
            Math.toRadians(rotation.toDouble())

        val satelliteX =
            center.x +
                    orbitRadius *
                    cos(angle).toFloat()

        val satelliteY =
            center.y +
                    orbitRadius *
                    sin(angle).toFloat()

        drawCircle(
            color = Color.White,
            radius = 6f,
            center = Offset(
                satelliteX,
                satelliteY
            )
        )

        // orbit particles

        for (i in 0..5) {

            val particleAngle =
                Math.toRadians(
                    (rotation + i * 60).toDouble()
                )

            drawCircle(
                color = Color.White.copy(
                    alpha = 0.35f
                ),
                radius = 2.5f,
                center = Offset(
                    center.x +
                            radius * 2.8f *
                            cos(
                                particleAngle
                            ).toFloat(),

                    center.y +
                            radius * 2.8f *
                            sin(
                                particleAngle
                            ).toFloat()
                )
            )
        }

        // stars

        if (stage >= 4) {

            drawCircle(
                color = Color.White,
                radius = 3f,
                center = Offset(
                    center.x - radius * 2.7f,
                    center.y - radius * 2.0f
                )
            )

            drawCircle(
                color = Color.White,
                radius = 2.5f,
                center = Offset(
                    center.x + radius * 2.8f,
                    center.y - radius * 1.8f
                )
            )

            drawCircle(
                color = Color.White,
                radius = 2.5f,
                center = Offset(
                    center.x - radius * 2.2f,
                    center.y + radius * 2.4f
                )
            )
        }

        // cosmic world

        if (stage == 5) {

            drawCircle(
                color = Color(0x22FFFFFF),
                radius = radius * 2.4f,
                center = center,
                style = Stroke(3f)
            )

            drawCircle(
                color = Color(0x11FFFFFF),
                radius = radius * 3f,
                center = center,
                style = Stroke(2f)
            )
        }
    }
}