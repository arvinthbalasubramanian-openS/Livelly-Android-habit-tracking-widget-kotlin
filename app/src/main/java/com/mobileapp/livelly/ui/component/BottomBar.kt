package com.mobileapp.livelly.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.mobileapp.livelly.navigation.BottomNavItem

@Composable
fun BottomBar(
    navController: NavController,
    currentRoute: String? = null
) {

    val items = listOf(
        BottomNavItem.Home,
        BottomNavItem.Habits,
        BottomNavItem.Widgets
    )

    val backStackEntry = navController.currentBackStackEntryAsState()
    val navBackStackEntry by
    navController.currentBackStackEntryAsState()

    val actualRoute =
        currentRoute
            ?: navBackStackEntry
                ?.destination
                ?.route

    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.95f),
        tonalElevation = 0.dp,
        modifier = Modifier.navigationBarsPadding()
    ) {
        items.forEach { item ->

            val selected = actualRoute == item.route

            NavigationBarItem(
                selected = selected,
                onClick = {
                    navController.navigate(item.route) {
                        popUpTo(navController.graph.startDestinationId)
                        launchSingleTop = true
                    }
                },
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.title
                    )
                },
                label = {
                    Text(item.title)
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.onBackground,
                    unselectedIconColor = Color.Gray,
                    selectedTextColor = MaterialTheme.colorScheme.onBackground,
                    unselectedTextColor = Color.Gray,
                    indicatorColor = Color(0xFF4F46E5)
                )
            )
        }
    }
}

