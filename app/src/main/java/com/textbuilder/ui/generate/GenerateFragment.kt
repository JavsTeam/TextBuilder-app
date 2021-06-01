package com.textbuilder.ui.generate

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.fragment.app.Fragment
import com.textbuilder.gen.TextBuilder
import com.example.textbuilder.R
import com.textbuilder.db.CardsDatabase
import com.textbuilder.db.CardEntity
import com.textbuilder.service.FileHandler
import com.textbuilder.service.PreferencesHandler
import com.textbuilder.ui.UpdateListener
import com.textbuilder.ui.upload.dialog.DeleteByTagDialogFragment
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

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

    // to allow activity pass command to update cards in ReadySourceFragment
    fun setListener(listener: UpdateListener) {
        updateListener = listener
    }

    private fun initButtonGenerate(generateButton: Button) {
        generateButton.setOnClickListener {
            hideKeyboard()
            val lengthStr = lengthEditText?.text.toString()
            val depthStr = depthEditText?.text.toString()
            val type = spinner?.selectedItemPosition

            val length: Int = if (lengthStr.isEmpty()) 10 else lengthStr.toInt()
            val depth: Int = if (depthStr.isEmpty()) 1 else depthStr.toInt()

            if (length in 1..1000) {
                if (depth in 1..3) { // input ok
                    handleTextGeneration(type!!, length, depth)
                    updateListener?.onUpdate()
                } else makeToast("Укажите глубину алгоритма от 1 до 3")
            } else makeToast("Укажите длину текста от 0 до 1000")

        }
    }

    private fun handleTextGeneration(sourceId: Int, length: Int, depth: Int) {
        val sourcesList = PreferencesHandler(requireActivity()).getSourceTagList()
        val text = getText(sourceId, length, depth)
        saveToDB(sourcesList[sourceId], text)
    }

    private fun hideKeyboard() {
        val focusedView: View? = requireActivity().currentFocus
        focusedView?.clearFocus()
        val imm: InputMethodManager =
            requireContext().getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(focusedView?.windowToken, 0)
    }

    // TODO: No net database connection
    private fun getText(sourceId: Int, length: Int, depth: Int): String {
        return getGeneratedText(sourceId, length, depth)
    }

    private fun saveToDB(tag: String, text: String) {
        val db = CardsDatabase(requireContext())
        GlobalScope.launch {
            db.cardsDao().insert(CardEntity(0, tag, text, false))
            db.cardsDao().deleteOverLimit()
        }
    }

    private fun makeToast(text: String) {
        Toast.makeText(requireContext(), text, Toast.LENGTH_SHORT).show()
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
}