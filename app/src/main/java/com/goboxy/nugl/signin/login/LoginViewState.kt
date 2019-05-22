package com.goboxy.nugl.signin.login

import android.support.annotation.StringRes

sealed class LoginViewState {

    class Successful : LoginViewState()

    class Loading : LoginViewState()

    class Failure(

            @StringRes var errorEmail: Int?,
            @StringRes var errorPassword: Int?,
            val errorLogin: Throwable?

    ) : LoginViewState()

}