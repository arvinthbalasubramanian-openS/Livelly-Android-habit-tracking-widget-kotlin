package com.mobileapp.livelly.ui.component

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.sp

@Composable
fun TreeStageWidget(
    streak: Int
) {

    val stage = when {

        streak < 3 -> "🌱"

        streak < 7 -> "🌿"

        streak < 14 -> "🌳"

        streak < 30 -> "🌲"

        else -> "🌸"
    }

    Text(
        text = stage,
        fontSize = 90.sp
    )
}