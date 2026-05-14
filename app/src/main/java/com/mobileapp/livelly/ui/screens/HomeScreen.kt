package com.mobileapp.livelly.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
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
import com.mobileapp.livelly.data.UserPrefs
import com.mobileapp.livelly.logic.isCompletedToday
import com.mobileapp.livelly.logic.updateHabit
import com.mobileapp.livelly.ui.component.AppBackground
import com.mobileapp.livelly.ui.component.PrimaryButton
import com.mobileapp.livelly.ui.component.ProfileHeader

@Composable
fun HomeScreen(
    selectedHabitName: String?,
    navController: NavController
) {

    val context = LocalContext.current
    val habits by HabitPrefs.habitsFlow.collectAsState()
    val selectedHabit = habits.find { it.name == selectedHabitName }
    val name = UserPrefs.getName(context)

    val streak = selectedHabit?.streak ?: 0
    val progress = (streak % 30) / 30f

    val animatedProgress by animateFloatAsState(progress, tween(800))
    val animatedStreak by animateIntAsState(streak, tween(500))

    // 🔥 animations
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

    AppBackground {

        Column(
            modifier = Modifier
                .fillMaxSize().statusBarsPadding()  // ✅ FULL SCREEN FIX
                .padding(horizontal = 16.dp)
        ) {

            // 🔝 HEADER
            ProfileHeader(name)

            Spacer(Modifier.height(20.dp))

            // 📜 HABIT LIST
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {

                items(habits.size) { index ->

                    val habit = habits[index]
                    val completedToday = isCompletedToday(habit.lastCompleted)

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                            .clickable {
                                navController.navigate("habit_detail/${habit.name}")
                            },
                        shape = RoundedCornerShape(18.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color(0xFF1F2937).copy(alpha = 0.85f) // 🔥 glass effect
                        ),
                        border = BorderStroke(
                            1.dp,
                            Color.White.copy(alpha = 0.05f) // 🔥 subtle premium border
                        )
                    ) {

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {

                            Column {
                                Text(
                                    habit.name,
                                    color = Color.White,
                                    style = MaterialTheme.typography.bodyMedium
                                )

                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ) {

                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = null,
                                        tint = Color.White.copy(alpha = 0.5f),
                                        modifier = Modifier.size(14.dp)
                                    )

                                    Spacer(Modifier.width(6.dp))

                                    Text(
                                        "${habit.streak} day streak",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = Color.White.copy(alpha = 0.6f)
                                    )
                                }
                            }

                            Button(
                                onClick = {
                                    val updated = habits.map {
                                        if (it.name == habit.name) updateHabit(it)
                                        else it
                                    }
                                    HabitPrefs.saveHabits(context, updated)
                                },
                                enabled = !completedToday,
                                shape = RoundedCornerShape(50),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (completedToday)
                                        Color(0xFF374151)
                                    else
                                        Color(0xFF4F46E5)
                                )
                            ) {
                                Text(
                                    if (completedToday) "Done" else "Complete",
                                    color = Color.White
                                )
                            }
                        }
                    }
                }
            }

            // 🔻 FIXED BUTTONS
            PrimaryButton(
                text = "Create Habit",
                onClick = {
                    navController.navigate("habits_onboarding")
                }
            )

            Spacer(Modifier.height(10.dp))

            PrimaryButton(
                text = "View Habits",
                onClick = {
                    navController.navigate("habits_list")
                }
            )

            Spacer(Modifier.height(16.dp))
        }
    }
}


