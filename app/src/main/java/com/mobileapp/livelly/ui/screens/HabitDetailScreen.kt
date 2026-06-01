package com.mobileapp.livelly.ui.screens

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.mobileapp.livelly.data.Habit
import com.mobileapp.livelly.data.HabitPrefs
import com.mobileapp.livelly.ui.component.AppBackground
import com.mobileapp.livelly.ui.component.PlanetWidget
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

@Composable
fun HabitDetailScreen(
    habitName: String,
    navController: NavController
) {

    val context = LocalContext.current

    val habits by HabitPrefs.habitsFlow.collectAsState()

    val habit = habits.find {
        it.name == habitName
    }

    val streak = habit?.streak ?: 0

    val target = habit?.target ?: 30

    val progress =
        (streak.toFloat() / target)
            .coerceIn(0f, 1f)

    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = tween(800),
        label = ""
    )

    val animatedStreak by animateIntAsState(
        targetValue = streak,
        animationSpec = tween(500),
        label = ""
    )

    val infiniteTransition =
        rememberInfiniteTransition(label = "")

    val pulse by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000),
            repeatMode = RepeatMode.Reverse
        ),
        label = ""
    )

    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 12000,
                easing = LinearEasing
            )
        ),
        label = ""
    )

    AppBackground {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .statusBarsPadding()
                    .animateContentSize(),
                horizontalAlignment =
                Alignment.CenterHorizontally
            ) {

                Spacer(Modifier.height(20.dp))

                Text(
                    text = habitName,
                    style =
                    MaterialTheme.typography
                        .titleLarge,
                    color =
                    MaterialTheme.colorScheme
                        .onBackground
                )

                Spacer(Modifier.height(30.dp))

                Box(
                    modifier = Modifier
                        .height(320.dp)
                        .fillMaxWidth()
                        .graphicsLayer {

                            scaleX = pulse
                            scaleY = pulse
                        },
                    contentAlignment = Alignment.Center
                ) {

                    val onBackground =
                        MaterialTheme.colorScheme
                            .onBackground

                    PlanetWidget(
                        progress = progress
                    )

                }

                Spacer(
                    Modifier.height(24.dp)
                )

                Text(
                    text = "$animatedStreak / $target",
                    style =
                        MaterialTheme.typography
                            .headlineMedium,
                    color =
                        MaterialTheme.colorScheme
                            .onBackground
                )

                Spacer(
                    Modifier.height(6.dp)
                )

                Text(
                    text =
                        "${(progress * 100).toInt()}% Complete",
                    style =
                        MaterialTheme.typography
                            .bodyMedium,
                    color =
                        MaterialTheme.colorScheme
                            .onBackground
                            .copy(alpha = 0.6f)
                )

                val stageName = when {

                    progress < 0.2f -> "Seed World"

                    progress < 0.4f -> "Emerging Planet"

                    progress < 0.6f -> "Ringed Planet"

                    progress < 0.8f -> "Starbound World"

                    else -> "Living World"
                }

                Text(
                    text = stageName,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary
                )

                Spacer(Modifier.height(40.dp))

                Button(
                    onClick = {

                        habit?.let { currentHabit ->

                            val today = LocalDate.now()

                            val updatedHabit = if (
                                currentHabit.lastCompleted == 0L
                            ) {

                                currentHabit.copy(
                                    streak = 1,
                                    lastCompleted =
                                        System.currentTimeMillis()
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

                                android.util.Log.d(
                                    "LIVELLY",
                                    "today=$today lastDate=$lastDate streak=${currentHabit.streak}"
                                )

                                when {

                                    // already completed today
                                    lastDate == today -> {

                                        currentHabit
                                    }

                                    // completed yesterday
                                    lastDate == today.minusDays(1) -> {

                                        currentHabit.copy(
                                            streak =
                                                currentHabit.streak + 1,
                                            lastCompleted =
                                                System.currentTimeMillis()
                                        )
                                    }

                                    // missed days
                                    else -> {

                                        currentHabit.copy(
                                            streak = 1,
                                            lastCompleted =
                                                System.currentTimeMillis()
                                        )
                                    }
                                }
                            }

                            val updatedHabits =
                                habits.map {

                                    if (it.id == currentHabit.id)
                                        updatedHabit
                                    else
                                        it
                                }

                            HabitPrefs.saveHabits(
                                context,
                                updatedHabits
                            )
                        }
                    }
                ) {

                    Text("Complete")
                }


                Spacer(Modifier.height(20.dp))

                TextButton(
                    onClick = {
                        navController.popBackStack()
                    }
                ) {

                    Text("Back")
                }
            }
        }
    }
}