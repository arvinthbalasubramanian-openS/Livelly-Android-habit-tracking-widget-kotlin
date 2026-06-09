package com.mobileapp.livelly.ui.component

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun HabitHeroCard(
    streak: Int,
    target: Int
) {

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF18122B)
        )
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalArrangement =
                Arrangement.SpaceBetween
        ) {

            Column(
                modifier = Modifier.weight(1f)
            ) {

                Text(
                    text = "You're on a roll 🔥",
                    color = Color.White,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(
                    modifier = Modifier.height(20.dp)
                )

                Text(
                    text = streak.toString(),
                    color = Color.White,
                    fontSize = 64.sp,
                    fontWeight = FontWeight.ExtraBold
                )

                Text(
                    text = "Days in a row",
                    color = Color(0xFFA78BFA)
                )

                Spacer(
                    modifier = Modifier.height(16.dp)
                )

                LinearProgressIndicator(
                progress = {
                    (streak.toFloat() / target)
                                            .coerceIn(0f, 1f)
                },
                modifier = Modifier.fillMaxWidth(),
                color = Color(0xFF8B5CF6),
                trackColor = ProgressIndicatorDefaults.linearTrackColor,
                strokeCap = ProgressIndicatorDefaults.LinearStrokeCap,
                )
            }

            Spacer(
                modifier = Modifier.width(16.dp)
            )

            TreeStageWidget(
                streak = streak
            )
        }
    }
}