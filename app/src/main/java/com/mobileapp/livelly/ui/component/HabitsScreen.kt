package com.mobileapp.livelly.ui.component

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.mobileapp.livelly.data.Habit
import com.mobileapp.livelly.data.HabitPrefs
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun HabitsScreen(
    selectedHabitName: String?,
    navController: NavController
) {

    val context = LocalContext.current
    val habits by HabitPrefs.habitsFlow.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    var habitToDelete by remember { mutableStateOf<Habit?>(null) }
    var removingHabit by remember { mutableStateOf<String?>(null) }

    AppBackground {
        Scaffold(
            containerColor = Color.Transparent,
            snackbarHost = { SnackbarHost(snackbarHostState) }
        ) { padding ->

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(horizontal = 16.dp)
                    .statusBarsPadding()
            ) {

                // 🔝 HEADER
                Text(
                    "Your Habits",
                    style = MaterialTheme.typography.titleLarge,
                    color = Color.White
                )

                Spacer(Modifier.height(24.dp))

                // 🧊 EMPTY STATE (premium)
                if (habits.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 60.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {

                            Text(
                                "No habits yet",
                                style = MaterialTheme.typography.titleMedium,
                                color = Color.White.copy(alpha = 0.7f)
                            )

                            Spacer(Modifier.height(6.dp))

                            Text(
                                "Create your first habit to get started",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.White.copy(alpha = 0.5f)
                            )
                        }
                    }
                }

                // 📜 LIST
                LazyColumn {

                    items(
                        items = habits,
                        key = { it.name }
                    ) { habit ->

                        AnimatedVisibility(
                            visible = removingHabit != habit.name,
                            enter = fadeIn(),
                            exit = fadeOut(tween(300)) +
                                    shrinkVertically(tween(300)) +
                                    slideOutVertically { it / 2 }
                        ) {

                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp),
                                shape = RoundedCornerShape(18.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = Color(0xFF1F2937).copy(alpha = 0.85f)
                                ),
                                border = BorderStroke(
                                    1.dp,
                                    Color.White.copy(alpha = 0.05f)
                                )
                            ) {

                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(18.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {

                                    Column {

                                        Text(
                                            habit.name,
                                            style = MaterialTheme.typography.titleMedium,
                                            color = Color.White
                                        )

                                        Spacer(Modifier.height(4.dp))

                                        Text(
                                            "${habit.streak} day streak",
                                            style = MaterialTheme.typography.bodySmall,
                                            color = Color.White.copy(alpha = 0.6f)
                                        )
                                    }

                                    Icon(
                                        imageVector = Icons.Default.Delete,
                                        contentDescription = null,
                                        tint = Color.White.copy(alpha = 0.4f),
                                        modifier = Modifier
                                            .size(20.dp)
                                            .clickable {
                                                habitToDelete = habit
                                            }
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(Modifier.weight(1f))

                // 🔻 BUTTON
                PrimaryButton(
                    text = "Create Habit",
                    onClick = {
                        navController.navigate("habits_onboarding")
                    }
                )
            }
        }
    }

    // 🔥 DIALOG (unchanged logic, refined text)
    habitToDelete?.let { habit ->
        AlertDialog(
            onDismissRequest = { habitToDelete = null },
            title = { Text("Remove habit") },
            text = { Text("This will permanently remove \"${habit.name}\"") },
            confirmButton = {
                TextButton(
                    onClick = {

                        removingHabit = habit.name

                        scope.launch {
                            delay(300)

                            val updated = habits.filter { it.name != habit.name }
                            HabitPrefs.saveHabits(context, updated)

                            removingHabit = null

                            val result = snackbarHostState.showSnackbar(
                                message = "${habit.name} removed",
                                actionLabel = "Undo"
                            )

                            if (result == SnackbarResult.ActionPerformed) {
                                val restored =
                                    HabitPrefs.habitsFlow.value + habit
                                HabitPrefs.saveHabits(context, restored)
                            }
                        }

                        habitToDelete = null
                    }
                ) {
                    Text("Remove", color = Color.Red)
                }
            },
            dismissButton = {
                TextButton(onClick = { habitToDelete = null }) {
                    Text("Cancel")
                }
            }
        )
    }
}