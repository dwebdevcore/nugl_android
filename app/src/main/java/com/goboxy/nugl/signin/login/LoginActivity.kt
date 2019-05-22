package com.goboxy.nugl.signin.login

import android.Manifest.permission.READ_CONTACTS
import android.app.LoaderManager.LoaderCallbacks
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.CursorLoader
import android.content.Intent
import android.content.Loader
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.ContactsContract
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.view.inputmethod.EditorInfo
import android.widget.ArrayAdapter
import android.widget.TextView
import com.goboxy.nugl.R
import com.goboxy.nugl.common.extensions.hideKeyboard
import com.goboxy.nugl.common.extensions.setBoldText
import com.goboxy.nugl.common.extensions.setError
import com.goboxy.nugl.common.extensions.showSnackbar
import com.goboxy.nugl.main.MainActivity
import com.goboxy.nugl.signin.recoverpassword.RecoverPasswordActivity
import com.goboxy.nugl.signin.signup.SignUpActivity
import kotlinx.android.synthetic.main.activity_login.*
import org.greenrobot.eventbus.EventBus
import java.util.*

private const val REQUEST_READ_CONTACTS_PERMISSION = 1030

class LoginActivity : AppCompatActivity(), LoaderCallbacks<Cursor> {

    companion object {


        fun start(context: Context) {
            val intent = Intent(context, LoginActivity::class.java)
            context.startActivity(intent)
        }

    }

    private val bus = EventBus.getDefault()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val viewModel = ViewModelProviders.of(this).get(LoginViewModel::class.java)
        viewModel.liveData.observe(this, Observer {
            it?.let {
                render(it)
            }
        })

        activityLoginSignUpTextView.setBoldText("Don't have an account?", "Sign up.")
        activityLoginForgotPasswordTextView.setBoldText("Forgot your password?", "Recover it.")

        // Set up the login form.
        populateAutoComplete()
        activityLoginPasswordEditText.setOnEditorActionListener(TextView.OnEditorActionListener { _, id, _ ->
            if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                activityLoginSignInButton.callOnClick()
                return@OnEditorActionListener true
            }
            false
        })

        activityLoginSignInButton.setOnClickListener {
            bus.post(SignInClickEvent(activityLoginEmailEditText.text, activityLoginPasswordEditText.text))
        }
        activityLoginForgotPasswordTextView.setOnClickListener {
            RecoverPasswordActivity.start(this@LoginActivity)
        }
        activityLoginSignUpTextView.setOnClickListener {
            SignUpActivity.start(this@LoginActivity)
        }
    }

    private fun render(viewState: LoginViewState) {
        when (viewState) {
            is LoginViewState.Loading -> {
                hideKeyboard()
                activityLoginSignInButton.setLoading(true)
            }
            is LoginViewState.Successful -> {
                MainActivity.start(this)
                finish()
            }
            is LoginViewState.Failure -> {
                onLoginFailure(viewState)
            }
        }
    }

    private fun onLoginFailure(viewState: LoginViewState.Failure) {
        activityLoginSignInButton.setLoading(false)

        activityLoginEmailInputLayout.setError(viewState.errorEmail)
        activityLoginPasswordInputLayout.setError(viewState.errorPassword)

        viewState.errorLogin?.message?.let {
            showSnackbar(viewState.errorLogin.message.toString())
        }
    }

    private fun populateAutoComplete() {
        if (!mayRequestContacts()) {
            return
        }

        loaderManager.initLoader(0, null, this)
    }

    private fun mayRequestContacts(): Boolean {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true
        }
        if (checkSelfPermission(READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            return true
        }
        if (shouldShowRequestPermissionRationale(READ_CONTACTS)) {
            Snackbar.make(activityLoginEmailEditText, R.string.permission_rationale, Snackbar.LENGTH_INDEFINITE)
                    .setAction(android.R.string.ok,
                            { requestPermissions(arrayOf(READ_CONTACTS), REQUEST_READ_CONTACTS_PERMISSION) })
        } else {
            requestPermissions(arrayOf(READ_CONTACTS), REQUEST_READ_CONTACTS_PERMISSION)
        }
        return false
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>,
                                            grantResults: IntArray) {
        if (requestCode == REQUEST_READ_CONTACTS_PERMISSION) {
            if (grantResults.size == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                populateAutoComplete()
            }
        }
    }

    override fun onCreateLoader(i: Int, bundle: Bundle?): Loader<Cursor> {
        return CursorLoader(this,
                // Retrieve liveData rows for the device user's 'profile' contact.
                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,

                // Select only email addresses.
                ContactsContract.Contacts.Data.MIMETYPE + " = ?", arrayOf(ContactsContract.CommonDataKinds.Email
                .CONTENT_ITEM_TYPE),

                // Show primary email addresses first. Note that there won't be
                // a primary email address if the user hasn't specified one.
                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC")
    }

    override fun onLoadFinished(cursorLoader: Loader<Cursor>, cursor: Cursor) {
        val emails = ArrayList<String>()
        cursor.moveToFirst()
        while (!cursor.isAfterLast) {
            emails.add(cursor.getString(ProfileQuery.ADDRESS))
            cursor.moveToNext()
        }

        addEmailsToAutoComplete(emails)
    }

    override fun onLoaderReset(cursorLoader: Loader<Cursor>) {

    }

    private fun addEmailsToAutoComplete(emailAddressCollection: List<String>) {
        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        val adapter = ArrayAdapter(this@LoginActivity,
                android.R.layout.simple_dropdown_item_1line, emailAddressCollection)

        activityLoginEmailEditText.setAdapter(adapter)
    }

    object ProfileQuery {
        val PROJECTION = arrayOf(
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY)
        const val ADDRESS = 0
    }

}

class SignInClickEvent(val email: Editable?, val password: Editable?)