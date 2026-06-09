package com.mobileapp.livelly.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
            ?.map {

                Instant
                    .ofEpochMilli(it)
                    .atZone(
                        ZoneId.systemDefault()
                    )
                    .toLocalDate()
            }
            ?: emptyList()

    Card(
        modifier =
            Modifier.fillMaxWidth()
    ) {

        Column(
            modifier =
                Modifier.padding(20.dp)
        ) {

            Text(
                "This Week",
                style =
                    MaterialTheme
                        .typography
                        .titleMedium
            )

            Spacer(
                Modifier.height(16.dp)
            )

            Row(
                modifier =
                    Modifier.fillMaxWidth(),

                horizontalArrangement =
                    Arrangement.SpaceEvenly
            ) {

                weekDays.forEach { day ->

                    val completed =
                        completedDays.contains(day)

                    Column(
                        horizontalAlignment =
                            Alignment.CenterHorizontally
                    ) {

                        Text(
                            day.dayOfWeek
                                .name
                                .take(1)
                        )

                        Spacer(
                            Modifier.height(8.dp)
                        )

                        Box(
                            modifier =
                                Modifier
                                    .size(36.dp)
                                    .background(
                                        if (completed)
                                            MaterialTheme
                                                .colorScheme
                                                .primary
                                        else
                                            MaterialTheme
                                                .colorScheme
                                                .surfaceVariant,
                                        CircleShape
                                    )
                        )
                    }
                }
            }
        }
    }
}