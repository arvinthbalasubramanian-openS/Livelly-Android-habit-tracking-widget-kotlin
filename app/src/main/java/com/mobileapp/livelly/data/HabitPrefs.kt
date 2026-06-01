package com.mobileapp.livelly.data

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.time.LocalDate
import java.time.ZoneId
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

object HabitPrefs {

    private const val PREF = "habit_data"
    private const val KEY = "habit_list"

    private val _habitsFlow = MutableStateFlow<List<Habit>>(emptyList())
    val habitsFlow: StateFlow<List<Habit>> = _habitsFlow

    fun loadHabits(context: Context) {
        val prefs = context.getSharedPreferences(PREF, Context.MODE_PRIVATE)
        val json = prefs.getString(KEY, null)

        val habits = if (json != null) {
            val type = object : TypeToken<List<Habit>>() {}.type
            Gson().fromJson<List<Habit>>(json, type)
        } else emptyList()

        _habitsFlow.value = habits
    }

    fun saveHabits(context: Context, habits: List<Habit>) {
        val json = Gson().toJson(habits)
        val prefs = context.getSharedPreferences(PREF, Context.MODE_PRIVATE)
        prefs.edit().putString(KEY, json).apply()

        _habitsFlow.value = habits // 🔥 triggers UI update
    }
}