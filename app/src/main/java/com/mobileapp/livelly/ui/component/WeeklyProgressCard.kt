package com.mobileapp.livelly.ui.component

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.mobileapp.livelly.data.Habit
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

@Composable
fun WeeklyProgressCard(
    habit: Habit?
) {

    val today = LocalDate.now()

    val weekDays =
        (6 downTo 0).map {

            today.minusDays(
                it.toLong()
            )
        }

    val completedDays =

        habit?.completionDates
            .orEmpty()
            .map {

                Instant
                    .ofEpochMilli(it)
                    .atZone(
                        ZoneId.systemDefault()
                    )
                    .toLocalDate()
            }
            .toSet()

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
            modifier =
                Modifier.padding(20.dp)
        ) {

            Text(
                "This Week",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(
                Modifier.height(16.dp)
            )

            Row(
                modifier =
                    Modifier.fillMaxWidth(),

                horizontalArrangement =
                    Arrangement.SpaceBetween
            ) {

                weekDays.forEach { day ->

                    val completed =
                        completedDays.contains(day)

                    val isToday = day == today

                    val fillColor by animateColorAsState(
                        targetValue = if (completed)
                            MaterialTheme.colorScheme.primary
                        else
                            MaterialTheme.colorScheme.surfaceVariant,
                        animationSpec = tween(350),
                        label = "weekly day fill"
                    )

                    val scale by animateFloatAsState(
                        targetValue = if (completed) 1f else 0.92f,
                        animationSpec = tween(350),
                        label = "weekly day scale"
                    )

                    Column(
                        horizontalAlignment =
                            Alignment.CenterHorizontally
                    ) {

                        Text(
                            day.dayOfWeek.name.take(1),
                            style = MaterialTheme.typography.bodySmall,
                            color = if (isToday)
                                MaterialTheme.colorScheme.primary
                            else
                                MaterialTheme.colorScheme.onSurface.copy(alpha = 0.56f),
                            fontWeight = if (isToday) FontWeight.Bold else FontWeight.Normal
                        )

                        Spacer(
                            Modifier.height(8.dp)
                        )

                        Box(
                            modifier =
                                Modifier
                                    .size(36.dp)
                                    .scale(scale)
                                    .background(
                                        fillColor,
                                        CircleShape
                                    )
                                    .border(
                                        width = if (isToday) 2.dp else 0.dp,
                                        color = if (isToday)
                                            MaterialTheme.colorScheme.primary.copy(alpha = 0.7f)
                                        else
                                            MaterialTheme.colorScheme.surfaceVariant,
                                        shape = CircleShape
                                    ),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = day.dayOfMonth.toString(),
                                style = MaterialTheme.typography.bodySmall,
                                color = if (completed)
                                    MaterialTheme.colorScheme.onPrimary
                                else
                                    MaterialTheme.colorScheme.onSurface.copy(alpha = 0.52f),
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }
            }
        }
    }
}
