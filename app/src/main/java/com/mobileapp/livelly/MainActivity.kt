package com.mobileapp.livelly

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.appwidget.updateAll
import androidx.lifecycle.lifecycleScope
import com.mobileapp.livelly.data.HabitRepository
import com.mobileapp.livelly.logic.getWorldState
import com.mobileapp.livelly.widget.Livelly
import kotlinx.coroutines.launch
import com.mobileapp.livelly.navigation.AppNav
import androidx.compose.foundation.isSystemInDarkTheme
import com.mobileapp.livelly.data.SettingsPrefs
import com.mobileapp.livelly.ui.theme.LivellyTheme
import com.mobileapp.livelly.ui.theme.ThemeState
import android.app.Activity
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import com.google.accompanist.systemuicontroller.rememberSystemUiController

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(
            window,
            false
        )
        setContent {

            ThemeState.isDarkTheme.value =
                SettingsPrefs.isDarkTheme(this)

            LivellyTheme(
                darkTheme =
                    ThemeState.isDarkTheme.value
            ) {

                val systemUiController = rememberSystemUiController()
                val isDarkTheme = ThemeState.isDarkTheme.value
                val backgroundColor = MaterialTheme.colorScheme.background

                SideEffect {
                    systemUiController.setStatusBarColor(
                        color = backgroundColor,
                        darkIcons = !isDarkTheme
                    )
                }

                AppNav()
            }
        }





    }




}