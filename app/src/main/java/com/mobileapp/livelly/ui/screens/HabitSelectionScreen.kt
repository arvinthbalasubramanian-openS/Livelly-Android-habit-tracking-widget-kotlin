package com.mobileapp.livelly.ui.screens

import android.util.Log
import androidx.compose.animation.*
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mobileapp.livelly.data.Habit
import com.mobileapp.livelly.data.HabitPrefs
import com.mobileapp.livelly.ui.component.AnimatedScreen
import com.mobileapp.livelly.ui.component.AppBackground
import com.mobileapp.livelly.ui.component.PrimaryButton
import kotlinx.coroutines.delay

@Composable
fun HabitSelectionScreen(
    onNext: () -> Unit,
    onCustomHabitClick: () -> Unit
) {

    val context = LocalContext.current
    val savedHabits by HabitPrefs.habitsFlow.collectAsState()

    val suggestions = listOf(
        "Stay Active",
        "Learn Daily",
        "Focus Better"
    )

    var selectedHabits by remember { mutableStateOf(setOf<String>()) }
    var showError by remember { mutableStateOf(false) }
    var alertMessage by remember { mutableStateOf<String?>(null) }

    AppBackground {
        AnimatedScreen {

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp)
                    .statusBarsPadding()
            ) {

                // 🔝 TITLE
                Text(
                    "What do you want to build?",
                    style = MaterialTheme.typography.titleLarge,
                    color = Color.White
                )

                Spacer(Modifier.height(24.dp))

                // ⭐ PRIMARY ACTION
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onCustomHabitClick() },
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFF4F46E5)
                    )
                ) {
                    Box(
                        modifier = Modifier
                            .padding(18.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            "Create your own habit",
                            color = Color.White,
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                }

                Spacer(Modifier.height(30.dp))

                // 🔹 SUGGESTIONS LABEL
                Text(
                    "Try these ideas",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.White.copy(alpha = 0.6f)
                )

                Spacer(Modifier.height(12.dp))

                // 💡 CHIPS
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    suggestions.forEach { habit ->

                        val isSelected = selectedHabits.contains(habit)

                        Surface(
                            shape = RoundedCornerShape(50),
                            color = if (isSelected)
                                Color(0xFF4F46E5)
                            else
                                Color(0xFF1F2937).copy(alpha = 0.8f),
                            border = BorderStroke(
                                1.dp,
                                Color.White.copy(alpha = 0.05f)
                            ),
                            modifier = Modifier.clickable {
                                // your existing logic
                            }
                        ) {
                            Text(
                                habit,
                                modifier = Modifier.padding(
                                    horizontal = 16.dp,
                                    vertical = 10.dp
                                ),
                                color = Color.White
                            )
                        }
                    }
                }

                Spacer(Modifier.height(20.dp))

                // 🔴 ALERT
                AnimatedVisibility(
                    visible = alertMessage != null
                ) {
                    Text(
                        alertMessage ?: "",
                        color = Color.Red,
                        style = MaterialTheme.typography.bodySmall
                    )
                }

                Spacer(Modifier.weight(1f))

                // 🔽 CONTINUE BUTTON
                PrimaryButton(
                    text = "Continue",
                    onClick = {

                        if (selectedHabits.isEmpty()) {
                            showError = true
                        } else {

                            val existing = HabitPrefs.habitsFlow.value

                            val newHabits = selectedHabits.map {
                                Habit(name = it)
                            }

                            val updated = (existing + newHabits)
                                .distinctBy { it.name }

                            HabitPrefs.saveHabits(context, updated)

                            onNext()
                        }
                    }
                )

                if (showError && selectedHabits.isEmpty()) {
                    Spacer(Modifier.height(8.dp))
                    Text(
                        "Select at least one habit",
                        color = Color.Red,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }
    }
}

