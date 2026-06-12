package com.mobileapp.livelly.ui.screens

import android.widget.Toast
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
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
fun CustomHabitScreen(
    onNext: (String, Int) -> Unit
) {

    var habitName by remember { mutableStateOf("") }
    var showError by remember { mutableStateOf(false) }
    val context = LocalContext.current
    var target by remember {
        mutableStateOf("30")
    }

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
                color = MaterialTheme.colorScheme.onBackground
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
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.4f)
                    )
                },

                singleLine = true,

                shape = RoundedCornerShape(18.dp),

                colors = TextFieldDefaults.colors(

                    focusedContainerColor =
                        MaterialTheme.colorScheme.surface.copy(alpha = 0.85f),

                    unfocusedContainerColor =
                        MaterialTheme.colorScheme.surface.copy(alpha = 0.65f),

                    focusedTextColor = MaterialTheme.colorScheme.onBackground,
                    unfocusedTextColor = MaterialTheme.colorScheme.onBackground,

                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,

                    cursorColor = Color(0xFF6366F1)
                ),

                modifier = Modifier
                    .fillMaxWidth()
            )

            Spacer(Modifier.height(16.dp))

            var expanded by remember { mutableStateOf(false) }
            val options = listOf("7", "14", "21", "30", "60", "100")

            Box(modifier = Modifier.fillMaxWidth()) {
                TextField(
                    value = "Target: $target days",
                    onValueChange = {},
                    readOnly = true,
                    shape = RoundedCornerShape(18.dp),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.85f),
                        unfocusedContainerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.65f),
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent,
                        focusedTextColor = MaterialTheme.colorScheme.onBackground,
                        unfocusedTextColor = MaterialTheme.colorScheme.onBackground,
                        disabledTextColor = MaterialTheme.colorScheme.onBackground
                    ),
                    modifier = Modifier.fillMaxWidth(),
                    trailingIcon = {
                        Icon(
                            imageVector = if (expanded) Icons.Filled.ArrowDropUp else Icons.Filled.ArrowDropDown,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                    }
                )
                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .clickable { expanded = true }
                )

                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.surface)
                ) {
                    options.forEach { option ->
                        DropdownMenuItem(
                            text = { Text("$option days", color = MaterialTheme.colorScheme.onSurface) },
                            onClick = {
                                target = option
                                expanded = false
                            }
                        )
                    }
                }
            }

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
                        onNext(
                            habitName,
                            target.toIntOrNull() ?: 30
                        )
                    }
                }
            )
        }
    }
}
