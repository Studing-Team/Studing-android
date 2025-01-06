package com.team.studing.Utils

import android.content.Context
import android.content.SharedPreferences

class PreferenceUtil(context: Context) {

    private val preferences: SharedPreferences =
        context.getSharedPreferences("prefs_name", Context.MODE_PRIVATE)

    fun setFCMToken(token: String) {
        preferences.edit().putString("FCM_TOKEN", token).apply()
    }

    fun getFCMToken(): String? =
        preferences.getString("FCM_TOKEN", null)

    fun setRole(token: String) {
        preferences.edit().putString("ROLE", token).apply()
    }

    fun getRole(): String? =
        preferences.getString("ROLE", "ROLE_UNUSER")
}