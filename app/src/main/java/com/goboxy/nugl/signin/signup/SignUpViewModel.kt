package com.goboxy.nugl.signin.signup

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.text.TextUtils
import com.goboxy.nugl.R
import com.goboxy.nugl.common.Validators.isEmailValid
import com.goboxy.nugl.common.Validators.isPasswordValid
import com.google.firebase.auth.FirebaseAuth
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe

class SignUpViewModel : ViewModel() {

    val liveData: MutableLiveData<SignUpViewState> = MutableLiveData()
    private val firebaseAuth = FirebaseAuth.getInstance()

    init {
        EventBus.getDefault().register(this)
    }

    override fun onCleared() {
        super.onCleared()
        EventBus.getDefault().unregister(this)
    }

    @Subscribe
    fun onSignUpClick(event: SignUpClickEvent) {
        // Reset errors.
        liveData.value = SignUpViewState.Failure(null, null, null, null, null)

        // Store values at the time of the login attempt.
        val emailStr = event.email
        val passwordStr = event.password
        val confirmPasswordStr = event.confirmPassword

        var errorEmail: Int? = null
        var errorPassword: Int? = null
        var errorConfirmPassword: Int? = null
        var errorToS: Int? = null

        if (TextUtils.isEmpty(passwordStr)) {
            errorPassword = R.string.error_field_required
        } else if (!isPasswordValid(passwordStr.toString())) {
            errorPassword = R.string.error_invalid_password
        }

        if (TextUtils.isEmpty(confirmPasswordStr)) {
            errorConfirmPassword = R.string.error_field_required
        } else if (TextUtils.isEmpty(confirmPasswordStr) || TextUtils.isEmpty(passwordStr) || confirmPasswordStr.toString() != passwordStr.toString()) {
            errorConfirmPassword = R.string.error_invalid_confirm_password
        }

        if (TextUtils.isEmpty(emailStr)) {
            errorEmail = R.string.error_field_required
        } else if (!isEmailValid(emailStr.toString())) {
            errorEmail = R.string.error_invalid_email
        }

        if (!event.ToSAccepted) {
            errorToS = R.string.error_tos_not_agreed
        }

        if (errorEmail != null || errorPassword != null || errorConfirmPassword != null || errorToS != null) {
            // There was an error; don't attempt sign up.
            liveData.value = SignUpViewState.Failure(errorEmail, errorPassword, errorConfirmPassword, errorToS, null)
        } else {
            liveData.value = SignUpViewState.Loading()
            firebaseAuth.createUserWithEmailAndPassword(emailStr.toString(), passwordStr.toString())
                    .addOnCompleteListener({ task ->
                        if (task.isSuccessful) {
                            liveData.value = SignUpViewState.Successful()
                        } else {
                            liveData.value = SignUpViewState.Failure(null, null, null, null, task.exception)
                        }
                    })
        }
    }

}
