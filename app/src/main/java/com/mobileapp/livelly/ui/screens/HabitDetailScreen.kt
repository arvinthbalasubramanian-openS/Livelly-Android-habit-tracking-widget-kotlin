package com.mobileapp.livelly.ui.screens

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
import com.mobileapp.livelly.logic.updateHabit
import com.mobileapp.livelly.ui.component.AppBackground
import kotlinx.coroutines.launch

@Composable
fun HabitDetailScreen(
    habitName: String,
    navController: NavController
) {
    val context = LocalContext.current
    val habits by HabitPrefs.habitsFlow.collectAsState()
    val habit = habits.find { it.name == habitName }

    val streak = habit?.streak ?: 0
    val progress = (streak % 30) / 30f

    val animatedProgress by animateFloatAsState(progress, tween(800))
    val animatedStreak by animateIntAsState(streak, tween(500))

    val infiniteTransition = rememberInfiniteTransition(label = "")

    val pulse by infiniteTransition.animateFloat(
        1f, 1.05f,
        infiniteRepeatable(tween(2000), RepeatMode.Reverse),
        label = ""
    )

    val rotation by infiniteTransition.animateFloat(
        0f, 360f,
        infiniteRepeatable(tween(12000, easing = LinearEasing)),
        label = ""
    )

    val scope = rememberCoroutineScope()

    AppBackground {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(Modifier.height(20.dp))

            Text(
                habitName,
                style = MaterialTheme.typography.titleLarge,
                color = Color.White
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

                Canvas(modifier = Modifier.fillMaxSize()) {

                    val stroke = 18.dp.toPx()

                    drawCircle(
                        color = Color(0x33FFFFFF),
                        style = Stroke(width = stroke)
                    )

                    rotate(rotation) {
                        drawArc(
                            brush = Brush.sweepGradient(
                                listOf(Color(0xFFFF7A18), Color(0xFFFFB347))
                            ),
                            startAngle = -90f,
                            sweepAngle = 360 * animatedProgress,
                            useCenter = false,
                            style = Stroke(width = stroke, cap = StrokeCap.Round)
                        )
                    }
                }

                Column(horizontalAlignment = Alignment.CenterHorizontally) {

                    Text(
                        "STREAK",
                        style = MaterialTheme.typography.labelMedium,
                        color = Color.White.copy(alpha = 0.6f)
                    )

                    Spacer(Modifier.height(6.dp))

                    Text(
                        "$animatedStreak",
                        fontSize = 42.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )

                    Spacer(Modifier.height(4.dp))

                    Text(
                        "days",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray
                    )
                }
            }

            Spacer(Modifier.height(40.dp))

            Button(
                onClick = {
                    habit?.let {
                        val updated = habits.map {
                            if (it.name == habitName) updateHabit(it) else it
                        }
                        HabitPrefs.saveHabits(context, updated)
                    }
                }
            ) {
                Text("Complete")
            }

            Spacer(Modifier.height(20.dp))

            TextButton(
                onClick = { navController.popBackStack() }
            ) {
                Text("Back")
            }
        }
    }
}