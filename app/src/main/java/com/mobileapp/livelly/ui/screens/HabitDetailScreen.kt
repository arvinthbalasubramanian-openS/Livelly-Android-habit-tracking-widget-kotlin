package com.mobileapp.livelly.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.glance.appwidget.updateAll
import androidx.navigation.NavController
import com.mobileapp.livelly.data.HabitPrefs
import com.mobileapp.livelly.ui.component.AIInsightCard
import com.mobileapp.livelly.ui.component.AppBackground
import com.mobileapp.livelly.ui.component.HabitHeroCard
import com.mobileapp.livelly.ui.component.HabitStatsCard
import com.mobileapp.livelly.ui.component.WeeklyProgressCard
import com.mobileapp.livelly.widget.Livelly
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

@Composable
fun HabitDetailScreen(
    habitName: String,
    navController: NavController
) {

    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val habits by HabitPrefs.habitsFlow.collectAsState()

    val habit = habits.find {
        it.name == habitName
    }

    val streak = habit?.streak ?: 0

    val target = habit?.target ?: 30

    AppBackground {

        Scaffold(

            containerColor = Color.Transparent,

            bottomBar = {

                Surface(
                    tonalElevation = 4.dp
                ) {

                    Button(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),

                        onClick = {

                            habit?.let { currentHabit ->

                                val today = LocalDate.now()
                                val completedAt = System.currentTimeMillis()
                                val storedCompletionDates =
                                    (currentHabit.completionDates ?: emptyList()).orEmpty()
                                val completionDates =
                                    if (
                                        storedCompletionDates.isEmpty() &&
                                        currentHabit.lastCompleted > 0L
                                    )
                                        listOf(currentHabit.lastCompleted)
                                    else
                                        storedCompletionDates

                                val updatedHabit =
                                    if (
                                        currentHabit.lastCompleted == 0L
                                    ) {

                                        currentHabit.copy(
                                            streak = 1,

                                            lastCompleted =
                                                completedAt,

                                            completionDates =
                                                completionDates + completedAt
                                        )

                                    } else {

                                        val lastDate =
                                            Instant
                                                .ofEpochMilli(
                                                    currentHabit.lastCompleted
                                                )
                                                .atZone(
                                                    ZoneId.systemDefault()
                                                )
                                                .toLocalDate()

                                        when {

                                            lastDate == today -> {

                                                currentHabit
                                            }

                                            lastDate ==
                                                    today.minusDays(1) -> {

                                                currentHabit.copy(
                                                    streak =
                                                        currentHabit.streak + 1,

                                                    lastCompleted =
                                                        completedAt,

                                                    completionDates =
                                                        completionDates + completedAt
                                                )
                                            }

                                            else -> {

                                                currentHabit.copy(
                                                    streak = 1,

                                                    lastCompleted =
                                                        completedAt,

                                                    completionDates =
                                                        completionDates + completedAt
                                                )
                                            }
                                        }
                                    }

                                val updatedHabits =
                                    habits.map {

                                        if (
                                            it.id ==
                                            currentHabit.id
                                        )
                                            updatedHabit
                                        else
                                            it
                                    }

                                HabitPrefs.saveHabits(
                                    context,
                                    updatedHabits
                                )

                                scope.launch {
                                    Livelly().updateAll(context)
                                }
                            }
                        }
                    ) {

                        Text(
                            "Complete Today"
                        )
                    }
                }
            }
        ) { padding ->

            Column(

                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp)
                    .verticalScroll(
                        rememberScrollState()
                    )
            ) {

                Text(
                    text = habitName,
                    style =
                        MaterialTheme
                            .typography
                            .headlineMedium
                )

                Spacer(
                    Modifier.height(20.dp)
                )

                HabitHeroCard(
                    streak = streak,
                    target = target
                )

                Spacer(
                    Modifier.height(16.dp)
                )

                AIInsightCard(
                    habit = habit
                )

                Spacer(
                    Modifier.height(16.dp)
                )

                WeeklyProgressCard(
                    habit = habit
                )

                Spacer(
                    Modifier.height(16.dp)
                )

                HabitStatsCard(
                    streak = streak,
                    target = target,
                    completionDates = habit?.completionDates.orEmpty()
                )

                Spacer(
                    Modifier.height(100.dp)
                )
            }
        }
    }
}
