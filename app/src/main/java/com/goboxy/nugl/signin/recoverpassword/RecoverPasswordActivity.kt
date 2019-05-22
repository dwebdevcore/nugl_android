package com.goboxy.nugl.signin.recoverpassword

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import com.goboxy.nugl.R
import com.goboxy.nugl.common.BaseActivity
import com.goboxy.nugl.common.extensions.hideKeyboard
import com.goboxy.nugl.common.extensions.setError
import com.goboxy.nugl.common.extensions.showSnackbar
import kotlinx.android.synthetic.main.activity_recover_password.*
import org.greenrobot.eventbus.EventBus

class RecoverPasswordActivity : BaseActivity() {

    companion object {

        fun start(context: Context) {
            val intent = Intent(context, RecoverPasswordActivity::class.java)
            context.startActivity(intent)
        }

    }

    private val bus = EventBus.getDefault()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recover_password)

        val viewModel = ViewModelProviders.of(this).get(RecoverPasswordViewModel::class.java)
        viewModel.liveData.observe(this, Observer {
            it?.let {
                render(it)
            }
        })

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        activityRecoverPasswordEmailEditText.setOnEditorActionListener(TextView.OnEditorActionListener { _, id, _ ->
            if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                activityRecoverPasswordSubmitButton.callOnClick()
                return@OnEditorActionListener true
            }
            false
        })
        activityRecoverPasswordSubmitButton.setOnClickListener {
            bus.post(SubmitRecoverPasswordClickEvent(activityRecoverPasswordEmailEditText.text))
        }
    }

    private fun render(viewState: RecoverPasswordViewState) {
        when (viewState) {
            is RecoverPasswordViewState.Loading -> {
                hideKeyboard()
                activityRecoverPasswordSubmitButton.setLoading(true)
            }
            is RecoverPasswordViewState.Successful -> {
                onSuccess(viewState)
            }
            is RecoverPasswordViewState.Failure -> {
                onFailure(viewState)
            }
        }
    }

    private fun onSuccess(viewState: RecoverPasswordViewState.Successful) {
        activityRecoverPasswordSubmitButton.setLoading(false)

        viewState.message?.let {
            showSnackbar(it)
        }
    }

    private fun onFailure(viewState: RecoverPasswordViewState.Failure) {
        activityRecoverPasswordSubmitButton.setLoading(false)

        activityRecoverPasswordEmailInputLayout.setError(viewState.errorEmail)

        viewState.error?.message?.let {
            showSnackbar(viewState.error.message.toString())
        }
    }

}

class SubmitRecoverPasswordClickEvent(val email: Editable?)