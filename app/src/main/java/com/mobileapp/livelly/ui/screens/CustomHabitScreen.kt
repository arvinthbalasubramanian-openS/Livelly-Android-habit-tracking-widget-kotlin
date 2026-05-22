package com.mobileapp.livelly.ui.screens

import android.R.style
import android.widget.Toast
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.mobileapp.livelly.ui.component.AppBackground
import com.mobileapp.livelly.ui.component.PrimaryButton


@Composable
fun CustomHabitScreen(onNext: (String) -> Unit) {

    var habitName by remember { mutableStateOf("") }
    var showError by remember { mutableStateOf(false) }
    val context = LocalContext.current

    AppBackground {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp)
                .statusBarsPadding().animateContentSize(),
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

                placeholder = {
                    Text(
                        "Enter your habit name",
                        color = Color.White.copy(alpha = 0.4f)
                    )
                },

                singleLine = true,

                shape = RoundedCornerShape(18.dp),

                colors = TextFieldDefaults.colors(

                    focusedContainerColor =
                        Color(0xFF1F2937).copy(alpha = 0.85f),

                    unfocusedContainerColor =
                        Color(0xFF1F2937).copy(alpha = 0.65f),

                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,

                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,

                    cursorColor = Color(0xFF6366F1)
                ),

                modifier = Modifier
                    .fillMaxWidth()
            )

            Spacer(Modifier.weight(1f))

            PrimaryButton(
                text = "Next",
                onClick = {
                    if (habitName.isBlank()) {

                        Toast.makeText(
                            context,
                            "Enter a habit name",
                            Toast.LENGTH_SHORT
                        ).show()

                    } else {
                        onNext(habitName)
                    }
                }
            )
        }
    }
}