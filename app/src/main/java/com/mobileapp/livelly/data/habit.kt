package com.mobileapp.livelly.data

import java.util.UUID

data class Habit(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val streak: Int = 0,
    val lastCompleted: Long = 0L
)