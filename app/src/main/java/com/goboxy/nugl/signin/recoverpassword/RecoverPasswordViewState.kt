package com.goboxy.nugl.signin.recoverpassword

import android.support.annotation.StringRes

sealed class RecoverPasswordViewState {

    class Successful(

            @StringRes var message: Int?

    ) : RecoverPasswordViewState()

    class Loading : RecoverPasswordViewState()

    class Failure(

            @StringRes var errorEmail: Int?,
            val error: Throwable?

    ) : RecoverPasswordViewState()

}
