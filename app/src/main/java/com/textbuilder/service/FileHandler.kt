package com.textbuilder.service

import android.app.Activity
import android.content.Context
import android.net.Uri
import java.io.FileNotFoundException
import java.math.BigInteger
import java.security.MessageDigest
import java.util.*

class FileHandler {
    companion object {
        fun addNewFileWithTextAndSaveTag(
            preferencesHandler: PreferencesHandler,
            context: Context,
            fileTag: String,
            text: String
        ) {
            if (isFileTagUnique(fileTag, preferencesHandler)) {
                preferencesHandler.saveFileTag(fileTag)
                saveTextToFile(
                    context,
                    encodeFileTag(fileTag),
                    text
                )
            }
        }

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
            Logger.d("Trying to delete file $fileName \nResult: ${if (context.deleteFile(fileName)) "OK" else "ERROR"}")
        }

        fun isTagsSame(fileTag: String, encodedName: String): Boolean {
            return encodeFileTag(fileTag) == encodedName
        }

        fun isFileTagUnique(fileTag: String, preferencesHandler: PreferencesHandler): Boolean {
            return !preferencesHandler.isKeyPresent(fileTag)
        }
    }
}