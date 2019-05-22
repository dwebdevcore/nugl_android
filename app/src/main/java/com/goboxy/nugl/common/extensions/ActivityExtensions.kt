package com.goboxy.nugl.common.extensions

import android.app.Activity
import android.content.Context
import android.support.annotation.StringRes
import android.support.design.widget.Snackbar
import android.view.View
import android.view.inputmethod.InputMethodManager

fun Activity.showSnackbar(message: CharSequence) {
    val contentView = findViewById<View>(android.R.id.content)
    Snackbar.make(contentView, message, Snackbar.LENGTH_LONG).show()
}

fun Activity.showSnackbar(@StringRes message: Int) {
    showSnackbar(getString(message))
}

fun Activity.hideKeyboard() {
    val view = currentFocus ?: return

    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(view.windowToken, 0)
}