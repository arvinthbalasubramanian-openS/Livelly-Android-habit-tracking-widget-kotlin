package com.mobileapp.livelly.navigation

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.*
import com.mobileapp.livelly.data.Habit
import com.mobileapp.livelly.data.HabitPrefs
import com.mobileapp.livelly.data.UserPrefs
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

    NavHost(
        navController = navController,
        startDestination = startDestination,

        enterTransition = {
            scaleIn(
                initialScale = 0.98f,
                animationSpec = tween(250)
            )
        },

        exitTransition = {
            ExitTransition.None   // ✅ prevents flash
        },

        popEnterTransition = {
            scaleIn(
                initialScale = 0.98f,
                animationSpec = tween(250)
            )
        },

        popExitTransition = {
            ExitTransition.None   // ✅ prevents flash
        }
    ) {

        composable("start") {
            OnboardingStart {
                navController.navigate("name")
            }
        }

        composable("name") {
            NameScreen { name ->
                UserPrefs.saveName(context, name)
                navController.navigate("habits_onboarding") {
                    popUpTo("start") { inclusive = true }
                }
            }
        }

        composable("habits_onboarding") {
            HabitSelectionScreen(
                onNext = { navController.navigate("time") },
                onCustomHabitClick = {
                    navController.navigate("custom_habit")
                }
            )
        }

        composable("time") {
            TimeSelectionScreen(navController)
        }

        composable("custom_habit") {
            CustomHabitScreen { habitName ->
                val existing = HabitPrefs.habitsFlow.value
                val updated = existing + Habit(name = habitName)

                HabitPrefs.saveHabits(context, updated)
                navController.navigate("time")
            }
        }

        composable("success") {
            SuccessScreen {
                navController.navigate("home")
            }
        }

        composable("home/{habitName}") { backStackEntry ->
            val habitName = backStackEntry.arguments?.getString("habitName")

            HomeScreen(
                selectedHabitName = habitName,
                navController = navController
            )
        }

        composable("home") {
            HomeScreen(
                selectedHabitName = null,
                navController = navController
            )
        }

        composable("habits_list") {
            HabitsScreen(
                selectedHabitName = null,
                navController = navController
            )
        }

        composable("habit_detail/{habitName}") { backStackEntry ->
            val habitName =
                backStackEntry.arguments?.getString("habitName") ?: ""

            HabitDetailScreen(
                habitName = habitName,
                navController = navController
            )
        }
    }
}