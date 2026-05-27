package com.mobileapp.livelly.ui.screens

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
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
                        .size(260.dp)
                        .graphicsLayer {

                            scaleX = pulse
                            scaleY = pulse
                        },
                    contentAlignment = Alignment.Center
                ) {

                    val onBackground =
                        MaterialTheme.colorScheme
                            .onBackground

                    Canvas(
                        modifier = Modifier.fillMaxSize()
                    ) {

                        val stroke = 18.dp.toPx()

                        drawCircle(
                            color =
                            onBackground.copy(alpha = 0.2f),
                            style = Stroke(width = stroke)
                        )

                        rotate(rotation) {

                            drawArc(
                                brush = Brush.sweepGradient(
                                    listOf(
                                        Color(0xFFFF7A18),
                                        Color(0xFFFFB347)
                                    )
                                ),
                                startAngle = -90f,
                                sweepAngle =
                                360 * animatedProgress,
                                useCenter = false,
                                style = Stroke(
                                    width = stroke,
                                    cap = StrokeCap.Round
                                )
                            )
                        }
                    }

                    Column(
                        horizontalAlignment =
                        Alignment.CenterHorizontally
                    ) {

                        Text(
                            text = "STREAK",
                            style =
                            MaterialTheme.typography
                                .labelMedium,
                            color =
                            MaterialTheme.colorScheme
                                .onBackground
                                .copy(alpha = 0.6f)
                        )

                        Spacer(Modifier.height(6.dp))

                        Text(
                            text = "$animatedStreak",
                            fontSize = 42.sp,
                            fontWeight = FontWeight.Bold,
                            color =
                            MaterialTheme.colorScheme.onBackground
                        )

                        Spacer(Modifier.height(4.dp))

                        Text(
                            text = "of $target days",
                            style =
                            MaterialTheme.typography.bodySmall,
                            color =
                            MaterialTheme.colorScheme
                                .onBackground
                                .copy(alpha = 0.5f)
                        )
                    }
                }

                Spacer(Modifier.height(40.dp))

                Button(
                    onClick = {

                        habit?.let { currentHabit ->

                            val today =
                                LocalDate.now()

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
                                            currentHabit
                                                .lastCompleted
                                        )
                                        .atZone(
                                            ZoneId.systemDefault()
                                        )
                                        .toLocalDate()

                                when {

                                    // already completed today
                                    lastDate == today -> {

                                        currentHabit
                                    }

                                    // completed yesterday
                                    lastDate.plusDays(1)
                                            == today -> {

                                        currentHabit.copy(
                                            streak =
                                            currentHabit.streak + 1,
                                            lastCompleted =
                                            System.currentTimeMillis()
                                        )
                                    }

                                    // streak broken
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

                                    if (it.name == habitName)
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