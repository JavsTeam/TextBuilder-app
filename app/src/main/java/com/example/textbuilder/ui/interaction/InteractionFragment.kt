package com.example.textbuilder.ui.interaction

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.fragment.app.Fragment
import com.example.textbuilder.R
import com.example.textbuilder.db.CardsDatabase
import com.example.textbuilder.db.CardsEntity
import com.example.textbuilder.ui.UpdateListener
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlin.random.Random


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
        initButtonGenerate(rootView.findViewById(R.id.interaction_fragment_button_generate))

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
        /*
        GlobalScope.launch {
            val db = CardsDatabase(requireContext())
            db.cardsDao().nukeTable()
        }
*/
        generateButton.setOnClickListener {
            val lengthStr = lengthEditText?.text.toString()
            val depthStr = depthEditText?.text.toString()
            val type = spinner?.selectedItemPosition
            val sources = resources.getStringArray(R.array.source_list)
            var text: String

            //text = getTextFromServer(1, 30, 2)
            //saveToDB(text)
/*
            GlobalScope.launch {
                val db = CardsDatabase(requireContext())
                val DBdata = db.cardsDao().getAll()
                DBdata.forEach {
                    Log.d("Debug", it.toString())
                }
            }
*/
            var length: Int = 0
            var depth: Int = 0
            if (lengthStr.isNotEmpty() && depthStr.isNotEmpty()) {
                length = lengthStr.toInt()
                depth = depthStr.toInt()
                if (length > 0 && depth > 0
                    && lengthStr.toInt() <= 1000 && depthStr.toInt() <= 10
                    && type != 0
                ) { // input ok
                    when (type) {
                        // TODO: no realization yet
                        1 -> {
                            makeToast(sources[1])
                            text = getTextFromServer(1, lengthStr.toInt(), depthStr.toInt())
                            saveToDB(text)
                        }
                        2 -> {
                            makeToast(sources[2])
                            text = getTextFromServer(2, lengthStr.toInt(), depthStr.toInt())
                            saveToDB(text)
                        }
                        3 -> {
                            makeToast(sources[3])
                            text = getTextFromServer(3, lengthStr.toInt(), depthStr.toInt())
                            saveToDB(text)
                        }
                        4 -> {
                            makeToast(sources[4])
                            text = getTextFromServer(4, lengthStr.toInt(), depthStr.toInt())
                            saveToDB(text)
                        }
                    }
                    Log.d("Debug", "onClick()")
                    updateListener?.onUpdate()
                }
            } else {
                makeToast("Поля не заполнены")
            }
        }

        // did it because android::focusableInTouchMode take tap to focus and doesn't count it as click
        generateButton.setOnFocusChangeListener { v, hasFocus -> // seems like a crutch and might caouse issues
            if (hasFocus) v.callOnClick()
        }
    }

    // TODO: Net database connection
    private fun getTextFromServer(sourceId: Int, length: Int, depth: Int): String {
        return getRandomText(Random.nextInt(0, 20))
    }

    private fun saveToDB(text: String) {
        val db = CardsDatabase(requireContext())
        GlobalScope.launch {
            // db.cardsDao().nukeTable()
            db.cardsDao().insert(CardsEntity(0, "title", text, false))
            db.cardsDao().deleteOverLimit()
            /*
            val DBdata = db.cardsDao().getAll()
            DBdata.forEach {
                Log.d("Debug", it.toString())
            }
           */
        }
    }

    private fun makeToast(text: String) {
        Toast.makeText(requireContext(), text, Toast.LENGTH_SHORT).show()
    }

    // all following is for deleting

    private fun getRandBool(): Boolean {
        return Random.nextBoolean()
    }

    private fun getRandomText(seed: Int): String {
        val result = StringBuilder()
        repeat((0..(20 - seed)).count()) {
            (0..seed * 5).forEach { i ->
                result.append(
                    (Random(i * it).nextInt() % 100 + 20).toChar()
                )
            }
        }
        return result.toString()
    }
}