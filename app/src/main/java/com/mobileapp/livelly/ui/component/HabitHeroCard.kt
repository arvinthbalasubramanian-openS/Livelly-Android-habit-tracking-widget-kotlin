package com.mobileapp.livelly.ui.component

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun HabitHeroCard(
    streak: Int,
    target: Int
) {
    val safeTarget = target.coerceAtLeast(1)
    val progress = (streak.toFloat() / safeTarget).coerceIn(0f, 1f)
    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = tween(800),
        label = "habit target progress"
    )
    val animatedStreak by animateIntAsState(
        targetValue = streak,
        animationSpec = tween(500),
        label = "habit streak"
    )

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(26.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primary
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.primary,
                            MaterialTheme.colorScheme.primary.copy(alpha = 0.82f)
                        )
                    )
                )
                .padding(24.dp)
        ) {
            Text(
                text = "Current streak",
                color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.72f),
                style = MaterialTheme.typography.bodySmall
            )

            Spacer(Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = animatedStreak.toString(),
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontSize = 64.sp,
                    lineHeight = 64.sp,
                    fontWeight = FontWeight.ExtraBold
                )

                Column {
                    Text(
                        text = "days",
                        color = MaterialTheme.colorScheme.onPrimary,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )

                    Text(
                        text = "of $safeTarget",
                        color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.64f),
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }

            Spacer(Modifier.height(22.dp))

            LinearProgressIndicator(
                progress = {
                    animatedProgress
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(10.dp)
                    .clip(RoundedCornerShape(50)),
                color = MaterialTheme.colorScheme.onPrimary,
                trackColor = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.22f),
                strokeCap = ProgressIndicatorDefaults.LinearStrokeCap
            )

            Spacer(Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "${(animatedProgress * 100).toInt()}% complete",
                    color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.78f),
                    style = MaterialTheme.typography.bodySmall
                )

                Text(
                    text = "${(safeTarget - streak).coerceAtLeast(0)} days left",
                    color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.78f),
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}
