package com.mobileapp.livelly.ui.component

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun HabitStatsCard(
    streak: Int,
    target: Int,
    completionDates: List<Long>? = emptyList()
) {
    val safeTarget = target.coerceAtLeast(1)
    val totalCompletions = completionDates.orEmpty().size
    val targetProgress = (streak.toFloat() / safeTarget * 100).toInt().coerceIn(0, 100)
    val completionRate = (totalCompletions.toFloat() / safeTarget * 100).toInt().coerceIn(0, 100)

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.86f)
        ),
        border = BorderStroke(
            1.dp,
            MaterialTheme.colorScheme.onSurface.copy(alpha = 0.05f)
        )
    ) {

        Column(
            modifier = Modifier.padding(vertical = 8.dp)
        ) {
            Text(
                text = "Statistics",
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(Modifier.height(2.dp))

            StatRow(
                "Current Streak",
                "$streak days"
            )

            StatRow(
                "Completion Rate",
                "$completionRate%"
            )

            StatRow(
                "Total Completions",
                totalCompletions.toString()
            )

            StatRow(
                "Target Progress",
                "$targetProgress%"
            )
        }
    }
}

@Composable
fun StatRow(
    title: String,
    value: String
) {

    Row(

        modifier =
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),

        horizontalArrangement =
            Arrangement.SpaceBetween
    ) {

        Text(
            text = title,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.68f)
        )

        Text(
            value,
            color =
                MaterialTheme
                    .colorScheme
                    .primary,
            fontWeight = FontWeight.SemiBold
        )
    }
}
