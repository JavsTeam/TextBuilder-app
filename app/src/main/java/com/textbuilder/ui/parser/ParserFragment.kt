package com.textbuilder.ui.parser

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import com.chaquo.python.PyException
import com.chaquo.python.Python
import com.chaquo.python.android.AndroidPlatform
import com.textbuilder.R
import com.textbuilder.service.FileHandler
import com.textbuilder.service.Misc
import com.textbuilder.service.PreferencesHandler
import com.textbuilder.ui.upload.dialog.DeleteByTagDialogFragment

class ParserFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_parser, container, false)

        init(rootView)
        initDeleteButton(rootView.findViewById(R.id.parser_fragment_button_delete))

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

            if (tag.isNotEmpty()) {
                if (link.isNotEmpty()) {
                    val amountText = amountEditText.text.toString()
                    val amount = if (amountText.isEmpty()) 100 else amountText.toInt()
                    if (amount <= 1000) {
                        tagEditText.setText("")
                        linkEditText.setText("")
                        amountEditText.setText("")
                        Misc.hideKeyboard(requireActivity())
                        try {
                            var text = getParsedText(link, amount)
                            text = FileHandler.cleanText(text)
                            FileHandler.addNewFileWithTextAndSaveTag(
                                PreferencesHandler(requireActivity()),
                                requireContext(),
                                tag,
                                text
                            )
                            Misc.toast(requireContext(), "Успешно добавлено")
                        } catch (e: PyException) {
                            Misc.toast(
                                requireContext(),
                                "Ссылка не сработала, попробуйте другую"
                            )
                        }
                    } else Misc.toast(requireContext(), "Количество поство должно бять меньше 1000")
                } else Misc.toast(requireContext(), "Введите ссылку на сообщество")
            } else Misc.toast(requireContext(), "Введите название файла")
        }
    }

    private fun getParsedText(link: String, amount: Int): String {
        val domain = link.substring(15)

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

    private fun initDeleteButton(deleteButton: ImageButton) {
        val buttonScaleAnimation: Animation =
            AnimationUtils.loadAnimation(requireContext(), R.anim.button_scale)
        deleteButton.setOnClickListener {
            it.startAnimation(buttonScaleAnimation)
            val myDialogFragment = DeleteByTagDialogFragment()
            val manager = requireActivity().supportFragmentManager

            myDialogFragment.show(manager, "DeleteByTagDialog")
        }
    }
}