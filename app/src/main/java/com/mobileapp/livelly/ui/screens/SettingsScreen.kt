package com.mobileapp.livelly.ui.screens

import android.content.Intent
import android.widget.Toast
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.mobileapp.livelly.data.SettingsPrefs
import com.mobileapp.livelly.data.UserPrefs
import com.mobileapp.livelly.navigation.Routes
import com.mobileapp.livelly.ui.component.AppBackground
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.border
import coil.compose.AsyncImage
import com.mobileapp.livelly.data.ProfilePrefs
import com.mobileapp.livelly.ui.theme.ThemeState
import com.mobileapp.livelly.notifications.NotificationScheduler
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.io.File
import com.yalantis.ucrop.UCrop
import com.yalantis.ucrop.UCropActivity


@Composable
fun SettingsScreen(
    navController: NavController
) {

    val context = LocalContext.current

    var notificationsEnabled by remember {
        mutableStateOf(SettingsPrefs.isDailyReminderEnabled(context)
        )
    }

    var profileImageUri by remember {
        mutableStateOf(
            ProfilePrefs.getProfileImage(context)
        )
    }

    var compactWidget by remember {
        mutableStateOf(
            SettingsPrefs
                .isCompactWidgetEnabled(context)
        )
    }

    var transparentWidget by remember {
        mutableStateOf(
            SettingsPrefs
                .isTransparentWidgetEnabled(context)
        )
    }

    var darkThemeEnabled by remember {
        mutableStateOf(
            SettingsPrefs.isDarkTheme(context)
        )
    }

    val cropLauncher =
        rememberLauncherForActivityResult(
            contract =
                ActivityResultContracts.StartActivityForResult()
        ) { result ->

            if (result.resultCode ==
                android.app.Activity.RESULT_OK
            ) {

                val resultUri =
                    UCrop.getOutput(result.data!!)

                resultUri?.let {

                    profileImageUri = it.toString()

                    ProfilePrefs.saveProfileImage(
                        context,
                        it.toString()
                    )
                }
            }
        }

    val launcher =
        rememberLauncherForActivityResult(
            contract =
                ActivityResultContracts.OpenDocument()
        ) { uri: Uri? ->

            uri?.let {

                context.contentResolver
                    .takePersistableUriPermission(
                        it,
                        Intent.FLAG_GRANT_READ_URI_PERMISSION
                    )

                val destinationUri = Uri.fromFile(
                    File(
                        context.cacheDir,
                        "cropped_profile.jpg"
                    )
                )

                val cropIntent = UCrop.of(
                    it,
                    destinationUri
                )
                    .withAspectRatio(1f, 1f)
                    .withMaxResultSize(800, 800)
                    .withOptions(
                        UCrop.Options().apply {

                            setCircleDimmedLayer(true)

                            setFreeStyleCropEnabled(false)

                            setHideBottomControls(false)

                            setShowCropGrid(false)

                            setShowCropFrame(false)

                            setToolbarTitle("Profile Photo")

                            setToolbarWidgetColor(
                                android.graphics.Color.WHITE
                            )

                            setToolbarColor(
                                android.graphics.Color.parseColor("#111827")
                            )

                            setStatusBarColor(
                                android.graphics.Color.parseColor("#111827")
                            )

                            setAllowedGestures(
                                UCropActivity.SCALE,
                                UCropActivity.SCALE,
                                UCropActivity.SCALE
                            )



                        }
                    )
                    .getIntent(context)

                cropLauncher.launch(cropIntent)
            }
        }







    val userName = UserPrefs.getName(context)
        ?.lowercase()
        ?.replaceFirstChar { it.uppercase() }
        ?: "User"

    AppBackground {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp)
                .statusBarsPadding()
                .verticalScroll(rememberScrollState())
                .animateContentSize()
        ) {

            Spacer(Modifier.height(10.dp))

            // 🔝 TITLE
            Text(
                "Settings",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onBackground,
                fontWeight = FontWeight.Bold
            )

            Spacer(Modifier.height(28.dp))

            // 👤 PROFILE CARD
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(
                    containerColor =
                        MaterialTheme.colorScheme.surface.copy(alpha = 0.85f)
                ),
                border = BorderStroke(
                    1.dp,
                    MaterialTheme.colorScheme.onBackground.copy(alpha = 0.05f)
                )
            ) {

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Box(
                        modifier = Modifier
                            .size(90.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.surfaceVariant)
                            .border(
                                1.dp,
                                MaterialTheme.colorScheme.onBackground.copy(alpha = 0.08f),
                                CircleShape
                            )
                            .clickable {

                                launcher.launch(arrayOf("image/*"))
                            },
                        contentAlignment = Alignment.Center
                    ) {

                        if (profileImageUri != null) {

                            AsyncImage(
                                model = profileImageUri,
                                contentDescription = null,
                                modifier = Modifier.fillMaxSize()
                            )

                        } else {

                            Icon(
                                imageVector = Icons.Outlined.Person,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f),
                                modifier = Modifier.size(40.dp)
                            )
                        }
                    }
                    Spacer(Modifier.height(10.dp))

                    Text(
                        "Tap to change photo",
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.45f),
                        style = MaterialTheme.typography.bodySmall
                    )


                    Spacer(Modifier.height(18.dp))

                    Text(
                        text = userName,
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onBackground
                    )

                    Spacer(Modifier.height(6.dp))

                    Text(
                        text = "user@email.com",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                    )
                }
            }

            Spacer(Modifier.height(24.dp))

            // 🎨 APPEARANCE
            SettingsSectionTitle("Appearance")

            SettingsSwitchItem(
                icon = Icons.Outlined.DarkMode,
                title = "Dark Theme",
                checked = darkThemeEnabled,
                onCheckedChange = {

                    darkThemeEnabled = it

                    ThemeState.isDarkTheme.value = it

                    SettingsPrefs.saveDarkTheme(
                        context,
                        it
                    )

                    Toast.makeText(
                        context,
                        if (it)
                            "Dark theme enabled"
                        else
                            "Light theme enabled",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            )

            Spacer(Modifier.height(24.dp))

            // 🔔 NOTIFICATIONS
            SettingsSectionTitle("Notifications")

            SettingsSwitchItem(
                icon = Icons.Outlined.Notifications,
                title = "Daily Reminders",
                checked = notificationsEnabled,
                onCheckedChange = {
                    notificationsEnabled = it

                    SettingsPrefs.saveDailyReminder(
                        context,
                        it
                    )

                    if (it) {
                        val hour = SettingsPrefs.getReminderHour(context)
                        val minute = SettingsPrefs.getReminderMinute(context)
                        NotificationScheduler.scheduleDailyReminder(context, hour, minute)
                        
                        val calendar = Calendar.getInstance().apply {
                            set(Calendar.HOUR_OF_DAY, hour)
                            set(Calendar.MINUTE, minute)
                            set(Calendar.SECOND, 0)
                            if (timeInMillis <= System.currentTimeMillis()) {
                                add(Calendar.DAY_OF_YEAR, 1)
                            }
                        }
                        
                        val sdf = SimpleDateFormat("MMM dd, hh:mm a", Locale.getDefault())
                        val dateString = sdf.format(calendar.time)
                        
                        Toast.makeText(
                            context,
                            "Reminder set! Next one: $dateString",
                            Toast.LENGTH_LONG
                        ).show()
                    } else {
                        NotificationScheduler.cancelDailyReminder(context)
                        Toast.makeText(
                            context,
                            "Daily reminders disabled",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            )

            Spacer(Modifier.height(24.dp))

            // 🧩 WIDGET SETTINGS
            SettingsSectionTitle("Widget")

            SettingsSwitchItem(
                icon = Icons.Outlined.GridView,
                title = "Compact Widget",
                checked = compactWidget,
                onCheckedChange = {
                    compactWidget = it

                    SettingsPrefs.saveCompactWidget(
                        context,
                        it
                    )

                    Toast.makeText(
                        context,
                        if (it)
                            "Compact widget enabled"
                        else
                            "Compact widget disabled",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            )

            SettingsSwitchItem(
                icon = Icons.Outlined.Visibility,
                title = "Transparent Widget",
                checked = transparentWidget,
                onCheckedChange = {
                    transparentWidget = it

                    SettingsPrefs.saveTransparentWidget(
                        context,
                        it
                    )

                    Toast.makeText(
                        context,
                        if (it)
                            "Transparent widget enabled"
                        else
                            "Transparent widget disabled",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            )

            Spacer(Modifier.height(24.dp))

            // 📦 DATA
            SettingsSectionTitle("Data")

            SettingsItem(
                icon = Icons.Outlined.Backup,
                title = "Backup Data",
                subtitle = "Save your habits securely",
                onClick = {
                    Toast.makeText(
                        context,
                        "Coming soon",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            )

            SettingsItem(
                icon = Icons.Outlined.RestartAlt,
                title = "Reset Progress",
                subtitle = "Clear streaks and history",
                onClick = {
                    Toast.makeText(
                        context,
                        "Coming soon",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            )

            Spacer(Modifier.height(24.dp))

            // 🔴 SIGN OUT
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {

                        navController.navigate(Routes.START) {
                            popUpTo(0)
                        }
                    },
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(
                    containerColor =
                        MaterialTheme.colorScheme.surface.copy(alpha = 0.75f)
                ),
                border = BorderStroke(
                    1.dp,
                    Color.Red.copy(alpha = 0.08f)
                )
            ) {

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement =
                        Arrangement.SpaceBetween
                ) {

                    Text(
                        "Sign Out",
                        color = Color.Red.copy(alpha = 0.9f),
                        style = MaterialTheme.typography.titleMedium
                    )

                    Icon(
                        imageVector = Icons.Outlined.ExitToApp,
                        contentDescription = null,
                        tint = Color.Red.copy(alpha = 0.9f)
                    )
                }
            }

            Spacer(Modifier.height(120.dp))
        }
    }
}

@Composable
fun SettingsSectionTitle(title: String) {

    Text(
        text = title,
        style = MaterialTheme.typography.titleMedium,
        color = MaterialTheme.colorScheme.onBackground,
        fontWeight = FontWeight.SemiBold
    )

    Spacer(Modifier.height(12.dp))
}

@Composable
fun SettingsItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit = {}
) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 12.dp)
            .clickable {
                onClick()
            },
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor =
                MaterialTheme.colorScheme.surface.copy(alpha = 0.75f)
        )
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f)
            )

            Spacer(Modifier.width(16.dp))

            Column {

                Text(
                    title,
                    color = MaterialTheme.colorScheme.onBackground,
                    style = MaterialTheme.typography.titleMedium
                )

                Spacer(Modifier.height(4.dp))

                Text(
                    subtitle,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

@Composable
fun SettingsSwitchItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 12.dp),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor =
                MaterialTheme.colorScheme.surface.copy(alpha = 0.75f)
        )
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement =
                Arrangement.SpaceBetween
        ) {

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {

                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f)
                )

                Spacer(Modifier.width(16.dp))

                Text(
                    title,
                    color = MaterialTheme.colorScheme.onBackground,
                    style = MaterialTheme.typography.titleMedium
                )
            }

            Switch(
                checked = checked,
                onCheckedChange = onCheckedChange
            )
        }
    }
}