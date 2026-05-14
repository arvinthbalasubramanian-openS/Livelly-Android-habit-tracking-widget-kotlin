package com.mobileapp.livelly.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mobileapp.livelly.ui.theme.Accent
import com.mobileapp.livelly.ui.theme.DarkBg
import com.mobileapp.livelly.ui.theme.TextPrimary
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.unit.sp
import com.mobileapp.livelly.ui.component.AnimatedScreen
import com.mobileapp.livelly.ui.component.AppBackground
import com.mobileapp.livelly.ui.component.PrimaryButton

@Composable
fun SuccessScreen(onDone: () -> Unit) {
    AppBackground {
        AnimatedScreen {
            Column(
                modifier = Modifier.fillMaxSize().statusBarsPadding(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {

                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = null,
                    tint = Accent,
                    modifier = Modifier.size(80.dp)
                )

                Spacer(Modifier.height(20.dp))

                Text("You are all set!", color = TextPrimary, fontSize = 22.sp, style = MaterialTheme.typography.titleLarge)

                Spacer(Modifier.height(40.dp))

                PrimaryButton(
                    text = "Start tracking",
                    onClick = onDone
                )
            }
        }
    }
}