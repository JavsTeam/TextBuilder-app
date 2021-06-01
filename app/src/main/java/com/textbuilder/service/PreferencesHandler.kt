package com.textbuilder.service

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import com.textbuilder.ui.MainActivity

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
        addToSourceTag(fileTag)
        editor.putString(fileTag, fileName).apply()
    }

    fun saveFileTag(fileTag: String) {
        return saveFileTag(
            fileTag,
            FileHandler.encodeFileTag(fileTag)
        )
    }

    fun createEmptySetIfNecessary() {
        if(!preferences.contains(listTag)) {
            preferences.edit()
                .putStringSet(listTag, HashSet<String>())
                .apply()
        }
    }

    fun clearPreferences(context: Context) {
        preferences.getStringSet(listTag, null)?.forEach {
            FileHandler.deleteFile(context, FileHandler.encodeFileTag(it))
        }

        preferences.edit()
            .clear()
            .putStringSet(listTag ,HashSet<String>())
            .apply()
    }

    fun deleteFile(tagToDelete: String) {
        val editor = preferences.edit()
        editor.remove(tagToDelete).apply()
        deleteFromSourceTag(tagToDelete)
    }

    private fun deleteFromSourceTag(tagToDelete: String) {
        val editor = preferences.edit()
        // I don't know why, but if you try to write initial set data disappears ofter runtime
        val newSet = HashSet<String>(preferences.getStringSet(listTag, null))
        newSet.remove(tagToDelete)
        editor.putStringSet(listTag, newSet).apply()
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

    private fun addToSourceTag(tagToAdd: String) {
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