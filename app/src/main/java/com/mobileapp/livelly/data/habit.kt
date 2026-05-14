package com.mobileapp.livelly.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Habit(
    val name: String,
    val streak: Int = 0,
    val lastCompleted: Long = 0L
)