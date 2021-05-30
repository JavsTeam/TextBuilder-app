package com.example.textbuilder.service

import android.content.Context
import android.content.SharedPreferences
import java.math.BigInteger
import java.security.MessageDigest

class FileHandler {
    companion object {
        fun encodeFileTag(fileTag: String): String {
            val md = MessageDigest.getInstance("MD5")
            return BigInteger(1, md.digest(fileTag.toByteArray()))
                .toString(16)
                .padStart(32, '0')
        }

        fun isTagsSame(fileTag: String, encodedName: String): Boolean {
            return encodeFileTag(fileTag) == encodedName
        }

        fun getText(context: Context, fileName: String) : String {
            val text = StringBuilder()
            context.openFileInput(fileName).bufferedReader().useLines { lines ->
                lines.forEach {
                    text.append(it).append("\n")
                }
            }
            return text.toString()
        }

        fun isFileTagUnique(fileTag: String, preferencesHandler: PreferencesHandler): Boolean {
           return !preferencesHandler.isKeyPresent(fileTag)
        }
    }
}