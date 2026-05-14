package com.mobileapp.livelly.logic

import com.mobileapp.livelly.data.Habit

fun isCompletedToday(lastCompleted: Long): Boolean {
    val today = System.currentTimeMillis()
    val oneDay = 24 * 60 * 60 * 1000
    return today - lastCompleted < oneDay
}

fun updateHabit(habit: Habit): Habit {

    val today = System.currentTimeMillis()
    val oneDay = 24 * 60 * 60 * 1000

    val isNextDay = today - habit.lastCompleted in oneDay..(2 * oneDay)

    return if (isNextDay) {
        habit.copy(
            streak = habit.streak + 1,
            lastCompleted = today
        )
    } else {
        habit.copy(
            streak = 1,
            lastCompleted = today
        )
    }
}