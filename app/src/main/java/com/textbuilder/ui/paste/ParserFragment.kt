package com.textbuilder.ui.paste

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import com.chaquo.python.PyException
import com.chaquo.python.Python
import com.chaquo.python.android.AndroidPlatform
import com.example.textbuilder.R
import com.textbuilder.service.FileHandler
import com.textbuilder.service.Logger
import com.textbuilder.service.Misc
import com.textbuilder.service.PreferencesHandler

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
            val link = linkEditText.text.toString()
            val amount = amountEditText.text.toString()

            if (tag.isNotEmpty()) {
                if (link.isNotEmpty()) {
                    if (amount.isNotEmpty()) {
                        try {
                            val text = getParsedText(link, amount.toInt())
                            FileHandler.addNewFileWithTextAndSaveTag(
                                PreferencesHandler(requireActivity()),
                                requireContext(),
                                tag,
                                text
                            )
                            tagEditText.setText("")
                            linkEditText.setText("")
                            amountEditText.setText("")
                            Misc.hideKeyboard(requireActivity())
                            Misc.toast(requireContext(), "Успешно добавлено")
                        } catch (e: PyException) {
                            Misc.toast(
                                requireContext(),
                                "Ссылка не сработала, попробуйте другую")
                        }
                    } else Misc.toast(
                        requireContext(),
                        "Введите количество постов для парсинга"
                    )
                } else Misc.toast(requireContext(), "Введите ссылку на сообщество")
            } else Misc.toast(requireContext(), "Введите название файла")
        }
    }

    private fun getParsedText(link: String, amount: Int): String {
        // https://vk.com/public158343732
        val domain = link.substring(15)
        Logger.d(domain)

        var py: Python? = null
        py = if (!Python.isStarted()) {
            Python.start(AndroidPlatform(requireContext()))
            Python.getInstance()
        } else {
            Python.getInstance()
        }

        val res = py.getModule("main").callAttr("get_posts", amount, domain)
        return res.toString()
    }
}