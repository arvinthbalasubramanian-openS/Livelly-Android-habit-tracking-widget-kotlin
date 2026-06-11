package com.mobileapp.livelly.widget

import android.content.Context
import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.Image
import androidx.glance.ImageProvider
import androidx.glance.action.clickable
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.action.actionStartActivity
import androidx.glance.appwidget.cornerRadius
import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.layout.Alignment
import androidx.glance.layout.Box
import androidx.glance.layout.Column
import androidx.glance.layout.ContentScale
import androidx.glance.layout.Spacer
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.height
import androidx.glance.layout.padding
import androidx.glance.layout.size
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextAlign
import androidx.glance.text.TextStyle
import androidx.glance.unit.ColorProvider
import com.mobileapp.livelly.MainActivity
import com.mobileapp.livelly.R
import com.mobileapp.livelly.data.Habit
import com.mobileapp.livelly.data.HabitPrefs
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

class Livelly : GlanceAppWidget() {

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        HabitPrefs.loadHabits(context.applicationContext)

        val habit =
            HabitPrefs.habitsFlow.value.firstOrNull()

        val state =
            habit.toBuddyState()

        provideContent {
            LivellyWidgetContent(
                state = state,
                launchIntent = Intent(context, MainActivity::class.java)
            )
        }
    }
}

private data class BuddyState(
    val imageRes: Int,
    val title: String,
    val subtitle: String,
    val backgroundColor: Color,
    val accentColor: Color
)

@Composable
private fun LivellyWidgetContent(
    state: BuddyState,
    launchIntent: Intent
) {
    Box(
        modifier = GlanceModifier
            .fillMaxSize()
            .background(ColorProvider(state.backgroundColor))
            .cornerRadius(24.dp)
            .clickable(actionStartActivity(launchIntent))
            .padding(12.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = GlanceModifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                provider = ImageProvider(state.imageRes),
                contentDescription = state.title,
                modifier = GlanceModifier.size(104.dp),
                contentScale = ContentScale.Fit
            )

            Spacer(GlanceModifier.height(8.dp))

            Text(
                text = state.title,
                style = TextStyle(
                    color = ColorProvider(Color.White),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                ),
                maxLines = 1
            )

            Spacer(GlanceModifier.height(4.dp))

            Text(
                text = state.subtitle,
                style = TextStyle(
                    color = ColorProvider(state.accentColor),
                    fontSize = 12.sp,
                    textAlign = TextAlign.Center
                ),
                maxLines = 2
            )
        }
    }
}

private fun Habit?.toBuddyState(): BuddyState {
    if (this == null) {
        return BuddyState(
            imageRes = R.drawable.widget_buddy_empty,
            title = "Ready to grow",
            subtitle = "Add a habit to wake your buddy.",
            backgroundColor = Color(0xFF111827),
            accentColor = Color(0xFFC7D2FE)
        )
    }

    val lastCompletedDate =
        if (lastCompleted > 0L)
            Instant
                .ofEpochMilli(lastCompleted)
                .atZone(ZoneId.systemDefault())
                .toLocalDate()
        else
            null

    val today = LocalDate.now()
    val isCompletedToday = lastCompletedDate == today
    val missedDays =
        if (lastCompletedDate == null)
            Int.MAX_VALUE
        else
            today.toEpochDay().minus(lastCompletedDate.toEpochDay()).toInt()

    return when {
        streak <= 0 || missedDays > 1 ->
            BuddyState(
                imageRes = R.drawable.widget_buddy_sad,
                title = "Needs care",
                subtitle = "$name is waiting for a check-in.",
                backgroundColor = Color(0xFF1F2937),
                accentColor = Color(0xFFFCA5A5)
            )

        isCompletedToday && streak >= target ->
            BuddyState(
                imageRes = R.drawable.widget_buddy_flourish,
                title = "Flourishing",
                subtitle = "$streak days. This habit has roots.",
                backgroundColor = Color(0xFF14532D),
                accentColor = Color(0xFFBBF7D0)
            )

        isCompletedToday && streak >= 7 ->
            BuddyState(
                imageRes = R.drawable.widget_buddy_happy,
                title = "Growing strong",
                subtitle = "$streak days of consistency.",
                backgroundColor = Color(0xFF166534),
                accentColor = Color(0xFFDCFCE7)
            )

        isCompletedToday ->
            BuddyState(
                imageRes = R.drawable.widget_buddy_growing,
                title = "Happy today",
                subtitle = "$name is complete for today.",
                backgroundColor = Color(0xFF312E81),
                accentColor = Color(0xFFC7D2FE)
            )

        else ->
            BuddyState(
                imageRes = R.drawable.widget_buddy_growing,
                title = "Keep it alive",
                subtitle = "$streak day streak. Check in today.",
                backgroundColor = Color(0xFF374151),
                accentColor = Color(0xFFFDE68A)
            )
    }
}
