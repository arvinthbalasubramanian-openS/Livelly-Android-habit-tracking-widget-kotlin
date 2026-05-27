package com.mobileapp.livelly.navigation

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.*
import com.mobileapp.livelly.data.Habit
import com.mobileapp.livelly.data.HabitPrefs
import com.mobileapp.livelly.data.UserPrefs
import com.mobileapp.livelly.ui.component.BottomBar
import com.mobileapp.livelly.ui.component.HabitsScreen
import com.mobileapp.livelly.ui.screens.*

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AppNav() {

    val navController = rememberNavController()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        HabitPrefs.loadHabits(context)
    }

    val savedName = UserPrefs.getName(context)

    val startDestination = if (savedName.isNullOrEmpty()) {
        "start"
    } else {
        "home"
    }

    val navBackStackEntry by
    navController.currentBackStackEntryAsState()

    val currentRoute =
        navBackStackEntry
            ?.destination
            ?.route

    val showBottomBar = currentRoute in listOf(

        "home",

        "habits_list",

        "settings",

        "habit_detail/{habitName}"
    )

    Scaffold(

        containerColor = MaterialTheme.colorScheme.background,

        bottomBar = {

            if (showBottomBar) {

                BottomBar(navController)
            }
        }

    ) { innerPadding ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {

            NavHost(
                navController = navController,
                startDestination = startDestination
            ) {

                composable(Routes.START) {
                    OnboardingStart {
                        navController.navigate(Routes.NAME)
                    }
                }

                composable(Routes.NAME) {
                    NameScreen { name ->
                        UserPrefs.saveName(context, name)
                        navController.navigate(Routes.HABITS_ONBOARDING) {
                            popUpTo(Routes.START) { inclusive = true }
                        }
                    }
                }

                composable(Routes.HABITS_ONBOARDING) {
                    HabitSelectionScreen(
                        onNext = { navController.navigate(Routes.TIME) },
                        onCustomHabitClick = {
                            navController.navigate(Routes.CUSTOM_HABIT)
                        }
                    )
                }

                composable(Routes.TIME) {
                    TimeSelectionScreen(navController)
                }

                composable("custom_habit") {

                    CustomHabitScreen { habitName, target ->

                        val existing =
                            HabitPrefs.habitsFlow.value

                        val updated = existing + Habit(

                            name = habitName,

                            target = target
                        )

                        HabitPrefs.saveHabits(
                            context,
                            updated
                        )

                        navController.navigate("time")
                    }
                }

                composable(Routes.SUCCESS) {
                    SuccessScreen {
                        navController.navigate(Routes.HOME)
                    }
                }

                composable(Routes.HOME_HABIT_NAME) { backStackEntry ->
                    val habitName = backStackEntry.arguments?.getString(Routes.HABIT_NAME)

                    HomeScreen(
                        selectedHabitName = habitName,
                        navController = navController
                    )
                }

                composable(Routes.HOME) {
                    HomeScreen(
                        selectedHabitName = null,
                        navController = navController
                    )
                }

                composable(Routes.HABITS_LIST) {
                    HabitsScreen(
                        selectedHabitName = null,
                        navController = navController
                    )
                }

                composable(Routes.HABIT_DETAIL) { backStackEntry ->
                    val habitName =
                        backStackEntry.arguments?.getString(Routes.HABIT_NAME) ?: ""

                    HabitDetailScreen(
                        habitName = habitName,
                        navController = navController
                    )
                }

                composable(Routes.SETTINGS) {
                    SettingsScreen(navController)
                }
            }
        }
    }
}