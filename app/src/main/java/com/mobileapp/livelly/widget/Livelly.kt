package com.mobileapp.livelly.widget;

import android.content.Context
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.provideContent
import androidx.glance.text.Text
import com.mobileapp.livelly.data.HabitRepository
import com.mobileapp.livelly.logic.getWorldState

class Livelly : GlanceAppWidget() {

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        provideContent {
            val state = getWorldState(HabitRepository.streak)
            Text(text = state, modifier = GlanceModifier)
        }
    }


}