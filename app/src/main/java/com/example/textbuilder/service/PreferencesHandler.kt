package com.example.textbuilder.service

import android.content.SharedPreferences
import com.example.textbuilder.ui.MainActivity
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class PreferencesHandler(private val preferences: SharedPreferences) {
    fun readFileName(fileTag: String): String {
        val fileName = preferences.getString(fileTag, "")
        return fileName ?: ""
    }

    private fun addToSourceTags(tagToAdd: String, preferences: SharedPreferences) {
        val listTag = MainActivity.SOURCE_TAGS_LIST_TAG
        val editor = preferences.edit()
        // I don't know why, but if you try to write initial set data disappears ofter runtime
        val newSet = HashSet<String>(preferences.getStringSet(listTag, null))
        newSet.add(tagToAdd)
        editor.putStringSet(listTag, newSet).apply()
    }

    fun saveFileTag(fileTag: String, fileName: String) {
        val editor = preferences.edit()
        addToSourceTags(fileTag, preferences)
        editor.putString(fileTag, fileName).apply()
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

    fun logState() {
        preferences.all.entries.forEach {
            Logger.d(it.toString())
        }
    }
}