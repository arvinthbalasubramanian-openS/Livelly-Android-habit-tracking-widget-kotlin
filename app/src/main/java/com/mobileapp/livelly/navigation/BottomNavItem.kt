package com.mobileapp.livelly.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.List
import androidx.compose.material.icons.outlined.Widgets
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavItem(
    val route: String,
    val title: String,
    val icon: ImageVector
) {
    object Home : BottomNavItem(
        route = "home",
        title = "Home",
        icon = Icons.Outlined.Home
    )

    object Habits : BottomNavItem(
        route = "habits_list",
        title = "Habits",
        icon = Icons.Outlined.List
    )

    object Widgets : BottomNavItem(
        route = "widgets",
        title = "Widgets",
        icon = Icons.Outlined.Widgets
    )
}