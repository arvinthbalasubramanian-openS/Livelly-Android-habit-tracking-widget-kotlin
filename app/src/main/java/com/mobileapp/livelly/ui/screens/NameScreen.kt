package com.mobileapp.livelly.ui.screens

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.mobileapp.livelly.ui.component.AnimatedScreen
import com.mobileapp.livelly.ui.component.AppBackground
import com.mobileapp.livelly.ui.component.PrimaryButton


@Composable
fun NameScreen(onNext: (String) -> Unit) {

    var name by remember { mutableStateOf("") }
    var showError by remember { mutableStateOf(false) }

    AppBackground {
        AnimatedScreen {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .statusBarsPadding().animateContentSize()

            ) {

                Spacer(Modifier.height(40.dp))

                Text("Firstly, what's your name?", color = MaterialTheme.colorScheme.onBackground, style = MaterialTheme.typography.titleLarge,)

                Spacer(Modifier.height(20.dp))

                TextField(
                    value = name,
                    onValueChange = {
                        name = it
                        showError = false
                    },
                    placeholder = { Text("Enter your name", style = MaterialTheme.typography.titleLarge)}
                )

                if (showError && name.isBlank()) {
                    Text("Please enter your name", color = Color.Red, style = MaterialTheme.typography.bodySmall)
                }

                Spacer(Modifier.weight(1f))

                PrimaryButton(
                    text = "Proceed",
                    onClick = {
                        if (name.isBlank()) {
                            showError = true
                        } else {
                            onNext(name)
                        }
                    }
                )
            }
        }
    }
}