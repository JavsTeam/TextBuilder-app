package com.example.textbuilder.ui.paste

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import com.example.textbuilder.R
import com.example.textbuilder.service.Misc

class PasteFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_paste, container, false)

        initSaveButton(rootView.findViewById(R.id.paste_fragment_button_save))

        val pasteEditText: EditText = rootView.findViewById(R.id.paste_fragment_edit_text)

        return rootView
    }

    private fun initSaveButton(saveButton: Button) {
        saveButton.setOnClickListener {
            Misc.makeToast(requireContext(), "Функционал еще не реализован")
        }
    }
}