package com.mobileapp.livelly.ui.screens

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import coil.compose.AsyncImage
import com.mobileapp.livelly.data.ProfilePrefs
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Person
import androidx.compose.ui.draw.clip
import com.mobileapp.livelly.navigation.Routes
import com.mobileapp.livelly.ui.theme.DarkBackground
import com.mobileapp.livelly.ui.theme.ThemeState


@Composable
fun HomeScreen(
    selectedHabitName: String?,
    navController: NavController
) {

    val context = LocalContext.current
    val habits by HabitPrefs.habitsFlow.collectAsState()
    val profileImage =
        ProfilePrefs.getProfileImage(context)

    val userName = UserPrefs.getName(context)
        ?.lowercase()
        ?.replaceFirstChar { it.uppercase() }
        ?: ""

    val focusHabit = habits.firstOrNull()

    AppBackground {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp)
                .statusBarsPadding()
                .animateContentSize()
        ) {
            Spacer(Modifier.height(10.dp))

            // HEADER
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement =
                Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Column {

                    Text(
                        text = "Good to see you, $userName",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onBackground
                    )

                    Spacer(Modifier.height(6.dp))

                    Text(
                        text = "Consistency creates growth",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f)
                    )
                }

                Box(
                    modifier = Modifier
                        .size(52.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                        .clickable {
                            navController.navigate(Routes.SETTINGS)
                        },
                    contentAlignment = Alignment.Center
                ) {

                    if (profileImage != null) {

                        AsyncImage(
                            model = profileImage,
                            contentDescription = null,
                            modifier = Modifier.fillMaxSize()
                        )

                    } else {

                        Icon(
                            imageVector = Icons.Outlined.Person,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f)
                        )
                    }
                }
            }

            Spacer(Modifier.height(28.dp))

            // TODAY FOCUS
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        navController.navigate(Routes.HABITS_LIST)
                    },
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (habits.isEmpty()) "Add habits to track" else "Today's Focus",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onBackground
                )

                if (habits.isNotEmpty()) {
                    Text(
                        text = "See all",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color(0xFF4F46E5) // Using the app's primary indigo color
                    )
                }
            }

            Spacer(Modifier.height(12.dp))

            if (habits.isEmpty()) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            navController.navigate(Routes.HABITS_ONBOARDING)
                        },
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
                    ),
                    border = BorderStroke(
                        1.dp,
                        MaterialTheme.colorScheme.onBackground.copy(alpha = 0.1f)
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            "Start your journey today",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
                        )
                        Spacer(Modifier.height(16.dp))
                        Button(
                            onClick = {
                                navController.navigate(Routes.HABITS_ONBOARDING)
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF4F46E5)
                            )
                        ) {
                            Text("Add Habit")
                        }
                    }
                }
            }

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
                            color = MaterialTheme.colorScheme.onBackground,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(Modifier.height(10.dp))

                        Text(
                            "${habit.streak} day streak",
                            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f)
                        )

                        Spacer(Modifier.height(20.dp))
                        Button(
                            onClick = {
                                navController.navigate(
                                    "habit_detail/${habit.name}"
                                )
                            },
                            colors = ButtonDefaults.buttonColors(

                                containerColor =
                                    if (ThemeState.isDarkTheme.value)

                                        DarkBackground
                                    else
                                        MaterialTheme.colorScheme.surface,

                                contentColor =
                                    if (ThemeState.isDarkTheme.value)
                                        Color.White
                                    else
                                        Color.Black
                            )
                        ) {

                            Text("Open")
                        }
                    }
                }
            }

            Spacer(Modifier.height(100.dp))
        }
    }
}