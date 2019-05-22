package com.goboxy.nugl.common

import android.text.TextUtils
import android.util.Patterns

private const val PASSWORD_MIN_LENGTH = 4

object Validators {

    fun isEmailValid(email: String): Boolean {
        return !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    fun isPasswordValid(password: String): Boolean {
        return password.length > PASSWORD_MIN_LENGTH
    }

}