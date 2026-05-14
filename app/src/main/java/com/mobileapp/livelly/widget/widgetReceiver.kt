package com.mobileapp.livelly.widget;

import androidx.glance.appwidget.GlanceAppWidgetReceiver

class LivingWidgetReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget = Livelly()
}