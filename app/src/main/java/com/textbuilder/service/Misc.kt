package com.textbuilder.service

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast

class Misc {
    companion object {
        fun hideKeyboard(activity: Activity) {
            val focusedView: View? = activity.currentFocus
            focusedView?.clearFocus()
            val imm: InputMethodManager =
                activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(focusedView?.windowToken, 0)
        }

        fun toast(context: Context, text: String) {
            Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
        }
    }
}