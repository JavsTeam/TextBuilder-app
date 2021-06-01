package com.textbuilder.ui.upload

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.textbuilder.R
import com.textbuilder.service.FileHandler
import com.textbuilder.service.FileHandler.Companion.isFileTagUnique
import com.textbuilder.service.Misc
import com.textbuilder.service.PreferencesHandler
import com.textbuilder.ui.upload.dialog.DeleteByTagDialogFragment


class UploadFragment : Fragment() {
    private var fileTagEditText: EditText? = null
    private var preferencesHandler: PreferencesHandler? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_upload, container, false)

        initUploadButton(rootView.findViewById(R.id.upload_fragment_button_upload))
        initDeleteButton(rootView.findViewById(R.id.upload_fragment_button_delete))
        initFileTagEditText(rootView.findViewById(R.id.upload_fragment_edittext_filetag))

        return rootView
    }

    private fun initDeleteButton(deleteButton: ImageButton) {
        val buttonScaleAnimation: Animation =
            AnimationUtils.loadAnimation(requireContext(), R.anim.button_scale)
        deleteButton.setOnClickListener {
            it.startAnimation(buttonScaleAnimation)
            val myDialogFragment = DeleteByTagDialogFragment()
            val manager = requireActivity().supportFragmentManager

            myDialogFragment.show(manager, "DeleteByTagDialog")
        }
    }

    private fun initFileTagEditText(fileNameEditText: EditText) {
        this.fileTagEditText = fileNameEditText
    }

    private val REQUEST_TEXT_GET = 1

    private fun initUploadButton(uploadButton: Button) {
        uploadButton.setOnClickListener {
            preferencesHandler = PreferencesHandler(requireActivity())
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
                fileTagEditText?.setText("")
                val fileName = FileHandler.encodeFileTag(fileTag)
                val text = FileHandler.getTextFromFileByUri(documentUri, requireActivity())

                preferencesHandler?.saveFileTag(fileTag, fileName)
                FileHandler.saveTextToFile(context, fileName, text)
                Toast.makeText(context, "Исходный файл добавлен", Toast.LENGTH_SHORT)
                    .show()
            } else {
                Toast.makeText(context, "Введите другое название для исходника", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    // TODO: update restrictions for filename
    private fun getFileTag(): String {
        val fileTag = fileTagEditText?.text.toString()
        if (!isFileTagUnique(fileTag, preferencesHandler!!)) {
            Toast.makeText(requireContext(), "Файл перезаписан", Toast.LENGTH_SHORT).show()
        }
        if (fileTag.isNotEmpty()) {
            return fileTag
        }
        return ""
    }

    private fun makeToast(text: String) {
        Toast.makeText(requireContext(), text, Toast.LENGTH_SHORT).show()
    }

}