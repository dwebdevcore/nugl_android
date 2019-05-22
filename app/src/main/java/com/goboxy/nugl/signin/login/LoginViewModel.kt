package com.goboxy.nugl.signin.login

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.text.TextUtils
import com.goboxy.nugl.R
import com.goboxy.nugl.common.Validators.isEmailValid
import com.goboxy.nugl.common.Validators.isPasswordValid
import com.google.firebase.auth.FirebaseAuth
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe

class LoginViewModel : ViewModel() {

    val liveData: MutableLiveData<LoginViewState> = MutableLiveData()
    private val firebaseAuth = FirebaseAuth.getInstance()

    init {
        EventBus.getDefault().register(this)
    }

    override fun onCleared() {
        super.onCleared()
        EventBus.getDefault().unregister(this)
    }

    @Subscribe
    fun onSignInClick(event: SignInClickEvent) {
        // Reset errors.
        liveData.value = LoginViewState.Failure(null, null, null)

        // Store values at the time of the login attempt.
        val emailStr = event.email
        val passwordStr = event.password

        var errorEmail: Int? = null
        var errorPassword: Int? = null

        if (TextUtils.isEmpty(passwordStr)) {
            errorPassword = R.string.error_field_required
        } else if (!isPasswordValid(passwordStr.toString())) {
            errorPassword = R.string.error_invalid_password
        }

        if (TextUtils.isEmpty(emailStr)) {
            errorEmail = R.string.error_field_required
        } else if (!isEmailValid(emailStr.toString())) {
            errorEmail = R.string.error_invalid_email
        }

        if (errorEmail != null || errorPassword != null) {
            // There was an error; don't attempt login.
            liveData.value = LoginViewState.Failure(errorEmail, errorPassword, null)
        } else {
            // perform the user login attempt.
            liveData.value = LoginViewState.Loading()
            firebaseAuth.signInWithEmailAndPassword(emailStr.toString(), passwordStr.toString())
                    .addOnCompleteListener({ task ->
                        if (task.isSuccessful) {
                            liveData.value = LoginViewState.Successful()
                        } else {
                            liveData.value = LoginViewState.Failure(null, null, task.exception)
                        }
                    })
        }
    }

}