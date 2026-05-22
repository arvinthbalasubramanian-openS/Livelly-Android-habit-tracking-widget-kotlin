package com.mobileapp.livelly.ui.screens

import android.app.TimePickerDialog
import android.content.Context
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.mobileapp.livelly.ui.component.AnimatedScreen
import com.mobileapp.livelly.ui.component.AppBackground
import com.mobileapp.livelly.ui.component.PrimaryButton
import com.mobileapp.livelly.ui.theme.Accent
import com.mobileapp.livelly.ui.theme.CardBg
import com.mobileapp.livelly.ui.theme.DarkBg
import com.mobileapp.livelly.ui.theme.TextPrimary
import java.util.Calendar

@Composable
fun TimeSelectionScreen(navController: NavController) {
    val context = LocalContext.current

    val times = listOf("6:00 AM", "12:00 PM", "6:00 PM")
    var selected by remember { mutableStateOf("") }

    AppBackground {
        AnimatedScreen {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .statusBarsPadding().animateContentSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Spacer(Modifier.height(40.dp))

                Text(
                    "When should we remind you?",
                    color = TextPrimary,
                    style = MaterialTheme.typography.titleLarge,
                )

                Spacer(Modifier.height(20.dp))

                // ⏰ Default options
                times.forEach { time ->
                    TimeCard(time, selected == time) {
                        selected = time
                    }
                }

                if (selected.isNotEmpty() && selected !in times) {
                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "Selected: $selected",
                        color = TextPrimary
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                // ➕ Custom Time Button
                Card(
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .clickable {
                            showTimePicker(context) { pickedTime: String ->
                                selected = pickedTime
                            }
                        },
                    colors = CardDefaults.cardColors(
                        containerColor = if (selected !in times && selected.isNotEmpty())
                            Accent
                        else
                            CardBg
                    )
                ) {
                    Box(
                        modifier = Modifier.padding(20.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("➕ Choose custom time", color = TextPrimary, style = MaterialTheme.typography.bodySmall)
                    }
                }

                Spacer(modifier = Modifier.weight(1f))

                var showError by remember { mutableStateOf(false) }

                if (showError && selected.isEmpty()) {
                    Text(
                        "Please select a time",
                        color = Color.Red,
                        fontSize = 12.sp,
                        style = MaterialTheme.typography.bodySmall
                    )
                }

                PrimaryButton(
                    text = "Continue",
                    onClick = {
                        if (selected.isEmpty()) {
                            showError = true
                        } else {
                            navController.navigate("success")
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun TimeCard(
    time: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(0.8f)
            .clickable { onClick() },
        colors = CardDefaults.cardColors(
            containerColor = if (selected) Accent else CardBg
        ),
        shape = RoundedCornerShape(16.dp)
    ) {
        Box(
            modifier = Modifier.padding(20.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = time,
                color = TextPrimary
            )
        }
    }
}


fun showTimePicker(context: Context, onTimeSelected: (String) -> Unit) {
    val calendar = Calendar.getInstance()

    val timePicker = TimePickerDialog(
        context,
        { _, hour, minute ->

            val amPm = if (hour < 12) "AM" else "PM"
            val hourFormatted = if (hour % 12 == 0) 12 else hour % 12

            val formatted = String.format(
                "%d:%02d %s",
                hourFormatted,
                minute,
                amPm
            )
            onTimeSelected(formatted)
        },
        calendar.get(Calendar.HOUR_OF_DAY),
        calendar.get(Calendar.MINUTE),
        false
    )

    timePicker.show()
}