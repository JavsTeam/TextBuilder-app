package com.example.textbuilder.service

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.net.Uri
import java.io.FileNotFoundException
import java.math.BigInteger
import java.security.MessageDigest
import java.util.*

class FileHandler {
    companion object {
        fun saveTextToFile(context: Context, fileName: String, text: String) {
            context.openFileOutput(fileName, Context.MODE_PRIVATE).use {
                it.write(text.toByteArray())
            }
        }

        fun getTextFromFileByUri(txtUri: Uri, activity: Activity): String {
            var result = ""
            try {
                val inputStream = activity.contentResolver.openInputStream(txtUri)
                val s: Scanner = Scanner(inputStream).useDelimiter("\\A")
                result = if (s.hasNext()) s.next() else ""
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
            }
            return result
        }

        fun getTextFromFile(context: Context, fileName: String): String {
            val text = StringBuilder()
            context.openFileInput(fileName).bufferedReader().useLines { lines ->
                lines.forEach {
                    text.append(it).append("\n")
                }
            }
            return text.toString()
        }

        fun encodeFileTag(fileTag: String): String {
            val md = MessageDigest.getInstance("MD5")
            return BigInteger(1, md.digest(fileTag.toByteArray()))
                .toString(16)
                .padStart(32, '0')
        }

        fun deleteFile(context: Context, fileName: String) {
            context.deleteFile(fileName)
        }

        fun isTagsSame(fileTag: String, encodedName: String): Boolean {
            return encodeFileTag(fileTag) == encodedName
        }

        fun isFileTagUnique(fileTag: String, preferencesHandler: PreferencesHandler): Boolean {
            return !preferencesHandler.isKeyPresent(fileTag)
        }
    }
}