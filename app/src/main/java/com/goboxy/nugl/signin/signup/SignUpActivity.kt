package com.goboxy.nugl.signin.signup

import android.app.AlertDialog
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.view.View
import com.goboxy.nugl.R
import com.goboxy.nugl.common.BaseActivity
import com.goboxy.nugl.common.extensions.*
import com.goboxy.nugl.main.MainActivity
import kotlinx.android.synthetic.main.activity_sign_up.*
import org.greenrobot.eventbus.EventBus

class SignUpActivity : BaseActivity() {

    companion object {

        fun start(context: Context) {
            val intent = Intent(context, SignUpActivity::class.java)
            context.startActivity(intent)
        }

    }

    private val bus = EventBus.getDefault()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        supportActionBar?.let {
            it.setDisplayHomeAsUpEnabled(true)
            it.subtitle = getString(R.string.sign_up_subtitle)
        }

        val viewModel = ViewModelProviders.of(this).get(SignUpViewModel::class.java)
        viewModel.liveData.observe(this, Observer {
            it?.let {
                render(it)
            }
        })

        activitySignUpToSCheckBox.setClickableText("" +
                "I agree to the",
                getString(R.string.terms_of_service),
                View.OnClickListener {
                    AlertDialog.Builder(it.context)
                            .setTitle(getString(R.string.terms_of_service))
                            .setMessage(getString(R.string.message_terms_of_service))
                            .setPositiveButton("Close", { dialog, _ -> dialog.dismiss() })
                            .show()
                })
        activitySignUpLoginTextView.setBoldText("Already have an account?", "Sign in.")
        activitySignUpLoginTextView.setOnClickListener { finish() }
        activitySignUpButton.setOnClickListener {
            bus.post(SignUpClickEvent(
                    activitySignUpEmailEditText.text,
                    activitySignUpPasswordEditText.text,
                    activitySignUpConfirmPasswordEditText.text,
                    activitySignUpToSCheckBox.isChecked))
        }
    }

    private fun render(viewState: SignUpViewState) {
        when (viewState) {
            is SignUpViewState.Loading -> {
                hideKeyboard()
                activitySignUpButton.setLoading(true)
            }
            is SignUpViewState.Successful -> {
                MainActivity.start(this)
                finish()
            }
            is SignUpViewState.Failure -> {
                onSignUpFailure(viewState)
            }
        }
    }

    private fun onSignUpFailure(viewState: SignUpViewState.Failure) {
        activitySignUpButton.setLoading(false)

        activitySignUpEmailInputLayout.setError(viewState.errorEmail)
        activitySignUpPasswordInputLayout.setError(viewState.errorPassword)
        activitySignUpConfirmPasswordInputLayout.setError(viewState.errorConfirmPassword)
        activitySignUpToSInputLayout.setError(viewState.errorToS)

        viewState.errorSignUp?.message?.let {
            showSnackbar(viewState.errorSignUp.message.toString())
        }
    }

}

class SignUpClickEvent(val email: Editable?, val password: Editable?, val confirmPassword: Editable?, val ToSAccepted: Boolean)