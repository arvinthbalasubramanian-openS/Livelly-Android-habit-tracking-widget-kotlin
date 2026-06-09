package com.mobileapp.livelly.ui.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun HabitStatsCard(
    streak: Int,
    target: Int
) {

    Card {

        Column {

            StatRow(
                "Current Streak",
                "$streak days"
            )

            StatRow(
                "Target",
                "$target days"
            )

            StatRow(
                "Completion Rate",
                "${(streak.toFloat() / target * 100).toInt()}%"
            )

            StatRow(
                "Total Completions",
                streak.toString()
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
                .padding(16.dp),

        horizontalArrangement =
            Arrangement.SpaceBetween
    ) {

        Text(title)

        Text(
            value,
            color =
                MaterialTheme
                    .colorScheme
                    .primary
        )
    }
}