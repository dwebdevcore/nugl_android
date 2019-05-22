package com.goboxy.nugl.common.extensions

import android.graphics.Typeface
import android.support.annotation.StringRes
import android.support.design.widget.TextInputLayout
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.TextUtils
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.StyleSpan
import android.view.View
import android.widget.TextView


fun TextView.setBoldText(charSequence: CharSequence?, boldText: CharSequence?) {
    text = if (!TextUtils.isEmpty(boldText)) {
        val spannableString = SpannableString(boldText)
        spannableString.setSpan(StyleSpan(Typeface.BOLD), 0, boldText?.length!!, 0)
        TextUtils.concat(charSequence, " ", spannableString)
    } else {
        charSequence
    }
}

fun TextView.setClickableText(charSequence: CharSequence?, clickableText: CharSequence?, clickListener: View.OnClickListener) {
    val spannable = SpannableString(clickableText)

    val clickableSpan = object : ClickableSpan() {
        override fun onClick(view: View) {
            clickListener.onClick(view)
        }

        override fun updateDrawState(textPaint: TextPaint) {
            super.updateDrawState(textPaint)
            textPaint.isUnderlineText = false
        }
    }

    spannable.setSpan(clickableSpan, 0, spannable.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
    movementMethod = LinkMovementMethod.getInstance()
    text = TextUtils.concat(charSequence, " ", spannable)
}

fun TextInputLayout.setError(@StringRes errorStringRes: Int?) {
    if (errorStringRes != null) {
        isErrorEnabled = true
        error = context.getString(errorStringRes)
    } else {
        error = null
        isErrorEnabled = false
    }
}