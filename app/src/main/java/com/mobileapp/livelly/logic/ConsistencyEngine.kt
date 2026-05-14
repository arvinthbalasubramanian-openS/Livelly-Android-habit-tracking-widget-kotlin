package com.mobileapp.livelly.logic;

fun getWorldState(streak: Int): String {
    return when {
        streak == 0 -> "🌵 Dead"
        streak < 3 -> "🌱 Growing"
        streak < 7 -> "🌿 Healthy"
        else -> "🌳 Flourishing"
    }
}