package com.example.textbuilder.ui.interaction

import android.app.Activity
import android.content.Context
import android.content.res.Resources
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.fragment.app.Fragment
import com.example.textbuilder.R


class InteractionFragment : Fragment() {

    var spinner: Spinner? = null
    var lengthEditText: EditText? = null
    var depthEditText: EditText? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //return super.onCreateView(inflater, container, savedInstanceState)
        val rootView = inflater.inflate(R.layout.fragment_interaction, container, false)
        initSpinner(rootView.findViewById(R.id.interaction_fragment_spinner))
        initTextEditors(
            rootView.findViewById(R.id.interaction_fragment_edittext_length),
            rootView.findViewById(R.id.interaction_fragment_edittext_depth)
        )
        initButton(rootView.findViewById(R.id.interaction_fragment_button_generate))

        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    private fun hideKeyboard(focusedView: View, context: Context) {
        focusedView.clearFocus()
        val imm: InputMethodManager =
            context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(focusedView.windowToken, 0)

        //((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(myEditText.getWindowToken(), 0);
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

        spinner.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?, // wtf its null
                position: Int,
                id: Long
            ) {

            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // write code to perform some action
            }
        }
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

    private fun initButton(generateButton: Button) {
        generateButton.setOnClickListener {
            val length = lengthEditText?.text.toString()
            val depth = depthEditText?.text.toString()
            val type = spinner?.selectedItemPosition
            val sources = resources.getStringArray(R.array.source_list)

            if (length.isNotEmpty() && depth.isNotEmpty()
                && length.toInt() > 0 && depth.toInt() > 0
                && length.toInt() <= 1000 && depth.toInt() <= 10
            ) { // input ok
                when (type) {
                    // TODO: no realization yet
                    1 -> makeToast(sources[1])
                    2 -> makeToast(sources[2])
                    3 -> makeToast(sources[3])
                    4 -> makeToast(sources[4])
                }
            } else {
                makeToast("Поля не заполнены")
            }
        }
    }

    private fun makeToast(text: String) {
        Toast.makeText(requireContext(), text, Toast.LENGTH_SHORT).show()
    }
}