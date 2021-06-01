package com.textbuilder.ui.upload.dialog

import android.app.Dialog
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.textbuilder.service.PreferencesHandler

class DeleteByTagDialogFragment : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val tagList = PreferencesHandler(requireActivity()).getSourceTagList().toTypedArray()
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.setTitle("Выберите файл для удаления")
                .setItems(
                    tagList
                ) { dialog, index ->
                    Toast.makeText(
                        activity, "Удаляем файл ${tagList[index]}",
                        Toast.LENGTH_SHORT
                    ).show()

                    PreferencesHandler(requireActivity()).deleteFile(tagList[index])
                }
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}