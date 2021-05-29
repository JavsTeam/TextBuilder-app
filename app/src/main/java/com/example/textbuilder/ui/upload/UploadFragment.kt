package com.example.textbuilder.ui.upload

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.textbuilder.R

class UploadFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_upload, container, false)

        val button: Button = rootView.findViewById(R.id.upload_fragment_button_upload)
        button.setOnClickListener {

        }

        return rootView
    }
}