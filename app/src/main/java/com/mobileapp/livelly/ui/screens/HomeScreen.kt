package com.mobileapp.livelly.ui.screens

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.mobileapp.livelly.data.HabitPrefs
import com.mobileapp.livelly.data.UserPrefs
import com.mobileapp.livelly.ui.component.AppBackground
import com.mobileapp.livelly.ui.component.BottomBar

@Composable
fun HomeScreen(
    selectedHabitName: String?,
    navController: NavController
) {

    val context = LocalContext.current
    val habits by HabitPrefs.habitsFlow.collectAsState()

    val userName = UserPrefs.getName(context)
        ?.lowercase()
        ?.replaceFirstChar { it.uppercase() }
        ?: ""

    val focusHabit = habits.firstOrNull()

    AppBackground {

        Scaffold(
            containerColor = Color.Transparent,
            bottomBar = {
                BottomBar(navController)
            }
        ) { padding ->

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(horizontal = 20.dp)
                    .statusBarsPadding().animateContentSize()
            ) {
                Spacer(Modifier.height(10.dp))

                // HEADER
                Text(
                    text = "Good to see you, $userName",
                    style = MaterialTheme.typography.titleLarge,
                    color = Color.White
                )

                Spacer(Modifier.height(6.dp))

                Text(
                    text = "Consistency creates growth",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White.copy(alpha = 0.5f)
                )

                Spacer(Modifier.height(28.dp))

                // TODAY FOCUS
                Text(
                    "Today's Focus",
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.White
                )

                Spacer(Modifier.height(12.dp))

                focusHabit?.let { habit ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                navController.navigate(
                                    "habit_detail/${habit.name}"
                                )
                            },
                        shape = RoundedCornerShape(24.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color(0xFF4F46E5)
                        )
                    ) {

                        Column(
                            modifier = Modifier.padding(24.dp)
                        ) {

                            Text(
                                habit.name,
                                style = MaterialTheme.typography.headlineSmall,
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )

                            Spacer(Modifier.height(10.dp))

                            Text(
                                "${habit.streak} day streak",
                                color = Color.White.copy(alpha = 0.8f)
                            )

                            Spacer(Modifier.height(20.dp))
                            Button(
                                onClick = {
                                    navController.navigate(
                                        "habit_detail/${habit.name}"
                                    )
                                },
                                shape = RoundedCornerShape(50),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color.White
                                )
                            ) {
                                Text(
                                    "Open",
                                    color = Color.Black
                                )
                            }
                        }
                    }
                }

                Spacer(Modifier.height(30.dp))

                Text(
                    "Your Habits",
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.White
                )

                Spacer(Modifier.height(12.dp))

                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {

                    items(habits) { habit ->

                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    navController.navigate(
                                        "habit_detail/${habit.name}"
                                    )
                                },
                            shape = RoundedCornerShape(18.dp),
                            colors = CardDefaults.cardColors(
                                containerColor =
                                    Color(0xFF1F2937).copy(alpha = 0.85f)
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

                                Surface(
                                    shape = RoundedCornerShape(50),
                                    color = Color(0xFF4F46E5).copy(alpha = 0.15f)
                                ) {

                                    Text(
                                        "Open",
                                        modifier = Modifier.padding(
                                            horizontal = 14.dp,
                                            vertical = 8.dp
                                        ),
                                        color = Color(0xFF818CF8),
                                        style = MaterialTheme.typography.bodySmall
                                    )
                                }
                            }
                        }
                    }

                    item {
                        Spacer(Modifier.height(100.dp))
                    }
                }
            }
        }
    }
}