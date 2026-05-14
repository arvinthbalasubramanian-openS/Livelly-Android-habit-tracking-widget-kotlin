package com.mobileapp.livelly.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun MainScreen() {
    Column {
        Text("Living Widget 🌱")

        Button(onClick = {
            // mark habit complete
        }) {
            Text("Complete Habit")
        }
    }
}