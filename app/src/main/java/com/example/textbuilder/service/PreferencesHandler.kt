package com.example.textbuilder.service

import android.app.Activity
import android.content.SharedPreferences
import com.example.textbuilder.ui.MainActivity

class PreferencesHandler(
    private val activity: Activity,
    private val preferences: SharedPreferences = activity.getPreferences(Activity.MODE_PRIVATE)
) {
    private val listTag = MainActivity.SOURCE_TAGS_LIST_TAG

    fun readFileName(fileTag: String): String {
        val fileName = preferences.getString(fileTag, "")
        return fileName ?: ""
    }

    fun saveFileTag(fileTag: String, fileName: String) {
        val editor = preferences.edit()
        addToSourceTag(fileTag, preferences)
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

    fun getSourceTagList(): ArrayList<String> {
        val set = preferences.getStringSet(listTag, null)
        val sourceTagList = ArrayList<String>()
        sourceTagList.addAll(set as Collection<String>)
        return sourceTagList
    }

    private fun addToSourceTag(tagToAdd: String, preferences: SharedPreferences) {
        val editor = preferences.edit()
        // I don't know why, but if you try to write initial set data disappears ofter runtime
        val newSet = HashSet<String>(preferences.getStringSet(listTag, null))
        newSet.add(tagToAdd)
        editor.putStringSet(listTag, newSet).apply()
    }

    fun logState() {
        preferences.all.entries.forEach {
            Logger.d(it.toString())
        }
    }
}