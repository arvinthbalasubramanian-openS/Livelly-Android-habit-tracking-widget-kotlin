package com.mobileapp.livelly.data

import android.content.Context

object ProfilePrefs {

    private const val PREFS_NAME =
        "profile_prefs"

    private const val PROFILE_IMAGE_URI =
        "profile_image_uri"

    fun saveProfileImage(
        context: Context,
        uri: String
    ) {

        context.getSharedPreferences(
            PREFS_NAME,
            Context.MODE_PRIVATE
        )
            .edit()
            .putString(PROFILE_IMAGE_URI, uri)
            .apply()
    }

    fun getProfileImage(
        context: Context
    ): String? {

        return context
            .getSharedPreferences(
                PREFS_NAME,
                Context.MODE_PRIVATE
            )
            .getString(PROFILE_IMAGE_URI, null)
    }
}