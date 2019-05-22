package com.goboxy.nugl.splash

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth

class SplashViewModel : ViewModel() {

    val liveData: MutableLiveData<Boolean> = MutableLiveData()

    init {
        val firebaseAuth = FirebaseAuth.getInstance()
        val user = firebaseAuth.currentUser
        liveData.value = user != null
    }

}