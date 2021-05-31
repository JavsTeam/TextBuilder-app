package com.example.textbuilder.ui.paste

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import com.example.textbuilder.R
import com.example.textbuilder.service.FileHandler
import com.example.textbuilder.service.Misc
import com.example.textbuilder.service.PreferencesHandler

class ParserFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_parser, container, false)

        init(rootView)

        return rootView
    }

    private fun init(rootView: View) {
        val tagEditText: EditText = rootView.findViewById(R.id.parser_fragment_edittext_filetag)
        val linkEditText: EditText = rootView.findViewById(R.id.parser_fragment_edittext_link)
        val amountEditText: EditText = rootView.findViewById(R.id.parser_fragment_edittext_amount)
        val saveButton: Button = rootView.findViewById(R.id.parser_fragment_button_save)

        saveButton.setOnClickListener {
            val tag = tagEditText.text.toString()
            val link = tagEditText.text.toString()
            val amount = tagEditText.text.toString()

            if (tag.isNotEmpty()) {
                if (link.isNotEmpty()) {
                    if (amount.isNotEmpty()) {
                        val text = getParsedText(link, amount.toInt())
                        FileHandler.addNewFileWithTextAndSaveTag(
                            PreferencesHandler(requireActivity()),
                            requireContext(),
                            tag,
                            text
                        )
                    }
                    Misc.makeToast(requireContext(), "Введите количество постов для парсинга")
                }
                Misc.makeToast(requireContext(), "Введите ссылку на сообщество")
            }
            Misc.makeToast(requireContext(), "Введите название файла")


            Misc.makeToast(requireContext(), "Функционал еще не реализован")
        }
    }

    private fun getParsedText(link: String, toInt: Int): String {
        TODO("Not yet implemented")
    }
}