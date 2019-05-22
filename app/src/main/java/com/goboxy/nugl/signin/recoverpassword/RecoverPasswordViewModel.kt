package com.goboxy.nugl.signin.recoverpassword

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.text.TextUtils
import com.goboxy.nugl.R
import com.goboxy.nugl.common.Validators.isEmailValid
import com.google.firebase.auth.FirebaseAuth
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe

class RecoverPasswordViewModel : ViewModel() {

    val liveData: MutableLiveData<RecoverPasswordViewState> = MutableLiveData()
    private val firebaseAuth = FirebaseAuth.getInstance()

    init {
        EventBus.getDefault().register(this)
    }

    override fun onCleared() {
        super.onCleared()
        EventBus.getDefault().unregister(this)
    }

    @Subscribe
    fun onSubmitRecoverPasswordClick(event: SubmitRecoverPasswordClickEvent) {
        // Reset errors.
        liveData.value = RecoverPasswordViewState.Failure(null, null)

        val emailStr = event.email
        var errorEmail: Int? = null

        if (TextUtils.isEmpty(emailStr)) {
            errorEmail = R.string.error_field_required
        } else if (!isEmailValid(emailStr.toString())) {
            errorEmail = R.string.error_invalid_email
        }

        if (errorEmail != null) {
            liveData.value = RecoverPasswordViewState.Failure(errorEmail, null)
        } else {
            liveData.value = RecoverPasswordViewState.Loading()
            firebaseAuth.sendPasswordResetEmail(emailStr.toString())
                    .addOnCompleteListener({ task ->
                        if (task.isSuccessful) {
                            liveData.value = RecoverPasswordViewState.Successful(R.string.recover_password_success_message)
                        } else {
                            liveData.value = RecoverPasswordViewState.Failure(null, task.exception)
                        }
                    })
        }
    }

}
