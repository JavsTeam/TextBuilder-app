package com.example.textbuilder.ui.interaction

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.fragment.app.Fragment
import com.example.textbuilder.gen.TextBuilder
import com.example.textbuilder.R
import com.example.textbuilder.db.CardsDatabase
import com.example.textbuilder.db.CardEntity
import com.example.textbuilder.ui.UpdateListener
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class InteractionFragment : Fragment() {
    private var spinner: Spinner? = null
    private var lengthEditText: EditText? = null
    private var depthEditText: EditText? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_interaction, container, false)
        initSpinner(rootView.findViewById(R.id.interaction_fragment_spinner))
        initTextEditors(
            rootView.findViewById(R.id.interaction_fragment_edittext_length),
            rootView.findViewById(R.id.interaction_fragment_edittext_depth)
        )
        initButtonGenerate(rootView.findViewById(R.id.interaction_fragment_button_generate))

        return rootView
    }

    private fun hideKeyboard(focusedView: View, context: Context) {
        focusedView.clearFocus()
        val imm: InputMethodManager =
            context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(focusedView.windowToken, 0)
    }

    private fun initSpinner(spinner: Spinner) {
        this.spinner = spinner
        val sourceList = resources.getStringArray(R.array.source_list)
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
        lengthEditText.setOnFocusChangeListener { v, hasFocus ->
            if (!hasFocus) hideKeyboard(v, requireContext())
        }
        this.depthEditText = depthEditText
        depthEditText.setOnFocusChangeListener { v, hasFocus ->
            if (!hasFocus) hideKeyboard(v, requireContext())
        }
    }

    var updateListener: UpdateListener? = null

    // to allow activity pass command to update cards in ReadySourceFragment
    fun setListener(listener: UpdateListener) {
        updateListener = listener
    }

    private fun initButtonGenerate(generateButton: Button) {
        generateButton.setOnClickListener {
            val lengthStr = lengthEditText?.text.toString()
            val depthStr = depthEditText?.text.toString()
            val type = spinner?.selectedItemPosition
            val sources = resources.getStringArray(R.array.source_list)
            val text: String

            val length: Int
            val depth: Int

            if (lengthStr.isNotEmpty() && depthStr.isNotEmpty()) {
                length = lengthStr.toInt()
                depth = depthStr.toInt()
                if (type != 0) {
                    if (length > 0 && lengthStr.toInt() <= 1000) {
                        if (depth > 0 && depthStr.toInt() <= 3) { // input ok
                            when (type) {
                                1 -> {
                                    text = getTextFromServer(0, lengthStr.toInt(), depthStr.toInt())
                                    saveToDB(sources[1], text)
                                }
                                2 -> {
                                    text = getTextFromServer(1, lengthStr.toInt(), depthStr.toInt())
                                    saveToDB(sources[2], text)
                                }
                                3 -> {
                                    text = getTextFromServer(2, lengthStr.toInt(), depthStr.toInt())
                                    saveToDB(sources[3], text)
                                }
                                4 -> {
                                    text = getTextFromServer(3, lengthStr.toInt(), depthStr.toInt())
                                    saveToDB(sources[4], text)
                                }
                            }
                            updateListener?.onUpdate()
                        } else makeToast("Укажите глубину алгоритма от 1 до 3")

                    } else makeToast("Укажите длину текста от 0 до 1000")

                } else makeToast("Выберите исходный файл")

            } else makeToast("Поля не заполнены")
        }

        // I did it because android::focusableInTouchMode take tap to focus and doesn't count it as click
        // TODO:
        //  Known issue: when you press enter on keyboard while editing text fields it triggers focusChangeListener
        generateButton.setOnFocusChangeListener { v, hasFocus -> // seems like a crutch and might cause issues
            if (hasFocus) v.callOnClick()
        }
    }

    // TODO: No net database connection
    private fun getTextFromServer(sourceId: Int, length: Int, depth: Int): String {
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

    private val sourcesList = listOf(
        R.raw.jumoreski,
        R.raw.bugurts,
        R.raw.quotesmipt,
        R.raw.news
    )

    private fun getGeneratedText(sourceId: Int, length: Int, depth: Int): String {
        val textBuilder = TextBuilder(
            depth,
            requireContext(),
            sourcesList[sourceId]
        )
        return textBuilder.getText(length)
    }
}