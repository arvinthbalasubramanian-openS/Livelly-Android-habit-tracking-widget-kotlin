package com.mobileapp.livelly.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mobileapp.livelly.data.Habit
import com.mobileapp.livelly.data.HabitPrefs
import com.mobileapp.livelly.ui.component.AppBackground
import com.mobileapp.livelly.ui.component.PlanetWidget
import com.mobileapp.livelly.ui.component.TreeStageWidget

@Composable
fun WidgetScreen() {
    val context = LocalContext.current
    val habits by HabitPrefs.habitsFlow.collectAsState()
    var selectedHabit by remember { mutableStateOf<Habit?>(null) }

    // Initialize selectedHabit if it's null and habits are available
    LaunchedEffect(habits) {
        if (selectedHabit == null && habits.isNotEmpty()) {
            selectedHabit = habits.first()
        }
    }

    AppBackground {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp)
                .statusBarsPadding()
        ) {
            Text(
                "Widget Customization",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(Modifier.height(24.dp))

            if (habits.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Add a habit first to customize its widget!", color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f))
                }
            } else {
                // Habit Selector
                Text(
                    "Select Habit",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                )
                
                Spacer(Modifier.height(8.dp))

                var expanded by remember { mutableStateOf(false) }
                Box {
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { expanded = true },
                        shape = RoundedCornerShape(12.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.onBackground.copy(alpha = 0.1f))
                    ) {
                        Text(
                            selectedHabit?.name ?: "Select a habit",
                            modifier = Modifier.padding(16.dp),
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    }

                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false },
                        modifier = Modifier.fillMaxWidth(0.9f).background(MaterialTheme.colorScheme.surface)
                    ) {
                        habits.forEach { habit ->
                            DropdownMenuItem(
                                text = { Text(habit.name) },
                                onClick = {
                                    selectedHabit = habit
                                    expanded = false
                                }
                            )
                        }
                    }
                }

                Spacer(Modifier.height(32.dp))

                // Preview Area
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(220.dp)
                        .clip(RoundedCornerShape(24.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)),
                    contentAlignment = Alignment.Center
                ) {
                    selectedHabit?.let { habit ->
                        val progress = (habit.streak.toFloat() / habit.target.coerceAtLeast(1)).coerceIn(0f, 1f)
                        if (habit.widgetStyle == "planet") {
                            PlanetWidget(progress = progress)
                        } else {
                            TreeStageWidget(streak = habit.streak)
                        }
                    }
                }

                Spacer(Modifier.height(32.dp))

                // Style Selection
                Text(
                    "Choose Style",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                )

                Spacer(Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    StyleCard(
                        title = "Cosmic Planet",
                        icon = "🪐",
                        isSelected = selectedHabit?.widgetStyle == "planet",
                        modifier = Modifier.weight(1f),
                        onClick = {
                            selectedHabit?.let { habit ->
                                updateHabitStyle(context, habit, "planet")
                                selectedHabit = habit.copy(widgetStyle = "planet")
                            }
                        }
                    )

                    StyleCard(
                        title = "Growth Tree",
                        icon = "🌲",
                        isSelected = selectedHabit?.widgetStyle == "tree",
                        modifier = Modifier.weight(1f),
                        onClick = {
                            selectedHabit?.let { habit ->
                                updateHabitStyle(context, habit, "tree")
                                selectedHabit = habit.copy(widgetStyle = "tree")
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun StyleCard(
    title: String,
    icon: String,
    isSelected: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier.clickable { onClick() },
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
        ),
        border = if (isSelected) null else BorderStroke(1.dp, MaterialTheme.colorScheme.onBackground.copy(alpha = 0.1f))
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(icon, fontSize = 32.sp)
            Spacer(Modifier.height(8.dp))
            Text(
                title,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                color = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onBackground
            )
            if (isSelected) {
                Spacer(Modifier.height(4.dp))
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}

private fun updateHabitStyle(context: android.content.Context, habit: Habit, style: String) {
    val habits = HabitPrefs.habitsFlow.value
    val updated = habits.map {
        if (it.id == habit.id) it.copy(widgetStyle = style) else it
    }
    HabitPrefs.saveHabits(context, updated)
}
