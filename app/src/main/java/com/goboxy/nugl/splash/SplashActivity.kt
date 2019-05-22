package com.goboxy.nugl.splash

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.goboxy.nugl.main.MainActivity
import com.goboxy.nugl.signin.login.LoginActivity

class SplashActivity : AppCompatActivity(), Observer<Boolean> {

    companion object {

        fun start(context: Context) {
            val intent = Intent(context, SplashActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            context.startActivity(intent)
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val viewModel = ViewModelProviders.of(this).get(SplashViewModel::class.java)
        viewModel.liveData.observe(this, this)
    }

    override fun onChanged(signedIn: Boolean?) {
        if (signedIn!!) {
            MainActivity.start(this)
        } else {
            LoginActivity.start(this)
        }
        finish()
    }

}