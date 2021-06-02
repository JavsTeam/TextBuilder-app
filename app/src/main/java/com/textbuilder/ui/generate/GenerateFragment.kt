package com.textbuilder.ui.generate

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import androidx.fragment.app.Fragment
import com.example.textbuilder.R
import com.textbuilder.db.CardEntity
import com.textbuilder.db.CardsDatabase
import com.textbuilder.db.providers.CardHandler
import com.textbuilder.gen.TextBuilder
import com.textbuilder.service.FileHandler
import com.textbuilder.service.Misc
import com.textbuilder.service.PreferencesHandler
import com.textbuilder.ui.UpdateListener

class GenerateFragment : Fragment() {
    private var spinner: Spinner? = null
    private var lengthEditText: EditText? = null
    private var depthEditText: EditText? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_generate, container, false)
        initSpinner(rootView.findViewById(R.id.generate_fragment_spinner))
        initTextEditors(
            rootView.findViewById(R.id.generate_fragment_edittext_length),
            rootView.findViewById(R.id.generate_fragment_edittext_depth)
        )
        initButtonGenerate(rootView.findViewById(R.id.generate_fragment_button_generate))

        return rootView
    }

    private fun initSpinner(spinner: Spinner) {
        val sourceList = PreferencesHandler(requireActivity()).getSourceTagList()
        this.spinner = spinner
        val adapter = ArrayAdapter(
            requireContext(),
            R.layout.interaction_spinner_item,
            sourceList
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.adapter = adapter
    }

    private fun initTextEditors(lengthEditText: EditText, depthEditText: EditText) {
        this.lengthEditText = lengthEditText
        this.depthEditText = depthEditText
    }

    private var updateListener: UpdateListener? = null

    private fun initButtonGenerate(generateButton: Button) {
        generateButton.setOnClickListener {
            Misc.hideKeyboard(requireActivity())
            val lengthStr = lengthEditText?.text.toString()
            val depthStr = depthEditText?.text.toString()
            val type = spinner?.selectedItemPosition

            val length: Int = if (lengthStr.isEmpty()) 10 else lengthStr.toInt()
            val depth: Int = if (depthStr.isEmpty()) 1 else depthStr.toInt()

            if (length in 1..1000) {
                if (depth in 1..3) { // input ok
                    handleTextGeneration(type!!, length, depth)
                    updateListener?.onUpdate()
                } else Misc.toast(requireContext(), "Укажите глубину алгоритма от 1 до 3")
            } else Misc.toast(requireContext(), "Укажите длину текста от 0 до 1000")

        }
    }

    private fun handleTextGeneration(sourceId: Int, length: Int, depth: Int) {
        val sourcesList = PreferencesHandler(requireActivity()).getSourceTagList()
        val text = getGeneratedText(sourceId, length, depth)
        saveToDB(sourcesList[sourceId], text)
    }

    private fun saveToDB(tag: String, text: String) {
        val handler = CardHandler(CardsDatabase(requireContext()))
        handler.addCard(CardEntity(0, tag, text, false))
        handler.deleteOverLimit()
    }

    private fun getGeneratedText(sourceId: Int, length: Int, depth: Int): String {
        val sourcesList = PreferencesHandler(requireActivity()).getSourceTagList()
        val sourceText = FileHandler.getTextFromFile(
            requireContext(),
            FileHandler.encodeFileTag(sourcesList[sourceId])
        )

        val textBuilder = TextBuilder(
            depth,
            requireContext(),
            sourceText
        )
        return textBuilder.getText(length)
    }

    // To allow activity pass commands to update cards
    fun setListener(listener: UpdateListener) {
        updateListener = listener
    }
}