package com.goboxy.nugl.main.account

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.goboxy.nugl.R
import com.goboxy.nugl.main.MainBottomFragment
import com.goboxy.nugl.splash.SplashActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_account.*

class AccountFragment : MainBottomFragment() {

    companion object {

        fun newInstance(): AccountFragment {
            return AccountFragment()
        }

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_account, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentAccountSignOutButton.setOnClickListener({
            FirebaseAuth.getInstance().signOut()
            SplashActivity.start(it.context)
        })
    }
}