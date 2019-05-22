package com.goboxy.nugl.signin.signup

import android.support.annotation.StringRes

sealed class SignUpViewState {

    class Successful : SignUpViewState()

    class Loading : SignUpViewState()

    class Failure(

            @StringRes var errorEmail: Int?,
            @StringRes var errorPassword: Int?,
            @StringRes var errorConfirmPassword: Int?,
            @StringRes var errorToS: Int?,
            val errorSignUp: Throwable?

    ) : SignUpViewState()

}