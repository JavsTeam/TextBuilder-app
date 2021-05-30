package com.example.textbuilder.service

import android.app.Activity
import android.content.SharedPreferences

class PreferencesHandler(private val preferences: SharedPreferences) {
    fun readFileName(fileTag: String): String {
        val fileName = preferences.getString(fileTag, "")
        return fileName ?: ""
    }

    fun saveFileTag(fileTag: String, fileName: String) {
        val editor = preferences.edit()
        editor.putString(fileTag, fileName)
        editor.apply()
    }

    fun saveFileTag(fileTag: String) {
        return saveFileTag(
            fileTag,
            FileHandler.encodeFileTag(fileTag)
        )
    }

    fun isKeyPresent(key: String): Boolean {
        return preferences.getString(key, "") != ""
    }
}