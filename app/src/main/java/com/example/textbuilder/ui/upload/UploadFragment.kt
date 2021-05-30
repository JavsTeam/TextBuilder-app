package com.example.textbuilder.ui.upload

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.textbuilder.R
import com.example.textbuilder.service.FileNameConverter
import com.example.textbuilder.service.PreferencesHandler
import java.io.FileNotFoundException
import java.math.BigInteger
import java.security.MessageDigest
import java.util.*


class UploadFragment : Fragment() {
    private var fileNameEditText: EditText? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_upload, container, false)

        initUploadButton(rootView.findViewById(R.id.upload_fragment_button_upload))
        initFileTagEditText(rootView.findViewById(R.id.upload_fragment_edittext_filetag))

        return rootView
    }

    private fun initFileTagEditText(fileNameEditText: EditText) {
        this.fileNameEditText = fileNameEditText
    }

    private val REQUEST_TEXT_GET = 1

    private fun initUploadButton(uploadButton: Button) {
        uploadButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
                type = "*/*"
            }
            intent.putExtra(Intent.EXTRA_MIME_TYPES, arrayOf("text/plain", "application/zip"))
            if (intent.resolveActivity(requireActivity().packageManager) != null) {
                startActivityForResult(intent, REQUEST_TEXT_GET)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_TEXT_GET && resultCode == Activity.RESULT_OK) {
            val context = requireContext()
            val documentUri: Uri? = data?.data
            val fileTag = getFileTag()
            if (documentUri != null && fileTag.isNotEmpty()) {
                val fileName = FileNameConverter.encodeFileTag(fileTag)
                val text = getTextFromTxtByUri(documentUri)
                PreferencesHandler.saveFileTagInPreferences(requireActivity(), fileTag, fileName)
                context.openFileOutput(fileName, Context.MODE_PRIVATE).use {
                    it.write(text.toByteArray())
                }
            } else {
                Toast.makeText(context, "Введите другое название для исходника", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }


    private fun read(fileTag: String) {
        val preferences = requireActivity().getPreferences(Activity.MODE_PRIVATE)
        val savedFileName = preferences.getString(fileTag, "")
        println(savedFileName)
        requireContext().openFileInput(savedFileName).bufferedReader().useLines { lines ->
            lines.forEach {
                println(it)
            }
        }
    }

    private fun getTextFromTxtByUri(txtUri: Uri): String {
        var result = ""
        try {
            val inputStream = requireActivity().contentResolver.openInputStream(txtUri)
            val s: Scanner = Scanner(inputStream).useDelimiter("\\A")
            result = if (s.hasNext()) s.next() else ""
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }
        return result
    }

    // TODO: update restrictions for filename
    private fun getFileTag(): String {
        val fileTag = fileNameEditText?.text.toString()
        if (fileTag.isNotEmpty() && isFileTagUnique(fileTag)) {
            return fileTag
        }
        return ""
    }

    private fun isFileTagUnique(fileTag: String): Boolean {

    }
}