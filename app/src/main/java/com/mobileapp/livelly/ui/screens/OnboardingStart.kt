package com.mobileapp.livelly.ui.screens

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mobileapp.livelly.ui.component.AnimatedScreen
import com.mobileapp.livelly.ui.component.AppBackground
import com.mobileapp.livelly.ui.component.PrimaryButton
import com.mobileapp.livelly.ui.theme.Accent
import com.mobileapp.livelly.ui.theme.DarkBg
import com.mobileapp.livelly.ui.theme.TextPrimary
import com.mobileapp.livelly.ui.theme.TextSecondary

@Composable
fun OnboardingStart(onStart: () -> Unit) {
    AppBackground {
        AnimatedScreen {

            Box(
                modifier = Modifier.fillMaxSize().animateContentSize(),
                contentAlignment = Alignment.Center
            ) {

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxHeight(0.8f).statusBarsPadding()

                ) {

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {

                        // 🌌 Top animation placeholder
                        Box(
                            modifier = Modifier
                                .size(120.dp)
                                .background(Accent, shape = CircleShape)
                        )

                        Spacer(Modifier.height(20.dp))

                        Text("Livelly", fontSize = 28.sp, color = TextPrimary, style = MaterialTheme.typography.titleLarge)

                        Text(
                            "Build habits and routines visually",
                            color = TextSecondary,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }

                    PrimaryButton(
                        text = "Let's start",
                        onClick = onStart
                    )
                }
            }
        }
    }
}