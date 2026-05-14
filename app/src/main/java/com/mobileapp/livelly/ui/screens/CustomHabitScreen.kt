package com.mobileapp.livelly.ui.screens

import android.R.style
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.mobileapp.livelly.ui.component.AppBackground
import com.mobileapp.livelly.ui.component.PrimaryButton


@Composable
fun CustomHabitScreen(onNext: (String) -> Unit) {

    var habitName by remember { mutableStateOf("") }
    var showError by remember { mutableStateOf(false) }

    AppBackground {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp)
                .statusBarsPadding(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                "Create your habit",
                style = MaterialTheme.typography.titleLarge,
                color = Color.White
            )

            Spacer(Modifier.height(20.dp))

            TextField(
                value = habitName,
                onValueChange = {
                    habitName = it
                    showError = false
                },
                placeholder = { Text("Enter habit name") }
            )

            if (showError && habitName.isBlank()) {
                Text("Please enter habit name",style = MaterialTheme.typography.titleLarge, color = Color.Red)
            }

            Spacer(Modifier.weight(1f))

            PrimaryButton(
                text = "Next",
                onClick = {
                    if (habitName.isBlank()) {
                        showError = true
                    } else {
                        onNext(habitName)
                    }
                }
            )
        }
    }
}